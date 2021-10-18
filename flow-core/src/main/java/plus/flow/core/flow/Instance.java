package plus.flow.core.flow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

public interface Instance<T> extends InstanceEvent {


    @JsonSerialize(using = ToStringSerializer.class)
    Long getId();

    String getName();

    Integer getVersion();

    String getClientId();

    List<T> getEvents();

}
