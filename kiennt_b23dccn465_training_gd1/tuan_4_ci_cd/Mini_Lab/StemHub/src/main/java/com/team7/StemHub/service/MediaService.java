package com.team7.StemHub.service;

import com.team7.StemHub.exception.FileUploadException;
import com.team7.StemHub.exception.UnsupportedMediaTypeException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static com.team7.StemHub.util.ExtensionUtil.getFileExtension;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucket;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);

    public String uploadFile(MultipartFile file) {
        // 1. Resolve filename and content type
        String original = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new UnsupportedMediaTypeException("Filename is missing"))
                .toLowerCase();
        String contentType = Optional.ofNullable(file.getContentType())
                .orElseThrow(() -> new UnsupportedMediaTypeException("Content-Type is unknown"));
        // 2. Decide folder by extension
        String ext = getFileExtension(original);
        String folder = switch (ext) {
            case "jpg","jpeg","png","gif" -> "images";
            case "mp4","mov"              -> "videos";
            case "pdf","doc","docx","txt" -> "documents";
            case "zip","rar","7z"         -> "archives";
            case "mp3","wav","flac"         -> "audio";
            case "pptx", "ppt", "xlsx", "xls"    -> "presentations";
            default -> throw new UnsupportedMediaTypeException("Unsupported file type: " + contentType);
        };
        // 3. Build object key
        String key = String.format("%s/%s-%s", folder, LocalDate.now().toString(), original);
        // 4. Prepare S3 Put request
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
        // 5. Perform upload
        try {
            s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new FileUploadException("File upload to Cloudflare R2 failed", e);
        }
        return publicUrl + "/" + bucket + "/" + key;
    }

    public String uploadFile(byte[] fileBytes, String originalFilename) {
        // 1. Resolve filename and content type
        String original = Optional.ofNullable(originalFilename)
                .orElseThrow(() -> new UnsupportedMediaTypeException("Filename is missing"))
                .toLowerCase();
        // Tự suy diễn content type từ tên file (cách đơn giản)
        String contentType;
        if (original.endsWith(".jpg") || original.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (original.endsWith(".png")) {
            contentType = "image/png";
        } else {
            // Mặc định cho các loại file khác nếu cần
            contentType = "application/octet-stream";
        }
        // 2. Decide folder by extension
        String ext = getFileExtension(original);
        // Thumbnail thì luôn là 'images'
        String folder = "images";
        // 3. Build object key (sửa đổi một chút để tránh trùng tên)
        String key = String.format("%s/%s-%s", folder, System.currentTimeMillis(), original);
        // 4. Prepare S3 Put request
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
        // 5. Perform upload
        try {
            s3Client.putObject(req, RequestBody.fromBytes(fileBytes));
        } catch (Exception e) {
            // Ném lỗi runtime để báo hiệu upload thất bại
            throw new FileUploadException("File upload (bytes) to Cloudflare R2 failed", e);
        }
        // *** SỬ DỤNG 'publicUrl' (sẽ sửa ở Lỗi 2) ***
        return publicUrl + "/" + bucket + "/" + key;
    }
}