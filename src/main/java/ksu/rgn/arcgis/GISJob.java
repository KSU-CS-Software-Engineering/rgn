package ksu.rgn.arcgis;


/**
 *
 */
public abstract class GISJob {

    GISFuture future = null;

    public abstract void run();

    @Override
    public abstract String toString();

}
