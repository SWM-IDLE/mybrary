package kr.mybrary.bookservice.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {

    private String status;
    private String message;
    private T data;

    public static <T> SuccessResponse<T> of(String status, String message, T data) {
        return new SuccessResponse<>(status, message, data);
    }
}