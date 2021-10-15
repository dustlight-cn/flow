package plus.flow.core.flow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public interface Instance {

    Integer getVersion();

    @JsonSerialize(using = ToStringSerializer.class)
    Long getId();

    String getName();
}
