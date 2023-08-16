package kr.mybrary.apigatewayserver.filter;

import kr.mybrary.apigatewayserver.util.JwtUtil;
import kr.mybrary.apigatewayserver.util.RedisUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
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

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                jwtUtil.extractAccessToken(exchange.getRequest()).ifPresentOrElse(accessToken -> {
                            if (jwtUtil.hasRefreshToken(exchange.getRequest())) {
                                return;
                            }

                            checkIfTokenIsLogout(accessToken);
                            jwtUtil.validateToken(accessToken);

                            exchange.getRequest().mutate()
                                    .header("USER-ID", jwtUtil.getUserId(accessToken).get()).build();
                        },
                        () -> {
                            if(tokenAuthenticationRequired(exchange.getRequest())) {
                                throw new IllegalArgumentException("access token required");
                            }
                        }
                );
            } catch (Exception e) {
                return onError(exchange.getResponse(), e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };
    }

    private boolean tokenAuthenticationRequired(ServerHttpRequest request) {
        return !TOKEN_AUTH_WHITELIST.stream()
                .anyMatch(uri -> request.getURI().getPath().contains(uri));
    }

    private void checkIfTokenIsLogout(String accessToken) {
        if (redisUtil.hasKey(accessToken)) {
            throw new IllegalArgumentException("token is logout");
        }
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Data
    public static class Config {
    }

}
