package plus.flow.core.nodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {

    private boolean success;
    private ExecutingException exception;
    private Map<String, Object> output;
    private ExecuteLog log;

}
