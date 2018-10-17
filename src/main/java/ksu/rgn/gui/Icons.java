package ksu.rgn.gui;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

/**
 *
 */
public class Icons {

    public static final Icons _24 = new Icons(24);
    public static final Icons _32 = new Icons(32);


    public final Image CONNECTED;
    public final Image DISCONNECTED;
    public final Image LIST;
    public final Image ADD_NEW;
    public final Image MAP;
    public final Image WAYPOINT_MAP;
    public final Image CROSS;
    public final Image PROGRESS_INDICATOR;
    public final Image OPEN_FOLDER;
    public final Image GROCERY_STORE;
    public final Image TRUCK;
    public final Image ROUTE;



    private final String basePath;
    private final HashMap<String, Image> images = new HashMap<>();

    private Icons(int size) {
        this.basePath = "/icons" + Integer.toString(size);

        CONNECTED = get("connected");
        DISCONNECTED = get("disconnected");
        LIST = get("list");
        ADD_NEW = get("add-new");
        MAP = get("map");
        WAYPOINT_MAP = get("waypoint-map");
        CROSS = get("cross");
        PROGRESS_INDICATOR = get("progress-indicator");
        OPEN_FOLDER = get("open-folder");
        GROCERY_STORE = get("grocery-store");
        TRUCK = get("truck");
        ROUTE = get("route");
    }

    public Image get(String name) {
        Image i = images.get(name);
        if (i != null) return i;
        InputStream is = getClass().getResourceAsStream(this.basePath + "/" + name + ".png");
        if (is != null) {
            i = new Image(is);
            images.put(name, i);
            return i;
        } else {
            return null;
        }
    }

}
