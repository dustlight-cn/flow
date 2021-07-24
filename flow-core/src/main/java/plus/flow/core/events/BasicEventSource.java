package plus.flow.core.events;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

public class BasicEventSource implements EventSource {

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String eventType;

    private Collection<EventListener> listeners;

    @Override
    public synchronized void register(EventListener listener) {
        if (listeners == null)
            listeners = new HashSet<>();
        listeners.add(listener);
    }

    public synchronized void remove(EventListener listener) {
        if (listener == null)
            return;
        listeners.remove(listener);
    }

    public void publishEvent(String key, Object data) {
        if (listeners == null)
            return;
        listeners.forEach(listener -> listener.onEvent(new Event(eventType, key, data)));
    }
}
