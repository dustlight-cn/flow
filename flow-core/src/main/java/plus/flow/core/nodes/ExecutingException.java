package plus.flow.core.nodes;

import plus.flow.core.exceptions.ErrorDetails;
import plus.flow.core.exceptions.FlowException;

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
