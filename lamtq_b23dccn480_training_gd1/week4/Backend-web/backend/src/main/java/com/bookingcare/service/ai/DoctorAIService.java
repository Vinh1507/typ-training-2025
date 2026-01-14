package com.bookingcare.service.ai;

import com.bookingcare.entity.ClinicEntity;
import com.bookingcare.entity.DoctorEntity;
import com.bookingcare.repository.ClinicRepository;
import com.bookingcare.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorAIService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    // lấy thông tin 1 bác sĩ
    public String getDoctorInfor(DoctorEntity doctor){
        return String.format(
                "Bác sĩ: %s, chuyên khoa: %s, phòng khám: %s, SĐT: %s, mô tả: %s",
                doctor.getFullName(),
                doctor.getSpecial().getName(),
                doctor.getClinic().getName(),
                doctor.getPhone(),
                doctor.getSpecial().getDescription()
        );
    }

    // lấy toàn bộ các bác sĩ
    public String getAllDoctorInfor(){
        List<DoctorEntity> doctors = doctorRepository.findAllByIsDeletedFalse();
        if(doctors.isEmpty()){
            return "Không có bác sĩ nào trong danh sách";
        }
        return doctors
                .stream()
                .map(this::getDoctorInfor)
                .collect(Collectors.joining("\n"));
    }
}