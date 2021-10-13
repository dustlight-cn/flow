package plus.flow.core.exceptions;

public class FlowException extends RuntimeException {

    private ErrorDetails errorDetails;

    public FlowException() {
        super();
        this.errorDetails = new ErrorDetails(0, null);
    }

    public FlowException(String message) {
        super(message);
        this.errorDetails = new ErrorDetails(0, message);
    }

    public FlowException(String message, Throwable throwable) {
        super(message, throwable);
        this.errorDetails = new ErrorDetails(0, message);
    }

    public FlowException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }

    public FlowException(ErrorDetails errorDetails, Throwable throwable) {
        super(errorDetails.getMessage(), throwable);
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }
}
