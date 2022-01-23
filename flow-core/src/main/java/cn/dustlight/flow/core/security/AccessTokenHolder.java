package cn.dustlight.flow.core.security;

import reactor.core.publisher.Mono;

public interface AccessTokenHolder {

    Mono<String> getAccessToken();
}
