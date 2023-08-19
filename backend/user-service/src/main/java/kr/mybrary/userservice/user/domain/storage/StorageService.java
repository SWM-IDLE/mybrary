package kr.mybrary.userservice.user.domain.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String putFile(MultipartFile multipartFile, String path);

    String getPathFromUrl(String url);

    boolean hasResizedFiles(String path, String size);

    String getResizedUrl(String url, String size);

}
