package plus.flow.zeebe.entities;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Map;

@Getter
@Setter
public class ZeebeUserTaskEntity implements Cloneable {

    private static final ZeebeUserTaskEntity t = new ZeebeUserTaskEntity();

    private long key;
    private Map<String, String> customHeaders;
    private long processInstanceKey;
    private String bpmnProcessId;
    private int processDefinitionVersion;
    private long processDefinitionKey;
    private String elementId;
    private long elementInstanceKey;
    private int retries;
    private long deadline;
    private Map<String, Object> variables;

    @SneakyThrows
    public static ZeebeUserTaskEntity fromJob(ActivatedJob job) {
        ZeebeUserTaskEntity instance = (ZeebeUserTaskEntity) t.clone();
        instance.key = job.getKey();
        instance.customHeaders = job.getCustomHeaders();
        instance.processInstanceKey = job.getProcessInstanceKey();
        instance.bpmnProcessId = job.getBpmnProcessId();
        instance.processDefinitionVersion = job.getProcessDefinitionVersion();
        instance.processDefinitionKey = job.getProcessDefinitionKey();
        instance.elementId = job.getElementId();
        instance.elementInstanceKey = job.getElementInstanceKey();
        instance.retries = job.getRetries();
        instance.deadline = job.getDeadline();
        instance.variables = job.getVariablesAsMap();
        return instance;
    }
}
