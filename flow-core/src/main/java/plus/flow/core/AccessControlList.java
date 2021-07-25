package plus.flow.core;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import plus.auth.entities.AuthUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

@Getter
@Setter
public class AccessControlList extends HashMap<String, String> implements Serializable {

    private final static Log logger = LogFactory.getLog(AccessControlList.class);

    public Collection<String> getPermissions(AuthUser user) {

        return user.getAuthorities();
    }

    public enum Operator {
        WRITE,
        READ
    }
}
