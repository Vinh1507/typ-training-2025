package com.bookingcare.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private String position;         // Chức vụ (VD: PGS.TS, BSCKII
    private String experience;       // Mô tả kinh nghiệm (VD: "35 năm kinh nghiệm về tim mạch")
    private String note;             // Ghi chú thêm (VD: "Bác sĩ nhận khám từ 7 tuổi trở lên")
    private String clinicName;
    private String clinicAddress;
    private Double price;
    private String imageUrl;
}
