package plus.flow.core.flow.usertask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUserTarget implements UserTaskTarget {

    private Collection<String> users;
    private Collection<String> roles;

}
