package com.bookingcare.service.payment;

import com.bookingcare.DTO.PaymentConfirmRequestDTO;
import com.bookingcare.DTO.PaymentQRRequestDTO;
import com.bookingcare.DTO.PaymentQRResponseDTO;
import com.bookingcare.entity.AppointmentEntity;
import com.bookingcare.enums.Status;
import com.bookingcare.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final AppointmentRepository appointmentRepository;
    private final QRCodeService  qrCodeService;

    private static final String BASE_URL = "http://localhost:5173";
    private static final long QR_EXPIRE_SECONDS = 30;

    //tạo qr thanh toán
    public PaymentQRResponseDTO generatePaymentQR(PaymentQRRequestDTO request){
        AppointmentEntity appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if(appointment.getStatus() == Status.DA_THANH_TOAN){
            throw new RuntimeException("Lịch hẹn này đã được thanh toán");
        }

        String transactionId = "TXN" + System.currentTimeMillis() +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        appointment.setTransactionId(transactionId);
        appointmentRepository.save(appointment);

        String description = String.format("Thanh toan kham benh - Ma lich hen #%d", appointment.getId());
        String qrData = qrCodeService.generatePaymentQRData(transactionId, request.amount(), description);
        String qrCodeBase64 = qrCodeService.generateQRCode(qrData, 300, 300);
        String paymentUrl = qrCodeService.generatePaymentQRUrl(transactionId, BASE_URL);

        return PaymentQRResponseDTO.builder()
                .appointmentId(appointment.getId())
                .transactionId(transactionId)
                .amount(request.amount())
                .qrCode(qrCodeBase64)
                .paymentUrl(paymentUrl)
                .description(description)
                .expiresIn(QR_EXPIRE_SECONDS)
                .build();
    }

    //xác nhận thanh toán
    @Transactional
    public boolean confirmPayment(PaymentConfirmRequestDTO request){
        System.out.println("Đã thanh toán");

        AppointmentEntity appointment = appointmentRepository.findByTransactionId(request.transactionId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if(request.isSuccess()){
            appointment.setStatus(Status.DA_THANH_TOAN);
            appointment.setPaymentTime(LocalDateTime.now());
            appointmentRepository.save(appointment);
            return true;
        }
        return false;
    }

    // kiểm tra thanh toán
    public boolean isPaid(Long appointmentId){
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        return appointment.getStatus() == Status.DA_THANH_TOAN;
    }

    // lấy status hiện tại
    public Status getPaymentStatus(Long appointmentId){
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));
        return appointment.getStatus();
    }
}