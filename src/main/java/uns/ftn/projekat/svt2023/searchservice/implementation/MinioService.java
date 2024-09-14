package uns.ftn.projekat.svt2023.searchservice.implementation;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uns.ftn.projekat.svt2023.exceptionhandling.exception.StorageException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${spring.minio.groups-bucket}")
    private String bucketName;


    @PostConstruct
    public void createBucketsIfNotExists() {
        try {
            createBucket(bucketName);
        } catch (Exception e) {
            throw new StorageException("Error while creating buckets: " + e.getMessage());
        }
    }

    private void createBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ErrorResponseException, ServerException, InternalException, InvalidResponseException, XmlParserException {
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        var originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new StorageException("Original filename is invalid.");
        }

        var originalFilenameTokens = originalFilename.split("\\.");
        var extension = originalFilenameTokens[originalFilenameTokens.length - 1];
        var objectName = UUID.randomUUID().toString() + "." + extension;

        try {
            // Ensure the bucket exists
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            try (InputStream inputStream = file.getInputStream()) {
                PutObjectArgs args = PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .headers(Collections.singletonMap("Content-Disposition",
                                "attachment; filename=\"" + originalFilename + "\""))
                        .stream(inputStream, file.getSize(), -1)
                        .build();
                minioClient.putObject(args);
            }
        } catch (Exception e) {
            throw new StorageException("Error while storing file in Minio: " + e.getMessage());
        }

        return objectName;
    }

    public void delete(String bucketName, String serverFilename) {
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(serverFilename)
                    .build();
            minioClient.removeObject(args);
        } catch (Exception e) {
            throw new StorageException("Error while deleting " + serverFilename + " from Minio.");
        }
    }
}
