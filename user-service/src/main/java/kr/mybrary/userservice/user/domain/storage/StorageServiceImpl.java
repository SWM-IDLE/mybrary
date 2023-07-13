package kr.mybrary.userservice.user.domain.storage;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import kr.mybrary.userservice.user.domain.exception.file.FileInputStreamException;
import kr.mybrary.userservice.user.domain.exception.storage.StorageClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    // TODO: LocalStack을 사용하여 테스트하기
    private final S3Template amazonS3Client;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;
    private static final String BASE_PATH = "https://mybrary-user-service.s3.ap-northeast-2.amazonaws.com/";

    @Override
    public String putFile(MultipartFile multipartFile, String path, String fileName) {
        String profileImageFilePath = generateProfileImageFilePath(path, fileName, multipartFile);
        try {
            amazonS3Client.upload(
                    bucketName,
                    profileImageFilePath,
                    multipartFile.getInputStream(),
                    generateObjectMetadata(multipartFile)
            );
        } catch (S3Exception e) {
            throw new StorageClientException();
        } catch (IOException e) {
            throw new FileInputStreamException();
        }
        return BASE_PATH + profileImageFilePath;
    }

    private static String generateProfileImageFilePath(String path, String fileName, MultipartFile multipartFile) {
        return path + "/" + fileName + "." + multipartFile.getContentType().split("/")[1];
    }

    private static ObjectMetadata generateObjectMetadata(MultipartFile multipartFile) {
        return ObjectMetadata.builder()
                .contentType(multipartFile.getContentType())
                .build();
    }

}
