package cn.dustlight.flow.core.flow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult<T> implements Serializable {

    private long count;

    private Collection<? extends T> data;

}
