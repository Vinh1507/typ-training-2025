package com.bookingcare.converter;

import com.bookingcare.DTO.DoctorDTO;
import com.bookingcare.entity.DoctorEntity;
import org.springframework.stereotype.Component;

@Component
public class ConvertDoctorEntityToDetail {
    public DoctorDTO convert(DoctorEntity doctorEntity) {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctorEntity.getId());
        doctorDTO.setName(doctorEntity.getFullName());
        doctorDTO.setSpecialization(doctorEntity.getSpecial().getName());
        doctorDTO.setPosition("PGS.TS, BSCKII");
        doctorDTO.setExperience("Bác sĩ có 35 năm kinh nghiệm về vực " + doctorEntity.getSpecial().getName() +". "+
                "Phó chủ tịch hội " +doctorEntity.getSpecial().getName() +" Việt Nam");
        doctorDTO.setNote("Bác sĩ nhận khám từ 7 tuổi trở lên");
        doctorDTO.setClinicName(doctorEntity.getClinic().getName());
        doctorDTO.setClinicAddress(doctorEntity.getClinic().getDescription());
        doctorDTO.setPrice(doctorEntity.getPrice());
        return doctorDTO;
    }
}
