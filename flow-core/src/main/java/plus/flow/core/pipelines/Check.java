package plus.flow.core.pipelines;

import lombok.Getter;
import lombok.Setter;
import plus.datacenter.core.entities.forms.ItemGroup;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class Check implements Serializable {

    private ItemGroup require;
    private Collection<String> users;

}
