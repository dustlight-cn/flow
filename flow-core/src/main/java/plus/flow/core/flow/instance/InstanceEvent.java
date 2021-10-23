package plus.flow.core.flow.instance;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.Instant;

public interface InstanceEvent {

    @JsonSerialize(using = ToStringSerializer.class)
    Long getId();

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
        INCIDENT,
        RESOLVED
    }

}
