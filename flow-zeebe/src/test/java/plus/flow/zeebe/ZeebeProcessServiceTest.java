package plus.flow.zeebe;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import plus.flow.zeebe.services.ZeebeProcessService;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@SpringBootTest
public class ZeebeProcessServiceTest {

    @Autowired
    ZeebeProcessService zeebeProcessService;

    Log logger = LogFactory.getLog(getClass());

    Gson gson = new Gson();

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
        zeebeProcessService.createProcess("demo", "ggboy", "order-ggboy", processData)
                .map(stringProcess -> {
                    logger.info(gson.toJson(stringProcess));
                    return stringProcess;
                })
                .block();
    }

}
