package meng.xing.controller.common;

public class ResponseStatusWithMessage {
    private String success;
    private String message;

    public ResponseStatusWithMessage() {
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
