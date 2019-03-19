package ksu.rgn.gui;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.MarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import javafx.scene.Node;
import javafx.stage.Window;
import ksu.rgn.scenario.MapNode;
import ksu.rgn.scenario.Scenario;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class MapView {

    private static MapView current;
    public static MapView current() { // Hack
        return current;
    }

    private static final SpatialReference reference = SpatialReference.create(4326);

    private GraphicsOverlay graphicsOverlay;
    private com.esri.arcgisruntime.mapping.view.MapView mapView;
    public static Node create(Scenario s, Window owner) {
        if (current != null) current.mapView.dispose();
        current = new MapView();

        current.graphicsOverlay = new GraphicsOverlay();
        Basemap basemap = Basemap.createStreets();

        ArcGISMap map = new ArcGISMap(basemap);

        current.mapView = new com.esri.arcgisruntime.mapping.view.MapView();
        map.setMaxScale(5000);
        current.mapView.setMap(map);

        current.mapView.getGraphicsOverlays().add(current.graphicsOverlay);

        current.updateGraphics(s.getNodes());
        current.setViewpoint(s.getNodes(), map);

        return current.mapView;
    }

    void updateMarkers(List<MapNode> nodes) {
        updateGraphics(nodes);
    }

    private static final float marginPercent = 1.3f;
    private void setViewpoint(List<MapNode> nodes, ArcGISMap map) {
        final PointCollection points = new PointCollection(reference);

        for (MapNode n : nodes) {
            points.add(new Point(n.gpsLon, n.gpsLat, reference));
        }

        final Envelope preciseEnvelope = new Polygon(points).getExtent();
        if (preciseEnvelope.getWidth() > 0 || preciseEnvelope.getHeight() > 0) {
            final Envelope marginEnvelope = new Envelope(
                    preciseEnvelope.getCenter(),
                    preciseEnvelope.getWidth() * marginPercent + 1,
                    preciseEnvelope.getHeight() * marginPercent + 1
            );
            map.setInitialViewpoint(new Viewpoint(marginEnvelope));
        } else if (nodes.size() > 0) {
            map.setInitialViewpoint(new Viewpoint(new Point(nodes.get(0).gpsLon, nodes.get(0).gpsLat, reference), 30000));
        } else {
            map.setInitialViewpoint(new Viewpoint(new Point(0, 0, reference), Double.MAX_VALUE));
        }

//        promise.addDoneListener(() -> System.out.println("Done"));
    }

    private void updateGraphics(List<MapNode> nodes) {
        MarkerSymbol marker = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.X, 0xFF800080, 15);

        final ListenableList<Graphic> g = graphicsOverlay.getGraphics();
        g.clear();
        for (MapNode n : nodes) {
            g.add(new Graphic(new Point(n.gpsLon, n.gpsLat, reference), marker));
        }
    }

}

