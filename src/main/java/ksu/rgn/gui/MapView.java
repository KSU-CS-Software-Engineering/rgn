package ksu.rgn.gui;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
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

/**
 *
 */
public class MapView {

    private static MapView current;
    public static MapView current() { // Hack
        return current;
    }


    private GraphicsOverlay graphicsOverlay;
    public static Node create(Scenario s, Window owner) {
        current = new MapView();

        current.graphicsOverlay = new GraphicsOverlay();
        Basemap basemap = Basemap.createStreets();

        ArcGISMap map = new ArcGISMap(basemap);

        com.esri.arcgisruntime.mapping.view.MapView mapView = new com.esri.arcgisruntime.mapping.view.MapView();
        map.setMaxScale(5000);
        mapView.setMap(map);

        mapView.getGraphicsOverlays().add(current.graphicsOverlay);

        current.updateGraphics(s.getNodes());

        return mapView;
    }

    void updateMarkers(List<MapNode> nodes) {
        updateGraphics(nodes);
    }

    private void updateGraphics(List<MapNode> nodes) {
        SpatialReference reference = SpatialReference.create(4326);
        MarkerSymbol marker = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.X, 0xFF800080, 15);

        final ListenableList<Graphic> g = graphicsOverlay.getGraphics();
        g.clear();
        for (MapNode n : nodes) {
            g.add(new Graphic(new Point(n.gpsLon, n.gpsLat, reference), marker));
        }
    }

}

