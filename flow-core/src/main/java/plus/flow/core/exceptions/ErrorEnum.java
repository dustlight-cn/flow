package plus.flow.core.exceptions;

public enum ErrorEnum {

    NO_ERRORS(-1, "No errors"),
    UNKNOWN(0, "Unknown error"),
    UNAUTHORIZED(1, "Unauthorized"),
    ACCESS_DENIED(2, "Access denied"),

    INPUT_INVALID(1000, "Input invalid"),
    USER_TASK_DATA_INVALID(1001, "User task data invalid"),

    RESOURCE_NOT_FOUND(2000, "Resource not found"),
    PROCESS_NOT_FOUND(2001, "Process not found"),
    INSTANCE_NOT_FOUND(2002, "Instance not found"),
    USER_TASK_NOT_FOUND(2003, "User task not found"),
    TRIGGER_OPERATION_NOT_FOUND(2004, "Trigger operation not found"),

    RESOURCE_EXISTS(3000, "Resource already exists"),
    PROCESS_EXISTS(3001, "Process already exists"),
    INSTANCE_EXISTS(3002, "Instance already exists"),
    USER_TASK_EXISTS(3003, "User task already exists"),

    CREATE_RESOURCE_FAILED(4000, "Fail to create resource"),
    CREATE_PROCESS_FAILED(4001, "Fail to create process"),
    CREATE_INSTANCE_FAILED(4002, "Fail to create instance"),
    CREATE_USER_TASK_FAILED(4003, "Fail to create user task"),

    UPDATE_RESOURCE_FAILED(5000, "Fail to update resource"),
    UPDATE_PROCESS_FAILED(5001, "Fail to update process"),
    UPDATE_INSTANCE_FAILED(5002, "Fail to update instance"),
    UPDATE_USER_TASK_FAILED(5003, "Fail to update user task"),

    DELETE_RESOURCE_FAILED(6000, "Fail to delete resource"),
    DELETE_PROCESS_FAILED(6001, "Fail to delete process"),
    DELETE_INSTANCE_FAILED(6002, "Fail to delete instance"),
    DELETE_USER_TASK_FAILED(6003, "Fail to delete user task");

    private ErrorDetails details;

    ErrorEnum(int code, String message) {
        this.details = new ErrorDetails(code, message);
    }

    public void throwException() {
        this.details.throwException();
    }

    public ErrorDetails getDetails() {
        return details;
    }

    public int getCode() {
        return this.details.getCode();
    }

    public String getMessage() {
        return this.details.getMessage();
    }

    public String getErrorDetails() {
        return this.details.getDetails();
    }

    public Exception getException() {
        return this.details.getException();
    }

    public ErrorDetails message(String message) {
        return new ErrorDetails(this.details.getCode(), message != null ? message : this.details.getMessage());
    }

    public ErrorDetails details(String details) {
        ErrorDetails instance = new ErrorDetails(this.details.getCode(), this.details.getMessage());
        instance.setDetails(details != null ? details : this.details.getDetails());
        return instance;
    }

    public ErrorDetails details(Throwable throwable) {
        ErrorDetails instance = new ErrorDetails(this.details.getCode(), this.details.getMessage(), throwable);
        return instance;
    }
}
