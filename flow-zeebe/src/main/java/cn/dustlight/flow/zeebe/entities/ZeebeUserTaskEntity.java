package cn.dustlight.flow.zeebe.entities;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;
import cn.dustlight.flow.core.flow.usertask.DefaultUserTarget;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ZeebeUserTaskEntity implements Cloneable {

    private static final ZeebeUserTaskEntity t = new ZeebeUserTaskEntity();
    static final String TARGET_USERS = "plus.flow:users";
    static final String TARGET_ROLES = "plus.flow:roles";
    static final String FORM_KEY = "io.camunda.zeebe:formKey";

    @Id
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

    private DefaultUserTarget target;
    private String doneBy;
    private Instant doneAt;
    private Map<String, Object> data;

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

        Map<String, String> headers = job.getCustomHeaders();
        String targetUsers = headers.get(TARGET_USERS);
        String targetRoles = headers.get(TARGET_ROLES);
        instance.target = new DefaultUserTarget();
        if (StringUtils.hasText(targetUsers)) {
            String[] users = targetUsers.split(",");
            Set<String> us = new HashSet<>();
            for (int i = 0; i < users.length; i++) {
                users[i] = users[i].trim();
                if (users[i].startsWith("=")) {
                    Object val = instance.variables.get(users[i].substring(1));
                    if (val != null)
                        users[i] = val.toString();
                    else
                        users[i] = null;
                }
                if (StringUtils.hasText(users[i]))
                    us.add(users[i]);
            }
            if (us.size() > 0)
                instance.target.setUsers(us);
        }
        if (StringUtils.hasText(targetRoles)) {
            String[] roles = targetRoles.split(",");
            Set<String> rs = new HashSet<>();
            for (int i = 0; i < roles.length; i++) {
                roles[i] = roles[i].trim();
                if (roles[i].startsWith("=")) {
                    Object val = instance.variables.get(roles[i].substring(1));
                    if (val != null)
                        roles[i] = val.toString();
                    else
                        roles[i] = null;
                }
                if (StringUtils.hasText(roles[i]))
                    rs.add(roles[i]);
            }
            if (rs.size() > 0)
                instance.target.setRoles(rs);
        }
        return instance;
    }
}
