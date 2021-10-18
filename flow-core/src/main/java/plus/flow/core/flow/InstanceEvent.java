package plus.flow.core.flow;

import java.time.Instant;

public interface InstanceEvent {

    String getElementType();

    String getElementId();

    Instance.Status getStatus();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    InstanceError getError();

    enum Status {
        ACTIVE,
        CANCELED,
        COMPLETED,
        INCIDENT
    }

}
