package cn.dustlight.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import cn.dustlight.flow.core.flow.process.Process;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
public class ZeebeProcess implements Process<String>, Cloneable {

    private ZeebeProcessEntity zeebeProcess;

    @Override
    public Instant getCreatedAt() {
        return zeebeProcess == null || zeebeProcess.getTimestamp() == null ? null : Instant.ofEpochMilli(zeebeProcess.getTimestamp());
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
        return getSuffix(str, '-');
    }

    @Override
    public String getClientId() {
        if (zeebeProcess == null || zeebeProcess.getValue() == null)
            return null;
        ZeebeProcessEntity.Value value = zeebeProcess.getValue();
        String str = value.getBpmnProcessId();
        if (!StringUtils.hasText(str) || !StringUtils.hasText((str = value.getResourceName())))
            return null;
        String prefix = getPrefix(str, '-');
        if (prefix == null)
            return null;
        return prefix.startsWith("c") ? prefix.substring(1) : prefix;
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
        return str.startsWith(clientId) ? str.substring(clientId.length() + 2) : str;
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

    protected static String getPrefix(String target, char split) {
        if (target == null)
            return null;
        int index = target.indexOf(split);
        if (index == -1)
            return null;
        return target.substring(0, index);
    }

    protected static String getSuffix(String target, char split) {
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
