package ksu.rgn.arcgis.jobs;

import com.darkyen.dave.WebbException;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.loadable.*;
import com.esri.arcgisruntime.tasks.networkanalysis.*;
import ksu.rgn.arcgis.GISJob;
import ksu.rgn.scenario.MapNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

    private static final SpatialReference reference = SpatialReference.create(4326);

    @Override
    public void run() {
        final String routeTaskWorld = "http://route.arcgis.com/arcgis/rest/services/World/Route/NAServer";
        RouteTask routeTask = new RouteTask(routeTaskWorld);
        routeTask.loadAsync();
        RouteParameters rp = new RouteParameters();

        routeTask.addDoneLoadingListener(() -> {
            if (routeTask.getLoadError() == null && routeTask.getLoadStatus() == LoadStatus.LOADED) {
                // route task has loaded successfully
            }
        });
        try {
            // get default route parameters
            rp = routeTask.createDefaultParametersAsync().get();
            // set flags to return stops and directions
            rp.setReturnStops(true);
            rp.setReturnDirections(true);
        } catch (Exception e) {
            future.invokeFail("Failed to route");
        }

        // add route stops
        rp.setStops(getStopsFromNodes(nodes));

        try {
            RouteResult result = routeTask.solveRouteAsync(rp).get();
            future.invokeSuccess(result);
        } catch (Exception ex) {
            future.invokeFail("Failed to route");
        }
    }

    private List<Stop> getStopsFromNodes(List<MapNode> nodes) {
        ArrayList<Stop> stops = new ArrayList<Stop>();
        for(MapNode n : nodes) {
            Stop stop = new Stop(new Point(n.gpsLat, n.gpsLon, reference));
            stops.add(stop);
        }
        return stops;
    }
}
