package com.bookingcare.service.payment;

import com.bookingcare.DTO.PaymentConfirmRequestDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QRCodeService {

    // tạo qr cho payment
    public String generateQRCode(String data, int width, int height) {
        try{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            // chuyển dữ liệu thành ma trận
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // chuển ma trận thành ảnh png
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);

            byte qrBytes[] = outputStream.toByteArray();
            // chuyển tất cả byte của ảnh thành chuỗi Base64
            String base64Image = Base64.getEncoder().encodeToString(qrBytes);

            return "data:image/png;base64," + base64Image;
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // tạo nội dung cho payment
    public String generatePaymentQRData(String transactionId, Double amount, String description) {
        return String.format(
                "Ma thanh toan: %s \nSs tien phai tra: %.0f \nMo ta: %s",
                transactionId, amount, description
        );
    }

    // tạo url cho payment
    public String generatePaymentQRUrl(String transactionId, String bareUrl){
        return bareUrl + "/payment/process?transactionId=" + transactionId;
    }
}