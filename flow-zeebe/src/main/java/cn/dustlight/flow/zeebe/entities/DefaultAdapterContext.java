package cn.dustlight.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import cn.dustlight.flow.zeebe.services.adapters.AdapterContext;


@Getter
@Setter
@AllArgsConstructor
public class DefaultAdapterContext implements AdapterContext, Cloneable {

    private String clientId;
    private String owner;

    @Override
    public DefaultAdapterContext clone() throws CloneNotSupportedException {
        return (DefaultAdapterContext) super.clone();
    }
}
