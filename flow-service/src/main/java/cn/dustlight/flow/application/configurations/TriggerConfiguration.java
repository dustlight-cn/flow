package cn.dustlight.flow.application.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.dustlight.datacenter.core.services.RecordEventHandler;
import cn.dustlight.flow.core.flow.instance.InstanceService;
import cn.dustlight.flow.core.flow.trigger.DefaultFlowTrigger;
import cn.dustlight.flow.core.flow.trigger.TriggerStore;

import java.util.Set;

@Configuration
public class TriggerConfiguration {

    @Bean
    public DefaultFlowTrigger defaultFlowTrigger(@Autowired TriggerStore store,
                                                 @Autowired InstanceService service) {
        return new DefaultFlowTrigger(service, store, Set.of(
                RecordEventHandler.EventType.CREATE.toString(),
                RecordEventHandler.EventType.DELETE.toString(),
                RecordEventHandler.EventType.UPDATE.toString()));
    }

}
