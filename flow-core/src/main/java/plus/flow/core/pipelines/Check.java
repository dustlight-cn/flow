package plus.flow.core.pipelines;

import lombok.Getter;
import lombok.Setter;
import plus.datacenter.core.entities.forms.ItemGroup;
import plus.flow.core.AccessControlList;

import java.io.Serializable;

@Getter
@Setter
public class Check implements Serializable {

    private ItemGroup require;

    private AccessControlList acl;

}
