package kr.mybrary.bookservice.client.user;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import java.util.List;
import kr.mybrary.bookservice.client.user.api.UserServiceClient;
import kr.mybrary.bookservice.client.user.dto.request.UserInfoRequest;
import kr.mybrary.bookservice.client.user.dto.response.UserInfoServiceResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import feign.Feign;

class UserServiceClientContractTest {

    private WireMockServer wireMockServer;
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();

        ObjectMapper objectMapper = new ObjectMapper();
        Encoder encoder = new JacksonEncoder(objectMapper);
        Decoder decoder = new JacksonDecoder(objectMapper);

        userServiceClient = Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(encoder)
                .decoder(decoder)
                .target(UserServiceClient.class, "http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("user-service가 200을 반환하면 UserInfoServiceResponse로 역직렬화된다.")
    void getUsersInfo_success_deserializesResponse() {
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/users/info"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "data": {
                                    "userInfoElements": [
                                      {
                                        "userId": "user-1",
                                        "nickname": "nick1",
                                        "profileImageUrl": "https://example.com/img1.jpg"
                                      },
                                      {
                                        "userId": "user-2",
                                        "nickname": "nick2",
                                        "profileImageUrl": "https://example.com/img2.jpg"
                                      }
                                    ]
                                  }
                                }
                                """)));

        UserInfoServiceResponse response = userServiceClient.getUsersInfo(
                UserInfoRequest.of(List.of("user-1", "user-2")));

        assertThat(response.getData().getUserInfoElements()).hasSize(2);
        assertThat(response.getData().getUserInfoElements().get(0).getUserId()).isEqualTo("user-1");
        assertThat(response.getData().getUserInfoElements().get(0).getNickname()).isEqualTo("nick1");
        assertThat(response.getData().getUserInfoElements().get(1).getUserId()).isEqualTo("user-2");
    }

    @Test
    @DisplayName("getUsersInfo는 POST /api/v1/users/info 에 userIds 배열을 전송한다.")
    void getUsersInfo_sendsCorrectRequestBody() {
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/users/info"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"data": {"userInfoElements": []}}
                                """)));

        userServiceClient.getUsersInfo(UserInfoRequest.of(List.of("abc-user", "def-user")));

        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/v1/users/info"))
                .withRequestBody(equalToJson("""
                        {"userIds": ["abc-user", "def-user"]}
                        """)));
    }

    @Test
    @DisplayName("user-service가 500을 반환하면 FeignException이 발생한다.")
    void getUsersInfo_serverError_throwsFeignException() {
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/users/info"))
                .willReturn(aResponse().withStatus(500)));

        assertThatThrownBy(() ->
                userServiceClient.getUsersInfo(UserInfoRequest.of(List.of("user-1"))))
                .isInstanceOf(feign.FeignException.class);
    }
}
