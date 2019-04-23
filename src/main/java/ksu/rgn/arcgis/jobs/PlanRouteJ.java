package ksu.rgn.arcgis.jobs;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import ksu.rgn.arcgis.GISJob;
import ksu.rgn.scenario.MapNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class PlanRouteJ extends GISJob {

    private static final Logger LOG = LoggerFactory.getLogger(PlanRouteJ.class);
    private static final String ROUTE_TASK_URL = "https://route.arcgis.com/arcgis/rest/services/World/Route/NAServer";

    private final Set<MapNode> nodes;

    public PlanRouteJ(Set<MapNode> routeStops) {
        this.nodes = routeStops;
    }

    private static final SpatialReference reference = SpatialReference.create(4326);

    @Override
    public void run() {
        RouteTask routeTask = new RouteTask(ROUTE_TASK_URL);
        routeTask.loadAsync();
        RouteParameters rp = new RouteParameters();

        routeTask.setCredential(null); // TODO(JSmith): we need a valid authentication

        routeTask.addDoneLoadingListener(() -> {
            LOG.debug("Done loading");
            if (routeTask.getLoadError() == null) {
                if (routeTask.getLoadStatus() == LoadStatus.LOADED) {
                    LOG.debug("No error");
                } else {
                    LOG.debug("?");
                }
            } else {
                LOG.warn("Error", routeTask.getLoadError());
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

    private List<Stop> getStopsFromNodes(Set<MapNode> nodes) {
        ArrayList<Stop> stops = new ArrayList<>();
        for(MapNode n : nodes) {
            Stop stop = new Stop(new Point(n.gpsLon, n.gpsLat, reference));
            stops.add(stop);
        }
        return stops;
    }
}
