package com.CIS642.Rural.GroceryImplementation.arcgis;

import com.CIS642.Rural.GroceryImplementation.utils.Future;
import com.darkyen.dave.Webb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 *
 */
public class GISBridge extends Thread {

    private final String URL = "https://www.arcgis.com";

    private static final Logger LOG = LoggerFactory.getLogger(GISBridge.class);
    final String tmpToken;
    final Webb api;

    public GISBridge(String tmpToken) {
        this.tmpToken = tmpToken;

        this.api = new Webb(URL);

        setDaemon(true);
        start();
    }


    private final ArrayList<GISJob> jobs = new ArrayList<>();
    private boolean close = false;
    private final Object lock = new Object();

    public Future addJob(GISJob j) {
        LOG.info("Queueing new GIS job: {}", j);
        Future f = new Future();
        j.bridge = this;
        j.future = f;
        synchronized (lock) {
            jobs.add(j);
            lock.notify();
        }
        return f;
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
                    j.run();
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
