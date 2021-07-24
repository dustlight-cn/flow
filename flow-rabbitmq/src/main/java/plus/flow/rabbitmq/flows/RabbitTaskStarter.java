//package plus.flow.rabbitmq.flows;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import plus.flow.core.exceptions.FlowException;
//import plus.flow.core.flows.Task;
//import plus.flow.core.flows.TaskStarter;
//import reactor.core.publisher.Mono;
//
//import java.util.Collection;
//
//@Getter
//@Setter
//@AllArgsConstructor
//public class RabbitTaskStarter implements TaskStarter {
//
//    private final static ObjectMapper mapper = new ObjectMapper();
//    private RabbitTemplate template;
//    private String exchange;
//    private String routingKey;
//
//    @Override
//    public Mono<Void> startTasks(Collection<Task> tasks) {
//        return Mono.fromRunnable(() -> {
//            try {
//                template.convertAndSend(exchange, routingKey, mapper.writeValueAsString(tasks));
//            } catch (JsonProcessingException e) {
//                throw new FlowException("Fail to start tasks, cause: " + e.getMessage(), e);
//            }
//        });
//    }
//
//}
