package ksu.rgn.db;

import ksu.rgn.scenario.Node;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

                        j.run(em);
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
            }

        } catch (Exception e) {
            if (onOpenFail != null) onOpenFail.run();
        }
    }

    @Override
    public void close() {
        close = true;
    }

    void addJob(Job j) {
        LOG.info("Queueing new DB job: {}", j);
        synchronized (lock) {
            jobs.removeIf(j::makesObsolete);
            jobs.add(j);
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
        }));
    }

    @Override
    public void persistScenario(Scenario s) {
        addJob(new Job.SimplePersist(s));
    }

    @Override
    public void persistNode(Node n) {
        addJob(new Job.SimplePersist(n));
    }

    @Override
    public void persistTruck(Truck t) {
        addJob(new Job.SimplePersist(t));
    }

}
