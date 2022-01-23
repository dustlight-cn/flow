package cn.dustlight.flow.core.flow.process;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.Instant;

/**
 * 过程服务
 *
 * @param <T>
 */
public interface Process<T> {

    Integer getVersion();

    @JsonSerialize(using = ToStringSerializer.class)
    Long getId();

    String getName();

    String getClientId();

    String getOwner();

    T getData();

    Instant getCreatedAt();
}
