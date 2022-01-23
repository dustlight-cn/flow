package cn.dustlight.flow.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import cn.dustlight.flow.core.flow.usertask.UserTask;
import cn.dustlight.flow.core.flow.usertask.UserTaskService;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest
public class ZeebeUserTaskServiceTest {

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void getTask() throws JsonProcessingException {
        UserTask task = (UserTask) userTaskService.getTask("86c3e34e2030000", 6755399441316180L).block();

        System.out.println(mapper.writeValueAsString(task));
    }

    @Test
    public void completeTask() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("gg1", "boy1");
        map.put("gg2", "boy2");
        userTaskService.complete("86c3e34e2030000", 6755399441316180L, "ggboy", map).block();
        getTask();
    }
}
