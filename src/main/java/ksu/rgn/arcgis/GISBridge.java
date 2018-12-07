package ksu.rgn.arcgis;

import com.darkyen.dave.Response;
import com.darkyen.dave.ResponseTranslator;
import com.darkyen.dave.Webb;
import com.darkyen.dave.WebbException;
import ksu.rgn.scenario.MapNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 *
 */
public class GISBridge extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(GISBridge.class);
    final String url, clientID, token;
    final Webb api;

    public GISBridge(String url, String clientID, String token) {
        this.url = url;
        this.clientID = clientID;
        this.token = token;

        this.api = new Webb(url);

        setDaemon(true);
        start();
    }


    private final ArrayList<GISJob> jobs = new ArrayList<>();
    private boolean close = false;
    private final Object lock = new Object();


    public void testConnection(GISFuture future) {
        final GISJob job = new GISJob() {
            @Override
            public void run() {
                try {
                    final Response<String> r = api
                            .post("/ArcGIS/rest/info/healthCheck?f=pjson")
                            .execute(ResponseTranslator.STRING_TRANSLATOR);
                    final String responseString = r.getBody();
                    final JSONObject response = new JSONObject(responseString);
                    if ((boolean) response.get("success")) {
                        future.invokeSuccess("Server is running");
                    } else {
                        future.invokeFail("Could not contact server");
                    }
                } catch (WebbException we) {
                    LOG.warn("Exception in connecting to ArcGIS routing server", we);
                    future.invokeFail("The server could not be reached");
                } catch (JSONException e) {
                    LOG.warn("Server returned unexpected values", e);
                    future.invokeFail("Unexpected response");
                }
            }

            @Override
            public String toString() {
                return "GISJob.TestConnection";
            }
        };
        job.future = future;

        addJob(job);
    }

    public void getRoute(GISFuture future, ArrayList<MapNode> nodes) {
        final RouteGISJob job = new RouteGISJob(this, nodes);

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
