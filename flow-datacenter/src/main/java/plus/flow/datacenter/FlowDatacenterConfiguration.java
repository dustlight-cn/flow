package plus.flow.datacenter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowDatacenterConfiguration {

    @Bean
    public DatacenterWatcher datacenterWatcher() {
        return new DatacenterWatcher();
    }
}
