package com.bookingcare.service.ai;

import com.bookingcare.entity.ClinicEntity;
import com.bookingcare.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClinicAIService {

    @Autowired
    private ClinicRepository clinicRepository;

    public String getClinicInfor(ClinicEntity clinic){
        return String.format("Phòng khám: %s, SĐT: %s, email: %s, mô tả: %s",
                clinic.getName(),
                clinic.getPhoneNumber(),
                clinic.getEmail(),
                clinic.getDescription()
        );
    }

    public String getAllClinicInfor(){
        List<ClinicEntity> clinics = clinicRepository.findAll();
        if(clinics.isEmpty()){
            return "Không có phòng khám nào trong danh sách";
        }
        return clinics
                .stream()
                .map(this::getClinicInfor)
                .collect(Collectors.joining("\n"));
    }
}