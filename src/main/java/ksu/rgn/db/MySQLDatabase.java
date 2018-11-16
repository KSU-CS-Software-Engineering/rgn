package ksu.rgn.db;

import ksu.rgn.scenario.Node;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public class MySQLDatabase extends Thread implements DBQueries {

    private static final Logger LOG = LoggerFactory.getLogger(MySQLDatabase.class);

    private String url, dbName, user, password;

    @Override
    public void open(String url, String dbName, String user, String password) {
        this.url = url;
        this.dbName = dbName;
        this.user = user;
        this.password = password;

        setDaemon(true);
        start();
    }

    private Runnable onOpenSuccess = null, onOpenFail = null, onSyncFinished = null, onSyncStarted = null;
    public void onOpenSuccess(Runnable onOpenSuccess) {
        this.onOpenSuccess = onOpenSuccess;
    }
    public void onOpenFail(Runnable onOpenFail) {
        this.onOpenFail = onOpenFail;
    }
    public void onSyncFinished(Runnable onSyncFinished) {
        this.onSyncFinished = onSyncFinished;
    }
    public void onSyncStarted(Runnable onSyncStarted) {
        this.onSyncStarted = onSyncStarted;
    }

    private final ArrayList<Job> jobs = new ArrayList<>();
    private boolean close = false;
    private final Object lock = new Object();
    @Override
    public void run() {
        try {
            final HashMap<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.user", user);
            properties.put("javax.persistence.jdbc.password", password);
            properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + url + "/" + dbName);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("RGN", properties);
            EntityManager em = emf.createEntityManager();
            em.setFlushMode(FlushModeType.COMMIT);


            if (onOpenSuccess != null) onOpenSuccess.run();

            try {
                boolean syncStarted = false;
                while (!close || !jobs.isEmpty()) {
                    Job j = null;
                    synchronized (lock) {
                        if (!jobs.isEmpty()) {
                            j = jobs.remove(0);
                        }
                    }
                    if (j != null) {
                        if (!syncStarted && onSyncStarted != null) onSyncStarted.run();
                        syncStarted = true;

                        try {
                            j.run(em);
                        } catch (RollbackException ignored) {}
                        j.dbf.forEach(DBFuture::invokeFinished);
                    }

                    synchronized (lock) {
                        if (jobs.isEmpty()) {
                            if (syncStarted && onSyncFinished != null) onSyncFinished.run();
                            syncStarted = false;
                            lock.wait();
                        }
                    }
                }

                if (emf.isOpen()) emf.close();
            } catch (Exception e) { // IOExc? DBExc?
                LOG.error("Database error", e);
                if (emf.isOpen()) emf.close();
                close = true;
            } finally {
                if (onClosed != null) onClosed.run();
            }

        } catch (Exception e) {
            if (onOpenFail != null) onOpenFail.run();
        }
    }

    private Runnable onClosed = null;
    @Override
    public void close(Runnable onClosed) {
        this.onClosed = onClosed;
        close = true;
        synchronized (lock) { lock.notify(); }
    }

    void addJob(Job j, DBFuture f) {
        LOG.info("Queueing new DB job: {}", j);
        synchronized (lock) {
            jobs.removeIf(j::mergeActions);
            jobs.add(j);
            j.dbf.add(f);
            lock.notify();
        }
    }

    @Override
    public void getAllScenarios(Consumer<List<Scenario>> cb) {
        addJob(new Job.Query("SELECT s FROM Scenario s", Scenario.class, list -> {
            if (cb != null) {
                List<Scenario> result = (List<Scenario>) list;
                cb.accept(result);
            }
        }), new DBFuture());
    }

    @Override
    public DBFuture persist(Runnable action, Object o) {
        final DBFuture f = new DBFuture();
        if (action == null) {
            addJob(new Job.SimplePersist(o), f);
        } else {
            addJob(new Job.ActionPersist(action, o), f);
        }

        return f;
    }

    @Override
    public DBFuture dropScenario(Scenario s) {
        final DBFuture f = new DBFuture();
        addJob(new Job.SimpleDrop(s), f);
        return f;
    }

    @Override
    public DBFuture dropNode(Node n) {
        final DBFuture f = new DBFuture();
        addJob(new Job.SimpleDrop(n), f);
        return f;
    }

    @Override
    public DBFuture dropTruck(Truck t) {
        final DBFuture f = new DBFuture();
        addJob(new Job.SimpleDrop(t), f);
        return f;
    }

}
