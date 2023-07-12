package kr.mybrary.userservice.user.domain.storage;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import kr.mybrary.userservice.user.domain.exception.FileInputStreamException;
import kr.mybrary.userservice.user.domain.exception.StorageClientException;
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

    private final AmazonS3Client amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String putFile(MultipartFile multipartFile, String path, String fileName) {
        try {
            amazonS3Client.putObject(generatePutObjectRequest(multipartFile, path, fileName));
        } catch (AmazonS3Exception e) {
            throw new StorageClientException();
        } catch (IOException e) {
            throw new FileInputStreamException();
        }
        return amazonS3Client.getUrl(bucketName, path + "/" + fileName).toString();
    }

    private PutObjectRequest generatePutObjectRequest(MultipartFile multipartFile, String path,
            String fileName) throws IOException {
        ObjectMetadata objectMetadata = generateObjectMetadata(multipartFile);
        String storagePath = path + "/" + fileName;
        return new PutObjectRequest(bucketName, storagePath, multipartFile.getInputStream(),
                objectMetadata);
    }

    private static ObjectMetadata generateObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        return objectMetadata;
    }

}
