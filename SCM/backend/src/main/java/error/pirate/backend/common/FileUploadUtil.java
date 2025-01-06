package error.pirate.backend.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileUploadUtil {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3 업로드 및 확장자 검증을 한 번에 처리하는 메서드
    public String uploadFile(MultipartFile file) throws IOException {
        // 파일 확장자 검증
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        // 파일 확장자 추출 및 검증
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExtension.equals("jpg") && !fileExtension.equals("png")) {
            throw new IllegalArgumentException("Only JPG and PNG files are allowed.");
        }

        // 파일명에 타임스탬프 추가
        fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3에 파일 업로드
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata);
        amazonS3Client.putObject(putObjectRequest);

        // S3에 저장된 파일의 URL을 반환
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public String uploadImagePath(String base64Image) {
        try {
            String fileName = "sign_" + System.currentTimeMillis() + ".jpg";

            String base64Data = base64Image.split(",")[1]; // Base64 데이터 분리
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Data);

            amazonS3Client.putObject(bucket, fileName, new ByteArrayInputStream(decodedBytes), null);

            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

}

