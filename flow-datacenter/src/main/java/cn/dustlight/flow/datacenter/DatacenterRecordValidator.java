package cn.dustlight.flow.datacenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.*;
import cn.dustlight.flow.core.exceptions.ErrorDetails;
import cn.dustlight.flow.core.exceptions.ErrorEnum;
import cn.dustlight.flow.core.flow.usertask.UserTaskDataValidator;
import cn.dustlight.flow.core.security.AccessTokenHolder;
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
                .filter((request, next) -> this.accessTokenHolder.getAccessToken()
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
                .exchangeToMono(clientResponse -> clientResponse.statusCode() == HttpStatus.OK ?
                        Mono.just(true) :
                        clientResponse.bodyToMono(ErrorDetails.class)
                                .map(errorDetails -> ErrorEnum.USER_TASK_DATA_INVALID
                                        .details(String.format("(#%d) %s, %s", errorDetails.getCode(), errorDetails.getMessage(), errorDetails.getDetails()))
                                        .getException())
                                .flatMap(e -> Mono.error(e)));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class Record {

        private String formName;
        private Map<String, Object> data;

        @SneakyThrows
        public String toJson() {
            return mapper.writeValueAsString(this);
        }

    }

}
