package kr.mybrary.bookservice.booksearch.presentation.dto.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookListByCategorySearchResultResponse implements Serializable {

    private List<BookListByCategoryResponseElement> books;

    public static BookListByCategorySearchResultResponse of(List<BookListByCategoryResponseElement> bookListByCategorySearchResultElement) {
        return BookListByCategorySearchResultResponse.builder()
                .books(bookListByCategorySearchResultElement)
                .build();
    }
}
