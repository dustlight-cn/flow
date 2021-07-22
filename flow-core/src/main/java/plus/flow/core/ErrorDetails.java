package plus.flow.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ErrorDetails {

    private String message;
    private int code;
    private String details;
    @JsonIgnore
    private transient FlowException exception;

    public ErrorDetails(int code, String message) {
        this.code = code;
        this.message = message;
        exception = new FlowException(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ErrorDetails message(String message) {
        this.message = message;
        return this;
    }

    public ErrorDetails code(int code) {
        this.code = code;
        return this;
    }

    public ErrorDetails details(String details) {
        this.details = details;
        return this;
    }

    public void setException(FlowException storageException) {
        this.exception = exception;
    }

    public FlowException getException() {
        return exception;
    }

    public void throwException() throws FlowException {
        throw exception;
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", details='" + details + '\'' +
                '}';
    }
}
