package ptit.edu.vn.bookshop.service;

import org.springframework.web.multipart.MultipartFile;
import ptit.edu.vn.bookshop.domain.dto.response.FileResponseDTO;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {
    void createUploadedFile(String folder) throws URISyntaxException, IOException;

    FileResponseDTO storeFile(MultipartFile file, String folder) throws IOException, URISyntaxException;
}
