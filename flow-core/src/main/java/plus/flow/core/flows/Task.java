package plus.flow.core.flows;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

@Getter
@Setter
public class Task implements Serializable {

    private String id;
    private Collection<String> owner;
    private Collection<String> members;

    private String flowId;
    private String flowName;
    private Integer flowVersion;

    private Instant createdAt;

    private String current;
}
