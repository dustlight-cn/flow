package plus.flow.triggers.sotre.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;

@Configuration
@EnableConfigurationProperties({ElasticsearchTriggerStoreProperties.class})
public class ElasticsearchTriggerStoreConfiguration {

    @Bean
    @ConditionalOnBean(ReactiveElasticsearchOperations.class)
    public ElasticsearchTriggerStore elasticsearchTriggerStore(@Autowired ReactiveElasticsearchOperations operations,
                                                               @Autowired ElasticsearchTriggerStoreProperties properties) {
        return new ElasticsearchTriggerStore(operations, properties.getIndex());
    }

}
