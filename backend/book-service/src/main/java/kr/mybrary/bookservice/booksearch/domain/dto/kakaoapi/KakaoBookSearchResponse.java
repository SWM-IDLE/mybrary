package kr.mybrary.bookservice.booksearch.domain.dto.kakaoapi;

import java.util.List;
import lombok.Data;

@Data
public class KakaoBookSearchResponse {

    private Meta meta;
    private List<Document> documents;
}
