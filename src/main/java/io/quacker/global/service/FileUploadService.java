package io.quacker.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID() + "-" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(bucketName, uniqueFilename, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new CustomException("이미지 업로드 실패", 500);
        }

        return amazonS3.getUrl(bucketName, uniqueFilename).toString();
    }

    public void deleteFromS3(String imageUrl) {
        try {
            String key = extractKeyFromUrl(imageUrl); // 버킷 내부 key 추출
            amazonS3.deleteObject(bucketName, key);
        } catch (Exception e) {
            throw new CustomException("S3 이미지 삭제 실패", 500);
        }
    }

    private String extractKeyFromUrl(String url) {
        return url.substring(url.indexOf(".com/") + 5); // .com/ 다음부터 key로 간주
    }
}
