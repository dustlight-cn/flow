package plus.flow.datacenter;

import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import plus.datacenter.amqp.entities.RecodeEvent;
import plus.datacenter.amqp.sync.SyncHandler;
import plus.datacenter.core.entities.forms.Record;
import plus.flow.core.flow.trigger.FlowTrigger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@AllArgsConstructor
public class DatacenterWatcher implements SyncHandler {

    private static final Log logger = LogFactory.getLog(DatacenterWatcher.class);
    private FlowTrigger trigger;

    @Override
    public Mono<Void> sync(RecodeEvent recodeEvent) {
        if (recodeEvent == null || recodeEvent.getRecords() == null || recodeEvent.getRecords().size() == 0)
            return Mono.empty();
        Collection<Record> records = recodeEvent.getRecords();
        String operation = recodeEvent.getType() == null ? null : recodeEvent.getType().toString();

        return Flux.fromIterable(records)
                .flatMap(record -> trigger.onEvent(record.getClientId(), record.getFormName(), operation, record.getData()))
                .flatMap(instance -> Mono.fromRunnable(() -> logger.info(String.format("Datacenter Event: (#%s) %d", operation, instance.getId()))))
                .then();
    }

}
