package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookListByCategoryResponseElement implements Serializable {

    private String thumbnailUrl;
    private String isbn13;
}
