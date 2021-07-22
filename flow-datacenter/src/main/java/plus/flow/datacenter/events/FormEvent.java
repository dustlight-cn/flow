package plus.flow.datacenter.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class FormEvent implements Event<FormEvent.RecordMessage> {

    private RecordMessage eventData;

    public static FormEvent from(String json) {
        FormEvent event = new FormEvent();
        event.setEventData(RecordMessage.from(json));
        return event;
    }

    public static FormEvent from(byte[] json) {
        FormEvent event = new FormEvent();
        event.setEventData(RecordMessage.from(json));
        return event;
    }

    @Override
    public String toString() {
        return "FormEvent{" +
                "eventData=" + eventData +
                '}';
    }

    @Override
    public String getEventType() {
        return getClass().getSimpleName();
    }

    @Getter
    @Setter
    public static class RecordMessage implements Serializable {

        private static ObjectMapper objectMapper = new ObjectMapper();

        private String recordId, formName, formId, clientId, owner;
        private Integer formVersion;
        private Map<String, Object> updateData;

        private MessageType type;

        public static RecordMessage from(String json) {
            try {
                return objectMapper.readValue(json, RecordMessage.class);
            } catch (IOException e) {
                throw new RuntimeException("Fail to convert json string to RecordMessage", e);
            }
        }

        public static RecordMessage from(byte[] json) {
            try {
                return objectMapper.readValue(json, RecordMessage.class);
            } catch (IOException e) {
                throw new RuntimeException("Fail to convert json string to RecordMessage", e);
            }
        }

        public String toJson() {
            try {
                return objectMapper.writeValueAsString(this);
            } catch (IOException e) {
                throw new RuntimeException("Fail to convert RecordMessage to json string", e);
            }
        }

        public enum MessageType {
            CREATED,
            UPDATED,
            DELETED
        }

        @Override
        public String toString() {
            return "RecordMessage{" +
                    "recordId='" + recordId + '\'' +
                    ", formName='" + formName + '\'' +
                    ", formId='" + formId + '\'' +
                    ", clientId='" + clientId + '\'' +
                    ", owner='" + owner + '\'' +
                    ", formVersion=" + formVersion +
                    ", updateData=" + updateData +
                    ", type=" + type +
                    '}';
        }
    }
}
