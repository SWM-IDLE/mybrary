package kr.mybrary.bookservice.mybook.persistence;

import lombok.Getter;

@Getter
public enum ReadStatus {
    TO_READ("읽기전"), READING("읽는중"), COMPLETED("완독");

    private String status;

    ReadStatus(String status) {
        this.status = status;
    }
}
