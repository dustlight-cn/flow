package plus.flow.core.flow.usertask;

public interface UserTask {

    String getForm();

    Long getProcessId();

    Long getInstanceId();

    Long getId();

    Long getUser();

    UserTaskTarget getTarget();
}
