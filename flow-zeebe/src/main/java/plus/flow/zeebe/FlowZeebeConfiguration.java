package plus.flow.zeebe;

import io.camunda.zeebe.client.CredentialsProvider;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import io.camunda.zeebe.client.impl.NoopCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import plus.flow.zeebe.services.*;
import plus.flow.zeebe.services.adapters.MultiTenantAdapter;
import plus.flow.zeebe.services.adapters.UserTaskFormAdapter;
import plus.flow.zeebe.services.adapters.ZeebeProcessAdapter;

import java.util.Set;

@Configuration
@EnableConfigurationProperties({ZeebeProperties.class})
public class FlowZeebeConfiguration {

    @Bean
    @ConditionalOnMissingBean(CredentialsProvider.class)
    public NoopCredentialsProvider noopCredentialsProvider() {
        return new NoopCredentialsProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public ZeebeClient zeebeClient(@Autowired ZeebeProperties properties,
                                   @Autowired CredentialsProvider provider) {
        ZeebeClientBuilder builder = ZeebeClient.newClientBuilder();
        builder.gatewayAddress(properties.getAddress())
                .credentialsProvider(provider);
        if (properties.isPlaintext())
            builder.usePlaintext();
        return builder.build();
    }

    @Bean
    public ZeebeProcessService zeebeProcessService(@Autowired ZeebeClient zeebeClient,
                                                   @Autowired ReactiveElasticsearchOperations elasticsearchOperations,
                                                   @Autowired ApplicationContext context,
                                                   @Autowired ZeebeProperties properties) {
        ZeebeProcessService service = new ZeebeProcessService(zeebeClient,
                elasticsearchOperations,
                Set.copyOf(context.getBeansOfType(ZeebeProcessAdapter.class).values()));
        service.setProcessIndex(properties.getProcessIndex());
        return service;
    }

    @Bean
    public ZeebeInstanceService zeebeInstanceService(@Autowired ZeebeClient zeebeClient,
                                                     @Autowired ReactiveElasticsearchOperations operations) {
        return new ZeebeInstanceService(zeebeClient, operations);
    }

    @Bean
    public ZeebeMessageService zeebeMessageService(@Autowired ZeebeClient zeebeClient) {
        return new ZeebeMessageService(zeebeClient);
    }


    @Bean
    public MultiTenantAdapter multiTenantAdapter(@Autowired ZeebeProperties properties) {
        return new MultiTenantAdapter(properties.getSystemPrefix());
    }

    @Bean
    public UserTaskFormAdapter userTaskFormAdapter(@Autowired ZeebeProperties properties) {
        return new UserTaskFormAdapter(properties.getUserTaskFormKeyPrefix());
    }

}
