//package plus.flow.rabbitmq.flows;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import plus.flow.core.flows.Task;
//import plus.flow.core.flows.TaskService;
//import plus.flow.core.functions.Function;
//import plus.flow.core.functions.FunctionService;
//import plus.flow.core.nodes.Result;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//
//@Getter
//@Setter
//@AllArgsConstructor
//public class TaskListener implements ApplicationRunner {
//
//    private static final Log logger = LogFactory.getLog(TaskListener.class);
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    private FunctionService functionService;
//    private TaskService taskService;
//    private ConnectionFactory factory;
//    private String exchange;
//    private String routingKey;
//    private String queue;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        RabbitAdmin admin = new RabbitAdmin(factory);
//        Exchange ex = new TopicExchange(exchange, true, false);
//        admin.declareExchange(ex);
//        Queue qu = new Queue(queue, true, false, false, null);
//        admin.declareQueue(qu);
//        Binding binding = new Binding(queue, Binding.DestinationType.QUEUE, exchange, routingKey, null);
//        admin.declareBinding(binding);
//
//        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
//        simpleMessageListenerContainer.setQueueNames(queue);
//        simpleMessageListenerContainer.setConnectionFactory(factory);
//        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//
//        simpleMessageListenerContainer.setupMessageListener((ChannelAwareMessageListener) (message, channel) -> {
//            try {
//                Tasks tasks = mapper.readValue(message.getBody(), Tasks.class);
//                Collection<String> functionIds = new HashSet<>();
//                for (Task task : tasks) {
//                    String functionId = task.getCurrentFunction();
//                    functionIds.add(functionId);
//                }
//                Map<String, Function> functionMap = functionService.getFunctions(functionIds).collectList()
//                        .map(functions -> {
//                            Map<String, Function> map = new HashMap<>();
//                            for (Function function : functions)
//                                map.put(function.getId(), function);
//                            return map;
//                        }).block();
//                for (Task task : tasks) {
//                    String functionId = task.getCurrentFunction();
//                    Function function = functionMap.get(functionId);
//                    Result output = function.execute(task.getInput(), task.getContext()).block();
//                    task.setOutput(output.getOutput());
//                }
//                taskService.saveTasks(tasks);
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            } catch (Throwable e) {
//                logger.error("Fail to handle task, cause: " + e.getMessage(), e);
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            }
//        });
//        simpleMessageListenerContainer.start();
//    }
//
//    private static class Tasks extends HashSet<Task> {
//    }
//}
