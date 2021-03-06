package cn.dustlight.flow.core.flow.usertask;

import java.time.Instant;
import java.util.Map;

public interface UserTask {

    String getForm();

    String getProcessName();

    String getClientId();

    String getElementId();

    Long getProcessId();

    Long getInstanceId();

    Long getId();

    String getUser();

    Instant getCompletedAt();

    UserTaskTarget getTarget();

    Map<String,Object> getVariables();

    Map<String,Object> getData();
}
