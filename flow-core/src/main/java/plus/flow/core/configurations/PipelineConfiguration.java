package plus.flow.core.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import plus.flow.core.events.EventSource;
import plus.flow.core.pipelines.PipelineExecutor;
import plus.flow.core.pipelines.PipelineService;

import java.util.Collection;

public class PipelineConfiguration {

    @Bean
    public PipelineExecutor pipelineExecutor(@Autowired ApplicationContext applicationContext,
                                             @Autowired PipelineService pipelineService) {
        Collection<EventSource> sources = applicationContext.getBeansOfType(EventSource.class).values();
        PipelineExecutor executor = new PipelineExecutor(sources, pipelineService, applicationContext);
        sources.forEach(eventSource -> eventSource.register(executor));
        return executor;
    }
}
