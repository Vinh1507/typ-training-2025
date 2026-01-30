package ptit.edu.vn.bookshop.controller.file;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptit.edu.vn.bookshop.domain.dto.response.FileResponseDTO;
import ptit.edu.vn.bookshop.exception.StorageException;
import ptit.edu.vn.bookshop.service.CloudinaryService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cloudinary")
public class CloudinaryFileController {

    private final CloudinaryService cloudinaryService;

    public CloudinaryFileController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    @ApiMessage("File uploaded to Cloudinary successfully")
    public ResponseEntity<FileResponseDTO> uploadToCloudinary(
            @RequestParam("file") MultipartFile file) throws IOException, StorageException {

        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty, please try again.");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");
        boolean isValid = allowedExtensions.stream().anyMatch(ext -> fileName.toLowerCase().endsWith(ext));

        if (!isValid) {
            throw new StorageException("Invalid file type. Allowed: " + allowedExtensions);
        }
        // Upload file lên Cloudinary
        String fileUrl = cloudinaryService.uploadFile(file);
        FileResponseDTO response = new FileResponseDTO();
        response.setFileName(fileName);
        response.setUrlFile(fileUrl);
        response.setUploadAt(Instant.now());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-multiple")
    @ApiMessage("Multiple files uploaded successfully")
    public ResponseEntity<List<FileResponseDTO>> uploadMultipleToCloudinary(
            @RequestParam("files") MultipartFile[] files) throws IOException, StorageException {
        if (files == null || files.length == 0) {
            throw new StorageException("No files provided for upload.");
        }

        List<String> urls = cloudinaryService.uploadMultipleFiles(files);

        List<FileResponseDTO> responses = Arrays.stream(files)
                .map(file -> {
                    String url = urls.get(Arrays.asList(files).indexOf(file));
                    FileResponseDTO dto = new FileResponseDTO();
                    dto.setFileName(file.getOriginalFilename());
                    dto.setUrlFile(url);
                    dto.setUploadAt(Instant.now());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(responses);
    }
}
