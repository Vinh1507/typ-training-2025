package com.bookingcare.service.ai;

import com.bookingcare.entity.SpecializationEntity;
import com.bookingcare.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecializationAIService {

    @Autowired
    SpecializationRepository specializationRepository;

    String getSpecializationInfor(SpecializationEntity specializationEntity){
        return String.format(
                "Chuyên khoa: %s, mô tả: %s",
                specializationEntity.getName(),
                specializationEntity.getDescription()
        );
    }

    String getAllSpecializationInfor(){
        List<SpecializationEntity> specializations = specializationRepository.findAll();
        if(specializations.isEmpty()){
            return "Không tìm thấy chuyên khoa nào";
        }
        return specializations
                .stream()
                .map(this::getSpecializationInfor)
                .collect(Collectors.joining("\n"));
    }
}