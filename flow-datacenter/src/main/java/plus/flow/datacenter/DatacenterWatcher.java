package plus.flow.datacenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import plus.datacenter.amqp.entities.RecodeEvent;
import plus.datacenter.amqp.sync.SyncHandler;
import reactor.core.publisher.Mono;

public class DatacenterWatcher implements SyncHandler {

    private static final Log logger = LogFactory.getLog(DatacenterWatcher.class);

    @Override
    public Mono<Void> sync(RecodeEvent recodeEvent) {
        return Mono.fromRunnable(() -> logger.info(recodeEvent.toJson()));
    }

}
