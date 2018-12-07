package ksu.rgn.arcgis;

import com.darkyen.dave.Response;
import com.darkyen.dave.ResponseTranslator;
import com.darkyen.dave.WebbException;
import ksu.rgn.scenario.MapNode;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class RouteGISJob extends GISJob {

    private static final Logger LOG = LoggerFactory.getLogger(RouteGISJob.class);

    private final List<MapNode> nodes;
    private final GISBridge bridge;

    public RouteGISJob(GISBridge bridge, List<MapNode> routeStops) {
        this.nodes = routeStops;
        this.bridge = bridge;
    }

    @Override
    public void run() {
        try {
            final JSONObject parameters = new JSONObject();
            parameters.append("stops", getStopsJson(nodes));
            parameters.put("token", bridge.token);
            parameters.put("f", "pjson");
            Response<String> r = bridge.api
                    .post("/arcgis/rest/services/World/Route/NAServer/Route_World/solve?")
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

    private JSONObject getStopsJson(List<MapNode> nodes) {
        final JSONObject features = new JSONObject();
        for(MapNode n : nodes) {
            final JSONObject geometry = new JSONObject();
            geometry.put("x", n.location.lat);
            geometry.put("y", n.location.lon);
            final JSONObject attributes = new JSONObject();
            attributes.put("name", n.ID);

            final JSONObject feature = new JSONObject();
            feature.put("geometry", geometry);
            feature.put("attributes", attributes);
            features.append("features", feature);
        }
        return features;
    }

    @Override
    public String toString() {
        return "GISJob.Route";
    }

}
