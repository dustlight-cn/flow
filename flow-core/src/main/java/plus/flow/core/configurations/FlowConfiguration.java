package plus.flow.core.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import plus.flow.core.nodes.impls.ServerlessNode;

@EnableConfigurationProperties(value = {ServerlessProperties.class})
public class FlowConfiguration {

    @Bean
    public ServerlessNode.FunctionService functionService(@Autowired DiscoveryClient discoveryClient) {
        return new ServerlessNode.DefaultFunctionService(discoveryClient);
    }
}
