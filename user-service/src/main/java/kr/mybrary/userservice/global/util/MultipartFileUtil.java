package kr.mybrary.userservice.global.util;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileUtil {

    public static String generateFilePath(String path, String fileName, MultipartFile multipartFile) {
        return path + fileName + "." + getFileExtension(multipartFile);
    }

    public static String getFileExtension(MultipartFile multipartFile) {
        return multipartFile.getContentType().split("/")[1];
    }

}
