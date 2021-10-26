package plus.flow.core.security;

import reactor.core.publisher.Mono;

public interface AccessTokenHolder {

    Mono<String> getAccessToken();
}
