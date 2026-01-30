package ptit.edu.vn.bookshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptit.edu.vn.bookshop.service.CloudinaryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // single file
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File không được rỗng!");
        }

        String original = file.getOriginalFilename();
        if (original == null || !original.contains(".")) {
            throw new IllegalArgumentException("Tên file không hợp lệ!");
        }
        int dotIndex = original.lastIndexOf('.');
        String baseName = original.substring(0, dotIndex);

        String publicId = baseName + "_" + UUID.randomUUID();

        Map<String, Object> options = ObjectUtils.asMap(
                "resource_type", "auto",
                "public_id", publicId,
                "overwrite", true
        );

        Map<?, ?> data = cloudinary.uploader().upload(file.getBytes(), options);

        return data.get("secure_url").toString();
    }


    // Upload multi file
    public List<String> uploadMultipleFiles(MultipartFile[] files) throws IOException {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String original = file.getOriginalFilename();
                int dotIndex = original.lastIndexOf('.');
                String baseName = original.substring(0, dotIndex);

                String publicId = baseName + "_" + UUID.randomUUID();

                Map<String, Object> options = ObjectUtils.asMap(
                        "resource_type", "auto",
                        "public_id", publicId,
                        "overwrite", true
                );

                Map<?, ?> data = cloudinary.uploader().upload(file.getBytes(), options);
                urls.add(data.get("secure_url").toString());
            }
        }
        return urls;
    }
}
