package com.CIS642.Rural.GroceryImplementation.gui;

import com.CIS642.Rural.GroceryImplementation.scenario.MapNode;
import com.CIS642.Rural.GroceryImplementation.scenario.Scenario;
import com.CIS642.Rural.GroceryImplementation.utils.Tuple2;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.LicenseStatus;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

import java.util.Set;
import java.util.function.Consumer;

/**
 *
 */
public class MapView {

    private static MapView current;

    public static MapView current() { // Hack
        return current;
    }

    private static final SpatialReference reference = SpatialReference.create(4326);

    private boolean valid = false;
    public Window w;

    private GraphicsOverlay graphicsOverlay;
    private com.esri.arcgisruntime.mapping.view.MapView mapView;

    public static Node create(Scenario s, String license, Window w) {
        if (current != null) {
            if (current.valid) current.mapView.dispose();
        }
        current = new MapView();
        current.w = w;

        if (license == null || license.isEmpty() || ArcGISRuntimeEnvironment.setLicense(license).getLicenseStatus() != LicenseStatus.VALID) {

            final Label l = new Label("Map view requires valid ArcGIS license key");

            l.setStyle("-fx-background-color: white; -fx-text-fill: " + Window.STYLE_LOW_CONTRAST_TEXT_COLOR + ";");

            final BorderPane p = new BorderPane();
            p.setStyle("-fx-background-color: white; -fx-text-alignment: center");
            p.setCenter(l);

            return p;

        }

        current.valid = true;

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

    void updateMarkers(Set<MapNode> nodes) {
        if (!valid) return;
        Platform.runLater(() -> updateGraphics(nodes));
    }

    private static final float marginPercent = 1.3f;

    private Consumer<Tuple2<Double, Double>> selectingPoint = null;
    private Graphic selectPointG = null;

    private void setViewpoint(Set<MapNode> nodes, ArcGISMap map) {
        if (!valid) return;

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
            MapNode n = nodes.iterator().next();
            map.setInitialViewpoint(new Viewpoint(new Point(n.gpsLon, n.gpsLat, reference), 30000));
        } else {
            map.setInitialViewpoint(new Viewpoint(new Point(0, 0, reference), Double.MAX_VALUE));
        }

        mapView.setOnMouseClicked(event -> {
            if (event.isStillSincePress() && event.getButton() == MouseButton.PRIMARY && selectingPoint != null) {
                final Point2D clickedPoint = new Point2D(event.getX(), event.getY());
                final Point mapPoint = (Point) GeometryEngine.normalizeCentralMeridian(mapView.screenToLocation(clickedPoint));

                final String gpsStr = CoordinateFormatter.toLatitudeLongitude(mapPoint, CoordinateFormatter.LatitudeLongitudeFormat.DECIMAL_DEGREES, 7);
                final String[] gpsStrs = gpsStr.split(" ");

                final double gpsLat = Double.parseDouble(gpsStrs[0].substring(0, gpsStrs[0].length() - 1)) * (gpsStrs[0].charAt(gpsStrs[0].length() - 1) == 'N' ? 1 : -1);
                final double gpsLon = Double.parseDouble(gpsStrs[1].substring(0, gpsStrs[1].length() - 1)) * (gpsStrs[1].charAt(gpsStrs[1].length() - 1) == 'E' ? 1 : -1);

                updatePointSelection(gpsLat, gpsLon);
                selectingPoint.accept(new Tuple2<>(gpsLat, gpsLon));
                selectingPoint = null;
            }
        });

    }

    public void selectPoint(Consumer<Tuple2<Double, Double>> onSelect) {
        if (!valid) return;

        this.selectingPoint = onSelect;

        if (selectPointG != null) {
            graphicsOverlay.getGraphics().remove(selectPointG);
            selectPointG = null;
        }
    }

    private static final PictureMarkerSymbol marker = new PictureMarkerSymbol(Icons._24.LOCATION);

    public void updatePointSelection(double gpsLat, double gpsLon) {
        if (!valid) return;

        if (selectPointG != null) {
            graphicsOverlay.getGraphics().remove(selectPointG);
        }

        selectPointG = new Graphic(new Point(gpsLon, gpsLat, reference), marker);
        graphicsOverlay.getGraphics().add(selectPointG);
    }

    private static final Image STORE_HERE_IMG = Icons._32.get("store-here");
    private static final Image WAREHOUSE_HERE_IMG = Icons._32.get("warehouse-here");
    private static final Image WAREHOUSE_STORE_HERE_IMG = Icons._32.get("warehouse-store-here");
    private static final Image ERROR_IMG = Icons._32.get("error-here");
    private static final Image NOTHING_IMG = Icons._32.get("nothing-here");

    private void updateGraphics(Set<MapNode> nodes) {
        if (!valid) return;

        final ListenableList<Graphic> g = graphicsOverlay.getGraphics();
        g.clear();
        for (MapNode n : nodes) {
            final PictureMarkerSymbol marker = new PictureMarkerSymbol(n.demand <= 0 ? n.supply > 0 ? WAREHOUSE_HERE_IMG : ERROR_IMG : n.supply > 0 ? WAREHOUSE_STORE_HERE_IMG : STORE_HERE_IMG);
            g.add(new Graphic(new Point(n.gpsLon, n.gpsLat, reference), marker));
        }
    }

}
