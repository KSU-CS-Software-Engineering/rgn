package ksu.rgn.arcgis;

import com.darkyen.dave.Response;
import com.darkyen.dave.ResponseTranslator;
import com.darkyen.dave.Webb;
import com.darkyen.dave.WebbException;
import ksu.rgn.scenario.Node;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 *
 */
public class GISBridge extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(GISBridge.class);
    private final String url, clientID, token;

    private final Webb api;

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
            public void run(GISBridge b) {
                try {
                    Response<String> r = api
                            .post("/ArcGIS/rest/info/healthCheck?f=pjson")
                            .execute(ResponseTranslator.STRING_TRANSLATOR);
                    String responseString = r.getBody();
                    JSONObject response = new JSONObject(responseString);
                    Object o = response.get("success");
                    boolean success = (boolean)o;
                    if(success){
                        future.invokeSuccess("The server is operational.");
                    }
                    else{
                        future.invokeFail("The server is not operational.");
                    }
                }
                catch(WebbException we)
                {
                    LOG.warn("Exception in connecting to ArcGIS routing server", we);
                    future.invokeFail("The server could not be reached");
                }
                catch(NullPointerException npe)
                {
                    LOG.warn("ArcGIS sample server returning unexpected null value", npe);
                    future.invokeFail("The server did not respond expectedly");
                }
            }

            @Override
            public String toString() {
                return "GISJob.testConnection()";
            }
        };
        job.future = future;

        addJob(job);
    }

    public void getRoute(GISFuture future, ArrayList<Node> nodes) {
        final GISJob job = new GISJob() {
            @Override
            public void run(GISBridge bridge) {
                try {
                    final JSONObject parameters = new JSONObject();
                    parameters.append("stops", getStopsJson(nodes));
                    parameters.put("token", token);
                    parameters.put("f", "pjson");
                    Response<String> r = api
                            .post("arcgis/rest/services/World/Route/NAServer/Route_World/solve?")
                            .bodyJson(parameters.toString())
                            .execute(ResponseTranslator.STRING_TRANSLATOR);
                    String responseString = r.getBody();
                    future.invokeSuccess(responseString);
                }
                catch(WebbException we)
                {
                    LOG.warn("Exception in connecting to ArcGIS routing server", we);
                    future.invokeFail("The server could not be reached");
                }
                catch(NullPointerException npe){
                    LOG.warn("ArcGIS routing server returning unexpected null value", npe);
                    future.invokeFail("The server did not respond expectedly");
                }
            }
            public String toString() {
                return "GISJob.getRoute()";
            }
        };
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

    public JSONObject getStopsJson(ArrayList<Node> nodes){
        JSONObject features = new JSONObject();
        for(Node n : nodes)
        {
            JSONObject geometry = new JSONObject();
            geometry.put("x", n.location.lat);
            geometry.put("y", n.location.lon);
            JSONObject attributes = new JSONObject();
            attributes.put("name", n.ID);

            JSONObject feature = new JSONObject();
            feature.put("geometry", geometry);
            feature.put("attributes", attributes);
            features.append("features", feature);
        }
        return features;
    }
    public void close() {
        this.close = true;
    }


}
