package meng.xing.controller.common;

public class ResponseStatus {
    private String success;
    private String message;

    public ResponseStatus() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
