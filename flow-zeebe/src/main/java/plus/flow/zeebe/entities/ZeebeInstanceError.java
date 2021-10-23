package plus.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import plus.flow.core.flow.instance.InstanceError;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZeebeInstanceError implements InstanceError {

    private String message, type;

}
