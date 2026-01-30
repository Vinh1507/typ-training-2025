package ptit.edu.vn.bookshop.service.impl;

import net.coobird.thumbnailator.Thumbnails;
import ptit.edu.vn.bookshop.domain.dto.response.FileResponseDTO;
import ptit.edu.vn.bookshop.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${app.upload-file.base-path}")
    private String basePath;

    @Value("${app.domain}")
    private String domain;

    public void createUploadedFile(String folder) throws URISyntaxException, IOException {
        Path path = Paths.get(basePath+ folder+"/");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Created upload directory at: {}", path.toAbsolutePath());
        } else {
            log.info("Upload directory already exists at: {}", path.toAbsolutePath());
        }
    }

    @Override
    public FileResponseDTO storeFile(MultipartFile file, String folder) throws IOException, URISyntaxException {
        //chuẩn hóa tên avatar
        String safeName = file.getOriginalFilename();
        String extension = "";

        int dotIndex = safeName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = safeName.substring(dotIndex);
        }
        safeName = UUID.randomUUID().toString();

        // create unique fileName
        String finalName = safeName+extension;

        Path path = Paths.get(basePath, folder);
        Path filePath = path.resolve(finalName);
        Files.createDirectories(path);

        try (InputStream inputStream = file.getInputStream()) {
            Thumbnails.of(inputStream)
                .size(640, 360)
                .outputQuality(0.8f)
                .toFile(filePath.toFile());
        }
        log.info("Stored file at: {}", path.toAbsolutePath());
        FileResponseDTO response = new FileResponseDTO();
        response.setFileName(finalName);
        response.setUrlFile(domain+"/api/v1/files/" + folder + "/" + finalName);
        response.setUploadAt(Instant.now());
        return response;
    }
}
