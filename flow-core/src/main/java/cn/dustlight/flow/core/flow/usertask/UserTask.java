package cn.dustlight.flow.core.flow.usertask;

import java.time.Instant;

public interface UserTask {

    String getForm();

    Long getProcessId();

    Long getInstanceId();

    Long getId();

    String getUser();

    Instant getCompletedAt();

    UserTaskTarget getTarget();

}
