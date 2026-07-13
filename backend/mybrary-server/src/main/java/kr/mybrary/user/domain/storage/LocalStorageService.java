package kr.mybrary.user.domain.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Profile("local")
@Slf4j
public class LocalStorageService implements StorageService {

    private static final String BASE_URL = "http://localhost:8080/uploads/";

    @Value("${local.storage.upload-dir:./local-uploads}")
    private String uploadDir;

    @Override
    public String putFile(MultipartFile multipartFile, String path) {
        try {
            Path target = Paths.get(uploadDir, path);
            Files.createDirectories(target.getParent());
            multipartFile.transferTo(target.toAbsolutePath());
            log.debug("로컬 파일 저장: {}", target.toAbsolutePath());
            return BASE_URL + path;
        } catch (IOException e) {
            throw new RuntimeException("로컬 파일 저장 실패: " + path, e);
        }
    }

    @Override
    public String getPathFromUrl(String url) {
        return url.replace(BASE_URL, "");
    }

    // 로컬 환경에서는 리사이즈 없이 원본 반환
    @Override
    public boolean hasResizedFiles(String path, String size) {
        return false;
    }

    @Override
    public String getResizedUrl(String url, String size) {
        return url;
    }
}
