package plus.flow.core.functions;

import plus.flow.core.ErrorDetails;
import plus.flow.core.FlowException;

public class ExecutingException extends FlowException {

    public ExecutingException(String message) {
        super(message);
    }

    public ExecutingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ExecutingException(ErrorDetails errorDetails) {
        super(errorDetails);
    }

    public ExecutingException(ErrorDetails errorDetails, Throwable throwable) {
        super(errorDetails, throwable);
    }
}
