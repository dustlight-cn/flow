package plus.flow.application.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import plus.flow.core.ErrorDetails;
import plus.flow.core.ErrorEnum;
import plus.flow.core.FlowException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@CrossOrigin
public class ExceptionController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @ExceptionHandler(Throwable.class)
    public Mono<ErrorDetails> onException(Throwable e, ServerWebExchange exchange) {
        var response = exchange.getResponse();
        var request = exchange.getRequest() ;
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        logger.error(String.format("Error on path: %s, remote ip: %s", request.getURI(), request.getRemoteAddress()), e);
        return Mono.just(logger.isDebugEnabled() ? ErrorEnum.UNKNOWN.details(e.getMessage()) : ErrorEnum.UNKNOWN.getDetails());
    }

    @ExceptionHandler(FlowException.class)
    public Mono<ErrorDetails>  onErrorException(FlowException e,ServerWebExchange exchange) {
        var response = exchange.getResponse();
        var request = exchange.getRequest() ;

        int code = e != null && e.getErrorDetails() != null ? e.getErrorDetails().getCode() : 0;
        if (code == -1)
            response.setStatusCode(HttpStatus.OK);
        else if (code == 1)
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
        else if (code == 2)
            response.setStatusCode(HttpStatus.FORBIDDEN);
        else if (code >= 2000 && code <= 2999)
            response.setStatusCode(HttpStatus.NOT_FOUND);
        else if (code >= 3000 && code <= 3999)
            response.setStatusCode(HttpStatus.CONFLICT);
        else
            response.setStatusCode(HttpStatus.BAD_REQUEST);

        logger.debug(e.getErrorDetails().getMessage(), e);
        return Mono.just(e.getErrorDetails());
    }

    @ExceptionHandler(AuthenticationException.class)
    public Mono<ErrorDetails> onAuthenticationException(AuthenticationException e, ServerWebExchange exchange) {
        var response = exchange.getResponse();
        var request = exchange.getRequest() ;
        response.setStatusCode(HttpStatus.FORBIDDEN);
        logger.debug(e.getMessage(), e);
        return Mono.just(ErrorEnum.UNAUTHORIZED.details(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ErrorDetails> onAccessDeniedException(AccessDeniedException e, ServerWebExchange exchange) {
        var response = exchange.getResponse();
        var request = exchange.getRequest() ;
        response.setStatusCode(HttpStatus.FORBIDDEN);
        logger.debug(e.getMessage(), e);
        return Mono.just(ErrorEnum.ACCESS_DENIED.details(e.getMessage()));
    }

}
