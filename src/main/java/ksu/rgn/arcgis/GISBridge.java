package ksu.rgn.arcgis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 *
 */
public class GISBridge extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(GISBridge.class);
    private final String url, clientID, token;

    public GISBridge(String url, String clientID, String token) {
        this.url = url;
        this.clientID = clientID;
        this.token = token;

        setDaemon(true);
        start();
    }


    private final ArrayList<GISJob> jobs = new ArrayList<>();
    private boolean close = false;
    private final Object lock = new Object();

    public void testConnection(GISFuture future) {
        final GISJob job = new GISJob() {
            @Override
            public void run(GISBridge b) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}

                future.invokeFail("Not implemented yet");
            }

            @Override
            public String toString() {
                return "GISJob.testConnection()";
            }
        };
        job.future = future;

        addJob(job);
    }

    void addJob(GISJob j) {
        LOG.info("Queueing new GIS job: {}", j);
        synchronized (lock) {
            jobs.add(j);
            lock.notify();
        }
    }

    @Override
    public void run() {
        try {
            while (!close || !jobs.isEmpty()) {
                GISJob j = null;
                synchronized (lock) {
                    if (!jobs.isEmpty()) {
                        j = jobs.remove(0);
                    }
                }
                if (j != null) {
                    j.run(this);
                }

                synchronized (lock) {
                    if (jobs.isEmpty()) {
                        lock.wait();
                    }
                }
            }

        } catch (Exception e) { // IOExc? DBExc?
            LOG.error("ArcGIS bridge error", e);
            close = true;
        }
    }

    public void close() {
        this.close = true;
    }


}
