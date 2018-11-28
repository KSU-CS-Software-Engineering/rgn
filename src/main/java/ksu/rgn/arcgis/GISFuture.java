package ksu.rgn.arcgis;

import java.util.function.Consumer;

/**
 *
 */
public class GISFuture {

    private Consumer<Object> success, fail = null;

    private boolean isFinished = false;
    private boolean isSuccess = false;
    private boolean isFail = false;
    private Object successPayload, failPayload;

    public void success(Consumer<Object> cb) {
        this.success = cb;
    }
    public void fail(Consumer<Object> cb) {
        this.fail = cb;
    }

    void invokeSuccess(Object data) {
        isFinished = true;
        isSuccess = true;
        successPayload = data;
        if (success != null) success.accept(data);
    }

    void invokeFail(Object data) {
        isFinished = true;
        isFail = true;
        failPayload = data;
        if (fail != null) fail.accept(data);
    }

    public boolean isFinished() {
        return isFinished;
    }
    public boolean isSuccess() {
        return isSuccess;
    }
    public boolean isFail() {
        return isFail;
    }
    public Object getSuccessPayload() {
        return isSuccess ? successPayload : null;
    }
    public Object getFailPayload() {
        return isFail ? failPayload : null;
    }

}
