package com.BTL_JAVA.BTL.Service.Cloudinary.impl;

import com.BTL_JAVA.BTL.Service.Cloudinary.UploadImageFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UploadImageFileImpl implements UploadImageFile {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        String publicValue=generatePublicValue(file.getOriginalFilename());
        String extension=getFileName(file.getOriginalFilename())[1];
        File fileUpload=convert(file);
//        cloudinary.uploader().upload( fileUpload, ObjectUtils.asMap("folder", "my-app/images/products","public_id", publicValue));
//        String filePath=cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));
//        cleanDisk(fileUpload);
//        return filePath;
        @SuppressWarnings("unchecked")
        var res = (java.util.Map<String, Object>) cloudinary.uploader().upload(
                fileUpload,
                ObjectUtils.asMap(
                        "folder", "my-app/images/products",
                        "public_id", publicValue,
                        "resource_type", "image",
                        "overwrite", true
                )
        );
// Dùng secure_url: đã có https + đúng folder + đúng đuôi
        String filePath = (String) res.get("secure_url");
// Nếu cần dùng sau này để xoá/thay ảnh: String publicIdFull = (String) res.get("public_id");

        cleanDisk(fileUpload);
        return filePath;


    }

    private File convert(MultipartFile file) {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()),getFileName(file.getOriginalFilename())[1]));
        try(InputStream is=file.getInputStream()){
            Files.copy(is,convFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try{
            Path path = file.toPath();
            Files.delete(path);
        }catch (IOException e){
        }
    }


    public String generatePublicValue(String originalName){
           String fileName=getFileName(originalName)[0];
           return StringUtils.join(UUID.randomUUID().toString(),"_",fileName);
    }
    public String[] getFileName(String originalName){
        return originalName.split("\\.");
    }
}
