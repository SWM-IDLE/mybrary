package kr.mybrary.bookservice.booksearch.domain.dto.kakaoapi;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Document {

    private String title;
    private String contents;
    private String url;
    private String isbn;
    private OffsetDateTime datetime;
    private List<String> authors;
    private String publisher;
    private List<String> translators;
    private Integer price;
    private Integer sale_price;
    private String thumbnail;
    private String status;
}
