package kr.mybrary.bookservice.booksearch.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookListByCategorySearchServiceRequest {

    private String type;
    private int categoryId;
    private int page;

    public static BookListByCategorySearchServiceRequest of(String type, int categoryId, int page) {
        return BookListByCategorySearchServiceRequest.builder()
                .type(type)
                .categoryId(categoryId)
                .page(page)
                .build();
    }
}
