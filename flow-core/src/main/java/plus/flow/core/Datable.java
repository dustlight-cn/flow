package plus.flow.core;

import java.time.Instant;

/**
 * 创建日期和更新日期
 */
public interface Datable {

    Instant getCreatedAt();

    Instant getUpdatedAt();

}
