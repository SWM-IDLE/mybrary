package kr.mybrary.userservice.user.domain.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String putFile(MultipartFile multipartFile, String path);

}
