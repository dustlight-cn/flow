package plus.flow.core.flow.instance;

import java.util.List;

public interface Instance<T> extends InstanceEvent {

    String getName();

    Integer getVersion();

    String getClientId();

    List<T> getEvents();

}
