package kr.mybrary.bookservice.book.infrastructure.dto.kakaoapi;

import lombok.Data;

@Data
public class Meta {

    private Integer total_count;
    private Integer pageable_count;
    private Boolean is_end;
}
