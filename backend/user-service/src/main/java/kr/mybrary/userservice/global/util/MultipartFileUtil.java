package kr.mybrary.userservice.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MultipartFileUtil {

    public static String generateFilePath(String path, String fileName, String fileExtension) {
        return path + fileName + "." + fileExtension;
    }

    public static String getFileExtension(MultipartFile multipartFile) {
        return multipartFile.getContentType().split("/")[1];
    }

    public static String generateTimestampFileName(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");
        return localDateTime.format(formatter);
    }

}
