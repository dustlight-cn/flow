package plus.flow.core.pipelines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import plus.flow.core.events.EventSource;

import java.util.Collection;

public class PipelineConfiguration {

    @Bean
//    @ConditionalOnBean(value = {ApplicationContext.class, PipelineService.class})
    public PipelineExecutor pipelineExecutor(@Autowired ApplicationContext applicationContext,
                                             @Autowired PipelineService pipelineService) {
        Collection<EventSource> sources = applicationContext.getBeansOfType(EventSource.class).values();
        PipelineExecutor executor = new PipelineExecutor(sources, pipelineService);
        sources.forEach(eventSource -> eventSource.register(executor));
        return executor;
    }
}
