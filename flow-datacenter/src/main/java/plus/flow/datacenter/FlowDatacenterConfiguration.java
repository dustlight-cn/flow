package plus.flow.datacenter;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import plus.flow.core.events.BasicEventSource;
import plus.flow.datacenter.events.*;

@EnableConfigurationProperties(FlowDatacenterProperties.class)
public class FlowDatacenterConfiguration {

    @Bean
    public FromEventListener fromEventListener(@Autowired ConnectionFactory connectionFactory,
                                               @Autowired BasicEventSource createdRecordEvent,
                                               @Autowired BasicEventSource updateRecordEvent,
                                               @Autowired BasicEventSource deleteRecordEvent,
                                               @Autowired FlowDatacenterProperties properties) {
        return new FromEventListener(connectionFactory,
                createdRecordEvent,
                updateRecordEvent,
                deleteRecordEvent,
                properties.getExchange());
    }

    @Bean
    public BasicEventSource createdRecordEvent() {
        BasicEventSource eventSource = new BasicEventSource();
        eventSource.setEventType("CREATE_RECORD");
        eventSource.setTitle("提交表单");
        eventSource.setDescription("当表单提交时触发。");
        return eventSource;
    }

    @Bean
    public BasicEventSource updateRecordEvent() {
        BasicEventSource eventSource = new BasicEventSource();
        eventSource.setEventType("UPDATE_RECORD");
        eventSource.setTitle("更新表单记录");
        eventSource.setDescription("当表单记录更新时触发。");
        return eventSource;
    }

    @Bean
    public BasicEventSource deleteRecordEvent() {
        BasicEventSource eventSource = new BasicEventSource();
        eventSource.setEventType("DELETE_RECORD");
        eventSource.setTitle("删除表单记录");
        eventSource.setDescription("当表单记录删除时触发。");
        return eventSource;
    }
}
