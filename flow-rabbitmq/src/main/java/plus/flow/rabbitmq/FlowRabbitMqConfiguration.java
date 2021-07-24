//package plus.flow.rabbitmq;
//
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import plus.flow.core.flows.TaskService;
//import plus.flow.core.functions.FunctionService;
//import plus.flow.rabbitmq.flows.RabbitTaskStarter;
//import plus.flow.rabbitmq.flows.TaskListener;
//
//@EnableConfigurationProperties(FlowRabbitMqProperties.class)
//public class FlowRabbitMqConfiguration {
//
//    @Bean
//    @ConditionalOnBean(ConnectionFactory.class)
//    public TaskListener taskListener(@Autowired ConnectionFactory factory,
//                                     @Autowired FunctionService functionService,
//                                     @Autowired TaskService taskService,
//                                     @Autowired FlowRabbitMqProperties properties) {
//        return new TaskListener(functionService, taskService, factory,
//                properties.getExchange(), properties.getRoutingKey(), properties.getQueue());
//    }
//
//    @Bean
//    @ConditionalOnBean(RabbitTemplate.class)
//    public RabbitTaskStarter rabbitTaskStarter(@Autowired RabbitTemplate template,
//                                               @Autowired FlowRabbitMqProperties properties) {
//        return new RabbitTaskStarter(template, properties.getExchange(), properties.getRoutingKey());
//    }
//
//}
