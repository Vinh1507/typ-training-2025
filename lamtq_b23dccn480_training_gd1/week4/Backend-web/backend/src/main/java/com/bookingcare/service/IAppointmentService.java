package com.bookingcare.service;

import com.bookingcare.DTO.AppointmentRequestDTO;
import com.bookingcare.DTO.AppointmentResponseDTO;
import com.bookingcare.DTO.BookingDTO;

import java.util.List;

public interface IAppointmentService {
    void createAppointment(BookingDTO dto, String currentUsername);
    List<AppointmentResponseDTO> getAllBookings();
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO appointmentRequestDTO);
    List<AppointmentResponseDTO> getAppointmentByUsername(String email);
    void deleteAppointment(Long id, String username);
    List<AppointmentResponseDTO> getAppointmentByDoctorId(Long id);
    void updateBookingStatus(Long id);
}