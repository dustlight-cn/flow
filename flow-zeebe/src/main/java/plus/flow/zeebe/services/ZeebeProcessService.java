package plus.flow.zeebe.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.Ordered;
import plus.flow.core.flow.Process;
import plus.flow.core.flow.ProcessService;
import plus.flow.zeebe.entities.DefaultAdapterContext;
import plus.flow.zeebe.entities.ZeebeProcess;
import plus.flow.zeebe.entities.ZeebeProcessEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.function.Consumer;

public class ZeebeProcessService implements ProcessService<String> {

    private ZeebeClient zeebeClient;

    private List<ZeebeProcessAdapter> adapters;

    @Getter
    @Setter
    private String prefix = "%s_%s";

    public ZeebeProcessService(ZeebeClient zeebeClient) {
        this(zeebeClient, null);
    }

    public ZeebeProcessService(ZeebeClient zeebeClient, Set<ZeebeProcessAdapter> zeebeProcessAdapters) {
        this.zeebeClient = zeebeClient;
        if (zeebeProcessAdapters != null && zeebeProcessAdapters.size() > 0) {
            adapters = new ArrayList<>();
            adapters.addAll(zeebeProcessAdapters);
            adapters.sort(Comparator.comparingInt(Ordered::getOrder));
        }
    }

    @Override
    public Mono<Process<String>> createProcess(String clientId, String owner, String name, String processData) {
        return adapt(clientId, owner, name, processData)
                .flatMap(s -> Mono.create((Consumer<MonoSink<DeploymentEvent>>) sink ->
                                sink.onRequest(unused -> zeebeClient.newDeployCommand()
                                        .addResourceBytes(Base64.getDecoder().decode(s), computePrefix(clientId, name))
                                        .send()
                                        .whenComplete((d, e) -> {
                                            if (e == null)
                                                sink.success(d);
                                            else sink.error(e);
                                        })))
                        .map(d -> {
                            ZeebeProcessEntity zeebeProcessEntity = new ZeebeProcessEntity();
                            ZeebeProcessEntity.Value value = new ZeebeProcessEntity.Value();
                            zeebeProcessEntity.setKey(d.getKey());
                            zeebeProcessEntity.setValue(value);
                            value.setResource(processData);
                            value.setResourceName(name);
                            return new ZeebeProcess(zeebeProcessEntity);
                        }));
    }

    @Override
    public Mono<Void> deleteProcess(String clientId, String processName) {
        return null;
    }

    @Override
    public Mono<Process<String>> getProcess(String clientId, String name, Integer version) {
        return null;
    }

    @Override
    public Flux<Process<String>> findProcess(String clientId, String keyword, int offset, int limit) {
        return null;
    }

    @Override
    public Flux<Process<String>> findProcessLatest(String clientId, String keyword, int offset, int limit) {
        return null;
    }

    protected String computePrefix(String clientId, String val) {
        return String.format(prefix, clientId, val);
    }

    protected Mono<String> adapt(String clientId, String owner, String name, String processData) {
        if (adapters == null || adapters.size() == 0)
            return Mono.just(processData);
        DefaultAdapterContext context = new DefaultAdapterContext(clientId, owner, name);
        Mono<BpmnModelInstance> result = Mono.just(Bpmn.readModelFromStream(new ByteArrayInputStream(Base64.getDecoder().decode(processData))));

        for (ZeebeProcessAdapter adapter : adapters) {
            result = result.transform(bpmnModelInstanceMono ->
                    bpmnModelInstanceMono.flatMap(instance -> {
                        try {
                            adapter.adapt(instance, context);
                            return Mono.just(instance);
                        } catch (Exception e) {
                            return Mono.error(e);
                        }
                    }));
        }

        return result.map(instance -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Bpmn.writeModelToStream(out, instance);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        });
    }
}
