package plus.flow.datacenter;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import plus.flow.core.flows.FlowsService;
import plus.flow.core.flows.TaskService;
import plus.flow.datacenter.events.*;

@EnableConfigurationProperties(FlowDatacenterProperties.class)
public class FlowDatacenterConfiguration {

    @Bean
    public FromEventListener fromEventListener(@Autowired ConnectionFactory connectionFactory,
                                               @Autowired FormRecordCreatedEventSource createdEventSource,
                                               @Autowired FormRecordUpdatedEventSource updatedEventSource,
                                               @Autowired FormRecordDeletedEventSource deletedEventSource,
                                               @Autowired FlowDatacenterProperties properties) {
        return new FromEventListener(connectionFactory,
                createdEventSource,
                updatedEventSource,
                deletedEventSource,
                properties.getExchange());
    }

    @Bean
    @ConditionalOnBean(FlowsService.class)
    public FormRecordCreatedEventSource formRecordCreatedEventSource(@Autowired FlowsService flowsService,
                                                                     @Autowired TaskService taskService) {
        return new FormRecordCreatedEventSource(flowsService, taskService);
    }

    @Bean
    @ConditionalOnBean(FlowsService.class)
    public FormRecordUpdatedEventSource formRecordUpdatedEventSource(@Autowired FlowsService flowsService,
                                                                     @Autowired TaskService taskService) {
        return new FormRecordUpdatedEventSource(flowsService, taskService);
    }

    @Bean
    @ConditionalOnBean(FlowsService.class)
    public FormRecordDeletedEventSource formRecordDeletedEventSource(@Autowired FlowsService flowsService,
                                                                     @Autowired TaskService taskService) {
        return new FormRecordDeletedEventSource(flowsService, taskService);
    }
}
