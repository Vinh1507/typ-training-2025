package com.team7.StemHub.util;

import com.team7.StemHub.service.MediaService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ThumbnailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);

    public static byte[] createThumbnailFromPdf(MultipartFile pdfFile) throws IOException {
        // 1. Tải file PDF từ MultipartFile vào bộ nhớ
        try (PDDocument document = Loader.loadPDF(pdfFile.getBytes())) {
            // 2. Tạo một đối tượng renderer
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            // 3. Render trang đầu tiên với scale nhỏ hơn để tránh overflow (1.5 thay vì 150)
            BufferedImage image = pdfRenderer.renderImage(0, 1.5f);
            // 4. Chuyển BufferedImage thành byte[] (định dạng JPG)
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                // Ghi ảnh vào stream, định dạng "jpg"
                ImageIO.write(image, "jpg", baos);
                // Hoàn tất và lấy mảng byte
                baos.flush();
                return baos.toByteArray();
            }
        } catch (IOException e) {
            logger.error("Failed to create thumbnail from PDF: {}", e.getMessage());
            throw e;
        }
    }
}
