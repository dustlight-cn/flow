package plus.flow.core.flows;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

@Getter
@Setter
public class Flow implements Serializable {

    private String id;

    private String clientId;

    private Collection<String> owner;

    private Collection<String> members;

    private String name;

    private Integer version;

    private String title;

    private String description;

    private Instant createdAt;

    private Collection<Node> nodes;

    private Collection<Line> lines;

    private String main;

    private Collection<String> eventSources;
}
