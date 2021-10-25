package plus.flow.datacenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.*;
import plus.datacenter.core.ErrorDetails;
import plus.flow.core.exceptions.ErrorEnum;
import plus.flow.core.flow.usertask.UserTaskDataValidator;
import plus.flow.core.security.AccessTokenHolder;
import reactor.core.publisher.Mono;

import java.util.Map;

public class DatacenterRecordValidator implements UserTaskDataValidator {

    private WebClient webClient;
    private AccessTokenHolder accessTokenHolder;
    private ObjectMapper mapper;

    public DatacenterRecordValidator(String baseUrl,
                                     AccessTokenHolder accessTokenHolder,
                                     ObjectMapper mapper) {
        this.accessTokenHolder = accessTokenHolder;
        this.mapper = mapper;
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .filter((request, next) -> accessTokenHolder.getAccessToken()
                        .flatMap(token -> next.exchange(ClientRequest.from(request)
                                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token))
                                .build())))
                .build();
    }

    @Override
    public Mono<Boolean> verify(String form, Map<String, Object> data) {
        return webClient.post()
                .uri("/v1/validation")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(new Record(form, data).toJson())
                .retrieve()
                .bodyToMono(ClientResponse.class)
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.OK)
                        return Mono.just(true);
                    return clientResponse.bodyToMono(ErrorDetails.class)
                            .map(errorDetails -> ErrorEnum.UNKNOWN.details(errorDetails.getMessage()).getException())
                            .flatMap(e -> Mono.error(e));
                });
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Record {

        private String form;
        private Map<String, Object> data;

        @SneakyThrows
        public String toJson() {
            return mapper.writeValueAsString(this);
        }

    }

}
