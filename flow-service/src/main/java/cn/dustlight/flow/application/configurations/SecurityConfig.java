package cn.dustlight.flow.application.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import cn.dustlight.auth.resources.AuthSecurityWebFilterChainConfiguration;
import cn.dustlight.flow.core.security.AccessTokenHolder;
import reactor.core.publisher.Mono;

@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig extends AuthSecurityWebFilterChainConfiguration {

    @Override
    protected ServerHttpSecurity configure(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .pathMatchers("/v1/**").authenticated()
                .anyExchange().permitAll()
                .and();
    }

    @Bean
    public OAuth2AccessTokenHolder oAuth2AccessTokenHolder() {
        return new OAuth2AccessTokenHolder();
    }

    public static class OAuth2AccessTokenHolder implements AccessTokenHolder {

        @Override
        public Mono<String> getAccessToken() {
            return ReactiveSecurityContextHolder.getContext()
                    .map(securityContext -> securityContext.getAuthentication())
                    .cast(AbstractOAuth2TokenAuthenticationToken.class)
                    .filter(token -> token != null && token.getToken() != null)
                    .map(token -> token.getToken().getTokenValue());
        }

    }
}
