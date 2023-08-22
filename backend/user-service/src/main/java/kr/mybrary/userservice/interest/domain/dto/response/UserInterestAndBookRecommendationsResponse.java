package kr.mybrary.userservice.interest.domain.dto.response;

import java.util.List;
import kr.mybrary.userservice.client.book.dto.response.BookRecommendationsServiceResponse.BookRecommendationsResponseElement;
import kr.mybrary.userservice.interest.persistence.Interest;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@Builder
public class UserInterestAndBookRecommendationsResponse {

    private List<UserInterestElement> userInterests;
    private List<BookRecommendationElement> bookRecommendations;

    @Getter
    @Builder
    public static class UserInterestElement {

        private String name;
        private int code;
    }

    @Getter
    @Builder
    public static class BookRecommendationElement {

        private String thumbnailUrl;
        private String isbn13;
    }

    public static UserInterestAndBookRecommendationsResponse of(List<Interest> userInterests,
            List<BookRecommendationsResponseElement> bookRecommendations) {

        return UserInterestAndBookRecommendationsResponse.builder()
            .userInterests(toUserInterestElements(userInterests))
            .bookRecommendations(toBookRecommendationElements(bookRecommendations))
            .build();
    }

    @NotNull
    private static List<UserInterestElement> toUserInterestElements(List<Interest> userInterests) {
        return userInterests.stream()
                .map(interest -> UserInterestElement.builder()
                        .name(interest.getName())
                        .code(interest.getCode())
                        .build())
                .toList();
    }

    @NotNull
    private static List<BookRecommendationElement> toBookRecommendationElements(
            List<BookRecommendationsResponseElement> bookRecommendations) {

        return bookRecommendations.stream().
                map(bookRecommendation -> BookRecommendationElement.builder()
                        .thumbnailUrl(bookRecommendation.getThumbnailUrl())
                        .isbn13(bookRecommendation.getIsbn13())
                        .build())
                .toList();
    }
}
