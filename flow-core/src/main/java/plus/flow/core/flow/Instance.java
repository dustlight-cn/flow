package plus.flow.core.flow;

import java.util.List;

public interface Instance<T> extends InstanceEvent {

    String getName();

    Integer getVersion();

    String getClientId();

    List<T> getEvents();

}
