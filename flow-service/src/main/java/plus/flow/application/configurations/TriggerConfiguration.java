package plus.flow.application.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.flow.core.flow.instance.InstanceService;
import plus.flow.core.flow.trigger.DefaultFlowTrigger;
import plus.flow.core.flow.trigger.TriggerStore;

@Configuration
public class TriggerConfiguration {

    @Bean
    public DefaultFlowTrigger defaultFlowTrigger(@Autowired TriggerStore store,
                                                 @Autowired InstanceService service) {
        return new DefaultFlowTrigger(service, store);
    }

}
