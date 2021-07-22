package plus.flow.core.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class DefaultEventService implements EventService, InitializingBean {

    private ApplicationContext context;

    @Override
    public Mono<Map<String, EventSource>> getSources() {
        Map<String, EventSource> sourceBeans = context.getBeansOfType(EventSource.class);
        if (sourceBeans == null)
            return Mono.empty();
        Collection<EventSource> sources = sourceBeans.values();
        Map<String, EventSource> sourceMap = new HashMap<>();
        for (EventSource source : sources)
            sourceMap.put(source.getName(), source);
        return Mono.just(sourceMap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(context, "context can not be null!");
    }
}
