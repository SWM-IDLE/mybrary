package kr.mybrary.bookservice.booksearch.domain;

import static org.junit.jupiter.api.Assertions.*;

import kr.mybrary.bookservice.booksearch.domain.dto.BookSearchDtoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookSearchDtoMapperTest {

    private final static String ISBN_10_AND_13 = "8980782977 9788980782970";
    private final static String ISBN_10 = "8980782977";
    private final static String ISBN_13 = "9788980782970";

    @DisplayName("ISBN10과 ISBN13이 모두 존재할 경우, ISBN10과 ISBN13으로 파싱해서 ISBN을 가져온다.")
    @Test
    void getISBNWhenHasISBN10And13() {

        // when given
        String isbn10 = BookSearchDtoMapper.getISBN10(ISBN_10_AND_13);
        String isbn13 = BookSearchDtoMapper.getISBN13(ISBN_10_AND_13);

        // then
        assertAll(
                () -> assertEquals(ISBN_10, isbn10),
                () -> assertEquals(ISBN_13, isbn13)
        );
    }

    @DisplayName("ISBN10만 존재할 경우, ISBN10을 가져온다.")
    @Test
    void getISBNWhenHasISBN10() {

        // when given
        String isbn10 = BookSearchDtoMapper.getISBN10(ISBN_10);
        String isbn13 = BookSearchDtoMapper.getISBN13(ISBN_10);

        // then
        assertAll(
                () -> assertEquals(ISBN_10, isbn10),
                () -> assertEquals("", isbn13)
        );
    }

    @DisplayName("ISBN13만 존재할 경우, ISBN13을 가져온다.")
    @Test
    void getISBNWhenHasISBN13() {

        // when given
        String isbn10 = BookSearchDtoMapper.getISBN10(ISBN_13);
        String isbn13 = BookSearchDtoMapper.getISBN13(ISBN_13);

        // then
        assertAll(
                () -> assertEquals("", isbn10),
                () -> assertEquals(ISBN_13, isbn13)
        );
    }
}