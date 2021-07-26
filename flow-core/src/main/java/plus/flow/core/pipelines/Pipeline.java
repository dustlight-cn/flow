package plus.flow.core.pipelines;

import lombok.Getter;
import lombok.Setter;
import plus.flow.core.AccessControlList;
import plus.flow.core.events.Event;
import plus.flow.core.nodes.Node;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class Pipeline implements Serializable {

    private String id;
    private String clientId;

    private Collection<String> owner;
    private Collection<String> members;

    private String title;
    private String description;

    private Stage[] stages;

    private Instant createdAt;
    private Instant updatedAt;

    private Collection<Event> trigger;

    private Node[] onSuccess;
    private Node[] onFailed;

    private Map<String, String> env;

    private AccessControlList acl;
}
