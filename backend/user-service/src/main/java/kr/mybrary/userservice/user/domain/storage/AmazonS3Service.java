package kr.mybrary.userservice.user.domain.storage;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Template;
import kr.mybrary.userservice.user.domain.exception.io.FileInputStreamException;
import kr.mybrary.userservice.user.domain.exception.storage.StorageClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AmazonS3Service implements StorageService {
    // TODO: LocalStack을 사용하여 테스트하기
    private final S3Template s3Template;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;
    private static final String RESIZED_POSTFIX = "-resized";
    private static final String BASE_URL = "https://mybrary-user-service.s3.ap-northeast-2.amazonaws.com/";
    private static final String RESIZED_BASE_URL = "https://mybrary-user-service-resized.s3.ap-northeast-2.amazonaws.com/";

    @Override
    public String putFile(MultipartFile multipartFile, String path) {
        try {
            s3Template.upload(bucketName, path, multipartFile.getInputStream(), generateObjectMetadata(multipartFile));
        } catch (S3Exception e) {
            throw new StorageClientException();
        } catch (IOException e) {
            throw new FileInputStreamException();
        }
        return BASE_URL + path;
    }

    private static ObjectMetadata generateObjectMetadata(MultipartFile multipartFile) {
        return ObjectMetadata.builder()
                .contentType(multipartFile.getContentType())
                .build();
    }

    @Override
    public String getPathFromUrl(String url) {
        return url.replace(BASE_URL, "");
    }

    @Override
    public boolean hasResizedFiles(String path, String size) {
        return s3Template.listObjects(bucketName + RESIZED_POSTFIX, size + "-" + path).size() > 0;
    }

    @Override
    public String getResizedUrl(String url, String size) {
        return RESIZED_BASE_URL + size + "-" + getPathFromUrl(url);
    }

}
