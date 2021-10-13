package plus.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import plus.flow.core.flow.Process;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
public class ZeebeProcess implements Process<String> {

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
    public String getProcessName() {
        return zeebeProcess == null || zeebeProcess.getValue() == null ? null : zeebeProcess.getValue().getBpmnProcessId();
    }

    @Override
    public String getProcessClientId() {
        return null;
    }

    @Override
    public String getProcessOwner() {
        return null;
    }

    @Override
    public String getProcess() {
        return zeebeProcess == null || zeebeProcess.getValue() == null ? null : zeebeProcess.getValue().getResource();
    }
}
