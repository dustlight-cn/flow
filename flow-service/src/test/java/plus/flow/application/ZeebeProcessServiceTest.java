package plus.flow.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import plus.flow.core.flow.Instance;
import plus.flow.core.flow.InstanceService;
import plus.flow.core.flow.ProcessService;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ZeebeProcessServiceTest {

    @Autowired
    ProcessService processService;

    @Autowired
    InstanceService instanceService;

    Log logger = LogFactory.getLog(getClass());

    @Autowired
    ObjectMapper mapper;

    byte[] getBpmnBytes(String resourceName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(resourceName);
        try (BufferedInputStream in = new BufferedInputStream(classPathResource.getInputStream())) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int len;
            while ((len = in.read(buffer, 0, buffer.length)) != -1)
                outputStream.write(buffer, 0, len);
            outputStream.close();
            return outputStream.toByteArray();
        }
    }

    @Test
    public void test() throws IOException {
        String processData = Base64.getEncoder().encodeToString(getBpmnBytes("order.bpmn"));
        processService.createProcess("86c3e34e2030000", "ggboy", processData)
                .map(p -> {
                    try {
                        logger.info(mapper.writeValueAsString(p));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return p;
                })
                .block();
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", "123456");
        variables.put("orderValue", "500");
        variables.put("gg", "hello world");
        instanceService.start("86c3e34e2030000", "order-demo", variables).block();
    }

    @Test
    public void getProcess() throws JsonProcessingException {
        Object process = processService.getProcess("86c3e34e2030000", "order-demo", null).block();
        logger.info(mapper.writeValueAsString(process));
    }

    @Test
    public void findProcess() throws JsonProcessingException {
        Collection list = (Collection) processService.findProcess("86c3e34e2030000", "", 0, 10).collectList().block();
        for (Object obj : list) {
            logger.info(mapper.writeValueAsString(obj));
        }
    }

    @Test
    public void findInstance() throws JsonProcessingException {
        Collection<Instance> list = instanceService.listInstance("86c3e34e2030000",
                "",
                null,
                null,
                1,
                10)
                .collectList()
                .block();
        for (Instance obj : list) {
            logger.info(mapper.writeValueAsString(obj));
        }
    }
}
