package kr.mybrary.userservice.user.domain.exception.io;

import kr.mybrary.userservice.global.exception.ApplicationException;

public class FileInputStreamException extends ApplicationException {

    private final static int STATUS = 500;
    private final static String ERROR_CODE = "IO-02";
    private final static String ERROR_MESSAGE = "파일을 읽어오는데 실패했습니다.";

    public FileInputStreamException() {
        super(STATUS, ERROR_CODE, ERROR_MESSAGE);
    }

}
