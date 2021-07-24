package plus.flow.core.nodes;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
public class ExecuteLog implements Serializable {

    private String info, error;
    private Instant beginAt, doneAt;
}
