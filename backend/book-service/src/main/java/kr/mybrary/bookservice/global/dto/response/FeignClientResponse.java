package kr.mybrary.bookservice.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeignClientResponse<T> {

    private T data;

    public static <T> FeignClientResponse<T> of(T data) {
        return new FeignClientResponse<>(data);
    }
}
