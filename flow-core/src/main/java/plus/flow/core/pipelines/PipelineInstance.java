package plus.flow.core.pipelines;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import plus.flow.core.context.Context;

import java.util.Map;

@Getter
@Setter
public class PipelineInstance extends Pipeline {

    private String pipeline;
    private StatusType status;
    private Integer current;

    private Context context;

    public String getCurrentStage() {
        Stage[] stages = getStages();
        if (current == null || stages == null)
            return null;
        return stages[current].getTitle();
    }

    public boolean isDone() {
        return status == null ? false : (status == StatusType.SUCCESS || status == StatusType.FAILED);
    }

    public static PipelineInstance from(Pipeline pipeline) {
        if (pipeline == null)
            return null;
        PipelineInstance instance = new PipelineInstance();

        instance.setId(null);
        instance.setClientId(pipeline.getClientId());
        instance.setOwner(pipeline.getOwner());
        instance.setMembers(pipeline.getMembers());
        instance.setTitle(pipeline.getTitle());
        instance.setDescription(pipeline.getDescription());
        instance.setStages(pipeline.getStages());
        instance.setPipeline(pipeline.getId());

        return instance;
    }

    @JsonIgnore
    @Override
    public Map<String, String> getEnv() {
        return super.getEnv();
    }
}
