package kr.mybrary.bookservice.mybook.persistence;

import lombok.Getter;

@Getter
public enum ReadStatus {
    TO_READ("읽기전"), READING("읽는중"), COMPLETED("완독");

    private final String status;

    ReadStatus(String status) {
        this.status = status;
    }

    public static ReadStatus of(String readStatus) {

        if (readStatus == null) {
            return null;
        }

        for (ReadStatus value : values()) {
            if (value.name().equals(readStatus.toUpperCase())) {
                return value;
            }
        }

        return null;
    }
}
