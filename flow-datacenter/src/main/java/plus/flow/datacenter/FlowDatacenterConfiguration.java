package plus.flow.datacenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.flow.core.flow.trigger.FlowTrigger;
import plus.flow.core.security.AccessTokenHolder;

@Configuration
@EnableConfigurationProperties({FlowDatacenterProperties.class})
public class FlowDatacenterConfiguration {

    @Bean
    @ConditionalOnBean({FlowTrigger.class})
    public DatacenterWatcher datacenterWatcher(@Autowired FlowTrigger flowTrigger) {
        return new DatacenterWatcher(flowTrigger);
    }

    @Bean
    @ConditionalOnBean(AccessTokenHolder.class)
    public DatacenterRecordValidator datacenterRecordValidator(@Autowired AccessTokenHolder accessTokenHolder,
                                                               @Autowired FlowDatacenterProperties properties,
                                                               @Autowired ObjectMapper mapper) {
        return new DatacenterRecordValidator(properties.getEndpoint(), accessTokenHolder, mapper);
    }

}
