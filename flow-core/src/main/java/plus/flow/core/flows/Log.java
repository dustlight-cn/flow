package plus.flow.core.flows;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
public class Log implements Serializable {

    private String id;
    private String taskId;
    private String nodeName;
    private Instant createdAt;

    private String content;
}
