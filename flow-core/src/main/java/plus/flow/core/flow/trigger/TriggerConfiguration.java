package plus.flow.core.flow.trigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.flow.core.flow.instance.InstanceService;

@Configuration
public class TriggerConfiguration {

    @Bean
    @ConditionalOnBean({TriggerStore.class, InstanceService.class})
    public DefaultFlowTrigger defaultFlowTrigger(@Autowired TriggerStore store,
                                                 @Autowired InstanceService service) {
        return new DefaultFlowTrigger(service, store);
    }

}
