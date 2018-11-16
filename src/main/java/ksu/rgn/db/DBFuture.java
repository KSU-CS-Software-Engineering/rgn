package ksu.rgn.db;

/**
 *
 */
public class DBFuture {

    private Runnable finished = null;

    public void finished(Runnable r) {
        this.finished = r;
    }

    void invokeFinished() {
        if (finished != null) finished.run();
    }

}
