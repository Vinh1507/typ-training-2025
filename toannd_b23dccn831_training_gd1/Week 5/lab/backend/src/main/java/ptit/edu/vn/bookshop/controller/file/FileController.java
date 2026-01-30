package ptit.edu.vn.bookshop.controller.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ptit.edu.vn.bookshop.domain.dto.response.FileResponseDTO;
import ptit.edu.vn.bookshop.exception.StorageException;
import ptit.edu.vn.bookshop.service.FileService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

import java.util.Arrays;
import java.util.List;
import java.io.FileNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${app.upload-file.base-path}")
    private String basePath;

    @PostMapping("/files")
    @ApiMessage("File uploaded successfully")
    public ResponseEntity<FileResponseDTO> uploadFile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder) throws URISyntaxException, IOException, StorageException {

        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty, please try again.");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(extension -> fileName.toLowerCase().endsWith(extension));

        if (!isValid) {
            throw new StorageException("Invalid file allow only " + allowedExtensions.toString());
        }
        // create folder
        this.fileService.createUploadedFile(folder);
        // store file in folder
        FileResponseDTO fileResponseDTO = this.fileService.storeFile(file, folder);
        return ResponseEntity.ok().body(fileResponseDTO);
    }

    @GetMapping("/files/{folder}/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String folder, @PathVariable String filename) throws IOException {
        Path file = Paths.get(basePath, folder, filename);
        Resource resource = new UrlResource(file.toUri());
        String contentType = Files.probeContentType(file);
        if (contentType == null) {
            contentType = "application/octet-stream"; // fallback
        }
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new FileNotFoundException("Could not read file: " + filename);
        }
    }
}
