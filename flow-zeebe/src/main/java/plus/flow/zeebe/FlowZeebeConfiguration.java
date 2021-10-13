package plus.flow.zeebe;

import io.camunda.zeebe.client.CredentialsProvider;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import io.camunda.zeebe.client.impl.NoopCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.flow.zeebe.services.ZeebeProcessService;

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
    public ZeebeProcessService zeebeProcessService(@Autowired ZeebeClient zeebeClient) {
        return new ZeebeProcessService(zeebeClient);
    }

}
