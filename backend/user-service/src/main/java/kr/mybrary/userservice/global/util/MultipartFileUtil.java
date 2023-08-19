package kr.mybrary.userservice.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MultipartFileUtil {

    public static String generateFilePath(String path, String fileName, String fileExtension) {
        return path + fileName + "." + fileExtension;
    }

    public static String getFileExtension(MultipartFile multipartFile) {
        return multipartFile.getContentType().split("/")[1];
    }

    public static String generateTimestampFileName(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        return dateFormat.format(date);
    }

}
