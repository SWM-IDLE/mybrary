package kr.mybrary.apigatewayserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import kr.mybrary.apigatewayserver.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private static final String ACCESS_TOKEN_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";
    private static final String BEARER = "Bearer ";
    private static final String USER_ID_CLAIM = "loginId";
    private static final String INVALID_TOKEN_MESSAGE = "token is invalid";

    public Optional<String> extractAccessToken(ServerHttpRequest request) {
        return Optional.ofNullable(request.getHeaders().get(ACCESS_TOKEN_HEADER))
                .map(authorization -> authorization.get(0).replace(BEARER, ""));
    }

    public void validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public Optional<String> getUserId(String accessToken) {
        return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(USER_ID_CLAIM)
                .asString());
    }

    public boolean hasRefreshToken(ServerHttpRequest request) {
        return request.getHeaders().containsKey(REFRESH_TOKEN_HEADER);
    }

}
