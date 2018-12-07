package ksu.rgn.arcgis;


/**
 *
 */
public abstract class GISJob {

    GISFuture future = null;

    public abstract void run(GISBridge bridge);

    @Override
    public abstract String toString();

}
