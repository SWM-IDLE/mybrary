package kr.mybrary.apigatewayserver.filter;

import kr.mybrary.apigatewayserver.exception.AccessTokenNotFoundException;
import kr.mybrary.apigatewayserver.exception.ApplicationException;
import kr.mybrary.apigatewayserver.exception.LoggedOutAccessTokenException;
import kr.mybrary.apigatewayserver.util.JwtUtil;
import kr.mybrary.apigatewayserver.util.RedisUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;

    private static final String SIGN_UP_PATH = "/sign-up";
    private static final String AUTHENTICATION_PATH = "/auth";
    private static final String OAUTH2_PATH = "/oauth2/authorization";
    private static final List<String> TOKEN_AUTH_WHITELIST = List.of(SIGN_UP_PATH, AUTHENTICATION_PATH, OAUTH2_PATH);
    private static final String ERROR_MESSAGE_FORMAT = "{\"errorCode\": \"%s\", \"errorMessage\": \"%s\"}";

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                jwtUtil.extractAccessToken(exchange.getRequest()).ifPresentOrElse(accessToken -> {
                        if (isTokenRefreshRequested(exchange.getRequest())) {
                            return;
                        }
                        checkIfTokenIsLogout(accessToken);
                        jwtUtil.validateToken(accessToken);

                        exchange.getRequest().mutate()
                                .header("USER-ID", jwtUtil.getUserId(accessToken).get()).build();
                    },
                    () -> {
                        if(tokenAuthenticationRequired(exchange.getRequest())) {
                            throw new AccessTokenNotFoundException();
                        }
                    }
                );
            } catch (ApplicationException e) {
                return onError(exchange.getResponse(), e);
            }
            return chain.filter(exchange);
        };
    }

    private boolean isTokenRefreshRequested(ServerHttpRequest request) {
        if (request.getURI().getPath().contains("/refresh")) {
            return true;
        }
        return false;
    }

    private boolean tokenAuthenticationRequired(ServerHttpRequest request) {
        return !TOKEN_AUTH_WHITELIST.stream()
                .anyMatch(uri -> request.getURI().getPath().contains(uri));
    }

    private void checkIfTokenIsLogout(String accessToken) {
        if (redisUtil.hasKey(accessToken)) {
            throw new LoggedOutAccessTokenException();
        }
    }

    private Mono<Void> onError(ServerHttpResponse response, ApplicationException e) {
        response.setRawStatusCode(e.getStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody = String.format(ERROR_MESSAGE_FORMAT, e.getErrorCode(), e.getErrorMessage());
        DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Data
    public static class Config {
    }

}
