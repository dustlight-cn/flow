package plus.flow.core.functions;

import lombok.Getter;
import lombok.Setter;
import plus.datacenter.core.entities.forms.Item;

import java.util.Collection;

@Getter
@Setter
public class InputOutputStruct {

    private Collection<Item> inputStruct;
    private Collection<Item> outputStruct;

}
