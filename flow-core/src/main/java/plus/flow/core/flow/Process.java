package plus.flow.core.flow;

import java.time.Instant;

/**
 * 过程服务
 *
 * @param <T>
 */
public interface Process<T> {

    Integer getVersion();

    Long getId();

    String getProcessName();

    String getProcessClientId();

    String getProcessOwner();

    T getProcess();

    Instant getCreatedAt();
}
