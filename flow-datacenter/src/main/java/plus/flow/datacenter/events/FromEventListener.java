package plus.flow.datacenter.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import plus.flow.core.events.BasicEventSource;

@Getter
@Setter
@AllArgsConstructor
public class FromEventListener implements ApplicationRunner {

    private static final Log logger = LogFactory.getLog(FromEventListener.class);

    private ConnectionFactory factory;

    private BasicEventSource formRecordCreatedEventSource;
    private BasicEventSource formRecordUpdatedEventSource;
    private BasicEventSource formRecordDeletedEventSource;

    private String exchange;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RabbitAdmin admin = new RabbitAdmin(factory);

        Queue created = new Queue("flow-datacenter-created", true, false, false);
        Queue updated = new Queue("flow-datacenter-updated", true, false, false);
        Queue deleted = new Queue("flow-datacenter-deleted", true, false, false);
        admin.declareQueue(created);
        admin.declareQueue(updated);
        admin.declareQueue(deleted);

        Binding createdBinding = new Binding(created.getName(), Binding.DestinationType.QUEUE, exchange, "CREATED.*.*", null);
        Binding updatedBinding = new Binding(updated.getName(), Binding.DestinationType.QUEUE, exchange, "UPDATED.*.*", null);
        Binding deletedBinding = new Binding(deleted.getName(), Binding.DestinationType.QUEUE, exchange, "DELETED.*.*", null);
        admin.declareBinding(createdBinding);
        admin.declareBinding(updatedBinding);
        admin.declareBinding(deletedBinding);

        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setQueueNames(created.getActualName(), updated.getActualName(), deleted.getActualName());
        simpleMessageListenerContainer.setConnectionFactory(factory);
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        simpleMessageListenerContainer.setupMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            try {
                Exception e = null;
                RecordMessage recordMessage = RecordMessage.from(message.getBody());
                switch (recordMessage.getType()) {
                    case CREATED:
                        formRecordCreatedEventSource.publishEvent(recordMessage.getFormName(), recordMessage);
                        break;
                    case UPDATED:
                        formRecordUpdatedEventSource.publishEvent(recordMessage.getFormName(), recordMessage);
                        break;
                    case DELETED:
                        formRecordDeletedEventSource.publishEvent(recordMessage.getFormName(), recordMessage);
                        break;
                    default:
                        e = new Exception("Form event type not found!");
                }
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                if (e != null)
                    throw e;
            } catch (Throwable e) {
                logger.error("Fail to handle event (FormEvent), cause: " + e.getMessage(), e);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        });
        simpleMessageListenerContainer.start();
    }

}
