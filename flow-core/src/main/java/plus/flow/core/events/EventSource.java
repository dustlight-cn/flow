package plus.flow.core.events;

public interface EventSource {

    String getEventType();

    String getTitle();

    String getDescription();

    void register(EventListener eventHandler);

}
