package ksu.rgn.utils;

import java.util.function.Consumer;

/**
 *
 */
public class Future {

    private Consumer<Object> success, fail, finish = null;

    private boolean isFinished = false;
    private boolean isSuccess = false;
    private boolean isFail = false;
    private Object successPayload, failPayload;

    public Future onSuccess(Consumer<Object> cb) {
        success = cb;
        finish = null;
        if (isFinished && isSuccess) {
            if (success != null) success.accept(successPayload);
        }
        return this;
    }
    public Future onFail(Consumer<Object> cb) {
        fail = cb;
        finish = null;
        if (isFinished && isFail) {
            if (fail != null) fail.accept(failPayload);
        }
        return this;
    }
    public Future onFinish(Consumer<Object> cb) {
        finish = cb;
        success = null;
        fail = null;
        if (isFinished) {
            if (finish != null) finish.accept(failPayload != null ? failPayload : successPayload);
        }
        return this;
    }

    public void invokeSuccess(Object data) {
        if (isFinished) return;
        isFinished = true;
        isSuccess = true;
        successPayload = data;
        if (success != null) success.accept(data);
        if (finish != null) finish.accept(data);
    }

    public void invokeFail(Object data) {
        if (isFinished) return;
        isFinished = true;
        isFail = true;
        failPayload = data;
        if (fail != null) fail.accept(data);
        if (finish != null) finish.accept(data);
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
