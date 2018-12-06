package ksu.rgn.db;

/**
 *
 */
public class DBFuture {

    private Runnable finished = null;
    private boolean aheadOfTimeFinish = false;

    public void finished(Runnable r) {
        this.finished = r;
        if (aheadOfTimeFinish) {
            if (r != null) r.run();
            aheadOfTimeFinish = false;
        }
    }

    void invokeFinished() {
        if (finished != null) {
            finished.run();
        } else {
            aheadOfTimeFinish = true;
        }
    }

}
