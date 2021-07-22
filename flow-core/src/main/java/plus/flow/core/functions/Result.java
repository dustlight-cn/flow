package plus.flow.core.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Result implements Serializable {

    private boolean success;
    private ExecutingException exception;
    private Map<String, Object> output;
}
