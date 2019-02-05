package ksu.rgn.db;

import ksu.rgn.scenario.MapNode;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;
import ksu.rgn.utils.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public class MySQLDBDatabase extends Thread implements DBQueries {

    private static final Logger LOG = LoggerFactory.getLogger(MySQLDBDatabase.class);

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

    private final ArrayList<DBJob> dbJobs = new ArrayList<>();
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
                while (!close || !dbJobs.isEmpty()) {
                    DBJob j = null;
                    synchronized (lock) {
                        if (!dbJobs.isEmpty()) {
                            j = dbJobs.remove(0);
                        }
                    }
                    if (j != null) {
                        if (!syncStarted && onSyncStarted != null) onSyncStarted.run();
                        syncStarted = true;

                        try {
                            j.run(em);
                            j.dbf.forEach(f -> f.invokeSuccess(null));
                        } catch (RollbackException e) {
                            j.dbf.forEach(f -> f.invokeFail(e));
                        }

                    }

                    synchronized (lock) {
                        if (dbJobs.isEmpty()) {
                            if (syncStarted && onSyncFinished != null) onSyncFinished.run();
                            syncStarted = false;
                            if (!close) lock.wait();
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

    void addJob(DBJob j, Future f) {
        synchronized (lock) {
            dbJobs.removeIf(j::mergeActions);
            dbJobs.add(j);
            j.dbf.add(f);
            lock.notify();
        }
    }

    @Override
    public Future preCache(Runnable job) {
        final Future f = new Future();
        addJob(new DBJob() {
            @Override
            public void run(EntityManager em) {
                job.run();
            }
        }, f);
        return f;
    }

    @Override
    public void getAllScenarios(Consumer<List<Scenario>> cb) {
        addJob(new DBJob.Query("SELECT s FROM Scenario s", Scenario.class, list -> {
            if (cb != null) {
                List<Scenario> result = (List<Scenario>) list;
                cb.accept(result);
            }
        }), new Future());
    }

    @Override
    public Future persist(Runnable action, Object o) {
        final Future f = new Future();
        if (action == null) {
            addJob(new DBJob.SimplePersist(o), f);
        } else {
            addJob(new DBJob.ActionPersist(action, o), f);
        }

        return f;
    }

    @Override
    public Future dropScenario(Scenario s) {
        final Future f = new Future();
        addJob(new DBJob.SimpleDrop(s), f);
        return f;
    }

    @Override
    public Future dropNode(MapNode n) {
        final Future f = new Future();
        addJob(new DBJob.SimpleDrop(n), f);
        return f;
    }

    @Override
    public Future dropTruck(Truck t) {
        final Future f = new Future();
        addJob(new DBJob.SimpleDrop(t), f);
        return f;
    }

}
