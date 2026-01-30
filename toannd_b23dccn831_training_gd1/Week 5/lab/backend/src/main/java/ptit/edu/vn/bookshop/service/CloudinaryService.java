package ptit.edu.vn.bookshop.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CloudinaryService {
    String uploadFile(MultipartFile file) throws IOException;
    List<String> uploadMultipleFiles(MultipartFile[] files) throws IOException;
}
