package plus.flow.mongo;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import plus.flow.mongo.services.MongoPipelineService;

@EnableConfigurationProperties(FlowMongoProperties.class)
public class FlowMongoConfiguration {

    @Bean
    @ConditionalOnBean(ReactiveMongoOperations.class)
    public MongoPipelineService mongoFlowService(@Autowired ReactiveMongoOperations operations,
                                                 @Autowired MongoClient mongoClient,
                                                 @Autowired FlowMongoProperties properties) {
        return new MongoPipelineService(mongoClient, operations, properties.getPipelineCollection(), properties.getPipelineInstanceCollection());
    }
}
