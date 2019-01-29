package ksu.rgn.arcgis.jobs;

import com.darkyen.dave.WebbException;
import ksu.rgn.arcgis.GISJob;
import ksu.rgn.scenario.MapNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class PlanRouteJ extends GISJob {

    private static final Logger LOG = LoggerFactory.getLogger(PlanRouteJ.class);

    private final List<MapNode> nodes;

    public PlanRouteJ(List<MapNode> routeStops) {
        this.nodes = routeStops;
    }

    @Override
    public void run() {
        try {

            // TODO (nils)

            final JSONObject parameters = new JSONObject();
            parameters.append("stops", getStopsJson(nodes));

            final JSONObject r = request("/ArcGIS/rest/services/World/Route/NAServer/Route_World/solve", parameters);


            future.invokeSuccess(r);

        } catch (WebbException we) {
            LOG.warn("GIS Server could not be reached");
            future.invokeFail("The server could not be reached");
        } catch (JSONException e) {
            LOG.warn("GIS Server replied with unexpected response");
            LOG.debug("Cause: {}", e);
            future.invokeFail("Unexpected response");
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
}
