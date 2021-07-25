package plus.flow.core.exceptions;

public enum ErrorEnum {

    NO_ERRORS(-1, "No errors"),
    UNKNOWN(0, "Unknown error"),
    UNAUTHORIZED(1, "Unauthorized"),
    ACCESS_DENIED(2, "Access denied"),


    INSTANCE_NOT_BLOCKING(10, "Instance is not blocking."),

    INPUT_INVALID(1000, "Input invalid"),

    RESOURCE_NOT_FOUND(2000, "Resource not found"),
    PIPELINE_NOT_FOUND(2001, "Pipeline not found"),
    INSTANCE_NOT_FOUND(2002, "Instance not found"),

    RESOURCE_EXISTS(3000, "Resource already exists"),
    PIPELINE_EXISTS(3001, "Pipeline already exists"),
    INSTANCE_EXISTS(3002, "Instance already exists"),

    CREATE_RESOURCE_FAILED(4000, "Fail to create resource"),
    CREATE_PIPELINE_FAILED(4001, "Fail to create pipeline"),
    CREATE_INSTANCE_FAILED(4002, "Fail to create instance"),

    UPDATE_RESOURCE_FAILED(5000, "Fail to update resource"),
    UPDATE_PIPELINE_FAILED(5001, "Fail to update pipeline"),
    UPDATE_INSTANCE_FAILED(5002, "Fail to update instance"),

    DELETE_RESOURCE_FAILED(6000, "Fail to delete resource"),
    DELETE_PIPELINE_FAILED(6001, "Fail to delete pipeline"),
    DELETE_INSTANCE_FAILED(6002, "Fail to delete instance");

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
}
