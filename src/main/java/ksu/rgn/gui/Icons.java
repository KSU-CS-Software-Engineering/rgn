package ksu.rgn.gui;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;

/**
 *
 */
public class Icons {

    private static final String STYLE = "office";

    private static final Logger LOG = LoggerFactory.getLogger(Icons.class);

    public static final Icons _16 = new Icons(16);
    public static final Icons _24 = new Icons(24);
    public static final Icons _32 = new Icons(32);


    public final Image CONNECTED;
    public final Image DISCONNECTED;
    public final Image LIST;
    public final Image ADD_NEW;
    public final Image MAP;
    public final Image WAYPOINT_MAP;
    public final Image CROSS;
    public final Image CHECK;
    public final Image ERROR;
    public final Image PROGRESS_INDICATOR;
    public final Image OPEN_FOLDER;
    public final Image GROCERY_STORE;
    public final Image TRUCK;
    public final Image ROUTE;
    public final Image GO_RIGHT;
    public final Image GO_LEFT;
    public final Image LOCATION;
    public final Image TRASH;



    private final String basePath;
    private final int size;
    private final HashMap<String, Image> images = new HashMap<>();

    private Icons(int size) {
        this.size = size;
        this.basePath = "/icons/" + STYLE + "/_" + size;

        CONNECTED = get("connected");
        DISCONNECTED = get("disconnected");
        LIST = get("list");
        ADD_NEW = get("add-new");
        MAP = get("map");
        WAYPOINT_MAP = get("waypoint-map");
        CROSS = get("cross");
        CHECK = get("check");
        ERROR = get("error");
        PROGRESS_INDICATOR = get("progress-indicator");
        OPEN_FOLDER = get("open-folder");
        GROCERY_STORE = get("grocery-store");
        TRUCK = get("truck");
        ROUTE = get("route");
        GO_RIGHT = get("go-right");
        GO_LEFT = get("go-left");
        LOCATION = get("location");
        TRASH = get("trash");
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
            LOG.warn("Icon '{}' does not exist in {}px size", name, size);
            return null;
        }
    }

}
