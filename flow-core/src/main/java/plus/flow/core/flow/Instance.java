package plus.flow.core.flow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.Instant;

public interface Instance {

    Integer getVersion();

    @JsonSerialize(using = ToStringSerializer.class)
    Long getId();

    String getName();

    String getClientId();

    Status getStatus();

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
