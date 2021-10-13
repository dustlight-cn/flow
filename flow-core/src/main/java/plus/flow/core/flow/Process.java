package plus.flow.core.flow;

import plus.flow.core.Datable;

/**
 * 过程服务
 *
 * @param <T>
 */
public interface Process<T> extends Datable {

    Integer getVersion();

    String getProcessName();

    String getProcessClientId();

    String getProcessOwner();

    T getProcess();
}
