package kr.mybrary.userservice.client.book;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import kr.mybrary.userservice.client.book.api.BookServiceClient;
import kr.mybrary.userservice.client.book.dto.response.BookRecommendationsServiceResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

class BookServiceClientContractTest {

    private WireMockServer wireMockServer;
    private BookServiceClient bookServiceClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();

        ObjectMapper objectMapper = new ObjectMapper();
        Encoder encoder = new JacksonEncoder(objectMapper);
        Decoder decoder = new JacksonDecoder(objectMapper);

        bookServiceClient = Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(encoder)
                .decoder(decoder)
                .target(BookServiceClient.class, "http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("book-service가 200을 반환하면 BookRecommendationsServiceResponse로 역직렬화된다.")
    void getBookListByCategoryId_success_deserializesResponse() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/books/recommendations/bestseller/categories/1?page=1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "data": {
                                    "books": [
                                      {
                                        "thumbnailUrl": "https://example.com/thumbnail1.jpg",
                                        "isbn13": "9781234567890"
                                      },
                                      {
                                        "thumbnailUrl": "https://example.com/thumbnail2.jpg",
                                        "isbn13": "9780987654321"
                                      }
                                    ]
                                  }
                                }
                                """)));

        BookRecommendationsServiceResponse response =
                bookServiceClient.getBookListByCategoryId("bestseller", 1, 1);

        assertThat(response.getData().getBooks()).hasSize(2);
        assertThat(response.getData().getBooks().get(0).getIsbn13()).isEqualTo("9781234567890");
        assertThat(response.getData().getBooks().get(1).getThumbnailUrl())
                .isEqualTo("https://example.com/thumbnail2.jpg");
    }

    @Test
    @DisplayName("getBookListByCategoryId는 GET /api/v1/books/recommendations/{type}/categories/{categoryId}?page={page} 를 호출한다.")
    void getBookListByCategoryId_sendsCorrectUrlWithPathAndQueryParams() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/books/recommendations/new/categories/5?page=2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"data": {"books": []}}
                                """)));

        bookServiceClient.getBookListByCategoryId("new", 5, 2);

        wireMockServer.verify(getRequestedFor(
                urlEqualTo("/api/v1/books/recommendations/new/categories/5?page=2")));
    }

    @Test
    @DisplayName("book-service가 500을 반환하면 FeignException이 발생한다.")
    void getBookListByCategoryId_serverError_throwsFeignException() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/books/recommendations/bestseller/categories/1?page=1"))
                .willReturn(aResponse().withStatus(500)));

        assertThatThrownBy(() ->
                bookServiceClient.getBookListByCategoryId("bestseller", 1, 1))
                .isInstanceOf(feign.FeignException.class);
    }

    @Test
    @DisplayName("book-service가 빈 books 배열을 반환하면 빈 리스트로 역직렬화된다.")
    void getBookListByCategoryId_emptyBooks_returnsEmptyList() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/books/recommendations/bestseller/categories/99?page=1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"data": {"books": []}}
                                """)));

        BookRecommendationsServiceResponse response =
                bookServiceClient.getBookListByCategoryId("bestseller", 99, 1);

        assertThat(response.getData().getBooks()).isEmpty();
    }
}
