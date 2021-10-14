package plus.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import plus.flow.core.flow.Process;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
public class ZeebeProcess implements Process<String>, Cloneable {

    private ZeebeProcessEntity zeebeProcess;

    @Override
    public Instant getCreatedAt() {
        return zeebeProcess == null ? null : Instant.ofEpochMilli(zeebeProcess.getTimestamp());
    }

    @Override
    public Integer getVersion() {
        return zeebeProcess == null || zeebeProcess.getValue() == null ? null : zeebeProcess.getValue().getVersion();
    }

    @Override
    public Long getId() {
        return zeebeProcess == null ? null : zeebeProcess.getKey();
    }

    @Override
    public String getName() {
        if (zeebeProcess == null || zeebeProcess.getValue() == null)
            return null;
        ZeebeProcessEntity.Value value = zeebeProcess.getValue();
        String str = value.getBpmnProcessId();
        if (!StringUtils.hasText(str))
            return null;
        return getSuffix(str, '.');
    }

    @Override
    public String getClientId() {
        if (zeebeProcess == null || zeebeProcess.getValue() == null)
            return null;
        ZeebeProcessEntity.Value value = zeebeProcess.getValue();
        String str = value.getBpmnProcessId();
        if (!StringUtils.hasText(str) || !StringUtils.hasText((str = value.getResourceName())))
            return null;
        return getPrefix(str, '.');
    }

    @Override
    public String getOwner() {
        if (zeebeProcess == null || zeebeProcess.getValue() == null)
            return null;
        ZeebeProcessEntity.Value value = zeebeProcess.getValue();
        String str = value.getResourceName();
        if (!StringUtils.hasText(str))
            return null;
        String clientId = getClientId();
        if (!StringUtils.hasText(clientId))
            return str;
        return str.startsWith(clientId) ? str.substring(clientId.length() + 1) : str;
    }

    @Override
    public String getData() {
        return zeebeProcess == null || zeebeProcess.getValue() == null ? null : zeebeProcess.getValue().getResource();
    }

    public void setData(String data) {
        if (zeebeProcess == null)
            zeebeProcess = new ZeebeProcessEntity();
        if (zeebeProcess.getValue() == null)
            zeebeProcess.setValue(new ZeebeProcessEntity.Value());
        ZeebeProcessEntity.Value value = zeebeProcess.getValue();
        value.setResource(data);
    }

    private static String getPrefix(String target, char split) {
        if (target == null)
            return null;
        int index = target.indexOf(split);
        if (index == -1)
            return null;
        return target.substring(0, index);
    }

    private static String getSuffix(String target, char split) {
        if (target == null)
            return null;
        int index = target.indexOf(split);
        if (index == -1)
            return null;
        return target.substring(index + 1);
    }

    @Override
    protected ZeebeProcess clone() throws CloneNotSupportedException {
        return (ZeebeProcess) super.clone();
    }

    public static ZeebeProcess cloneAndSet(ZeebeProcess template, ZeebeProcessEntity entity) throws CloneNotSupportedException {
        ZeebeProcess instance = template.clone();
        instance.zeebeProcess = entity;
        return instance;
    }
}
