package kr.mybrary.bookservice.booksearch.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookListByCategorySearchServiceRequest {

    private String type;
    private Long categoryId;
}
