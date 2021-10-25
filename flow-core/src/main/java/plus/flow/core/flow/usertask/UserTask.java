package plus.flow.core.flow.usertask;

public interface UserTask<T> {

    T getSchema();

    Long getId();

    Long getUser();

}
