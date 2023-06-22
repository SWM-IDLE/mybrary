package kr.mybrary.bookservice.book.domain.dto;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInfo {
    private String title;

    private String contents;

    private String descriptionUrl;

    private String isbn10;

    private String isbn13;

    private OffsetDateTime publishDate;

    private Integer price;

    private String thumbnailUrl;

    private List<String> authors;

    private List<String> translators;
}