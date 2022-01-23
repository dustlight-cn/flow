package cn.dustlight.flow.datacenter;

import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cn.dustlight.datacenter.amqp.entities.RecodeEvent;
import cn.dustlight.datacenter.amqp.sync.SyncHandler;
import cn.dustlight.datacenter.core.entities.forms.Record;
import cn.dustlight.flow.core.flow.trigger.FlowTrigger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
                .flatMap(record -> trigger.onEvent(record.getClientId(), record.getFormName(), operation, record2Map(record)))
                .flatMap(instance -> Mono.fromRunnable(() -> logger.info(String.format("Datacenter Event: (#%s) %d", operation, instance.getId()))))
                .then();
    }

    protected Map<String, Object> record2Map(Record record) {
        if (record == null)
            return Collections.emptyMap();
        Map<String, Object> result = new HashMap<>();
        result.put("owner", record.getOwner());
        result.put("clientId", record.getClientId());
        result.put("data", record.getData());
        result.put("formName", record.getFormName());
        result.put("formId", record.getFormId());
        result.put("formVersion", record.getFormVersion());
        result.put("id", record.getId());
        result.put("createdAt", record.getCreatedAt());
        result.put("updatedAt", record.getCreatedAt());
        return result;
    }
}
