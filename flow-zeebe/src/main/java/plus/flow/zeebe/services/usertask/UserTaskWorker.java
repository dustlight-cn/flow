package plus.flow.zeebe.services.usertask;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.worker.JobWorker;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import plus.flow.zeebe.entities.ZeebeUserTaskEntity;

import java.time.Duration;

public class UserTaskWorker implements JobHandler {

    private ZeebeClient zeebeClient;
    private String jobType = "io.camunda.zeebe:userTask";
    private String workerName = "UserTask";
    private JobWorker worker;
    private ReactiveElasticsearchOperations operations;

    @Getter
    @Setter
    private String index = "flow-user-task";

    private static final Log logger = LogFactory.getLog(UserTaskWorker.class);

    public UserTaskWorker(ZeebeClient zeebeClient, ReactiveElasticsearchOperations elasticsearchOperations) {
        this.zeebeClient = zeebeClient;
        this.operations = elasticsearchOperations;
    }


    public void start() {
        if (isRunning())
            return;
        worker = zeebeClient.newWorker()
                .jobType(jobType)
                .handler(this::handle)
                .name(workerName)
                .timeout(Duration.ofSeconds(10))
                .open();
    }

    public void stop() {
        if (isRunning())
            worker.close();
    }

    public boolean isRunning() {
        return worker != null && worker.isOpen();
    }

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        try {
            ZeebeUserTaskEntity instance = ZeebeUserTaskEntity.fromJob(job);
            operations.save(instance, IndexCoordinates.of(index)).block();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            client.newThrowErrorCommand(job.getKey())
                    .errorCode("Fail to save job into es.")
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
