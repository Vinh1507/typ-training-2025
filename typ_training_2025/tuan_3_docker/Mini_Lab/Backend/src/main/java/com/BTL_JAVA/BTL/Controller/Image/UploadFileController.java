package com.BTL_JAVA.BTL.Controller.Image;

import com.BTL_JAVA.BTL.Service.Cloudinary.UploadImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadFileController {
    private final UploadImageFile uploadImageFile;

    @PostMapping("/image")
    public String uploadImage(@RequestParam("file")MultipartFile file) throws IOException {
         String urlImage=uploadImageFile.uploadImage(file);
         return urlImage;
    }
}
