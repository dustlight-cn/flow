package plus.flow.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import plus.flow.core.flow.usertask.UserTask;
import plus.flow.core.flow.usertask.UserTaskService;


@SpringBootTest
public class ZeebeUserTaskServiceTest {

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void test() throws JsonProcessingException {
        UserTask task = userTaskService.getTask("86c3e34e2030000", 6755399441312133L).block();

        System.out.println(mapper.writeValueAsString(task));
    }
}
