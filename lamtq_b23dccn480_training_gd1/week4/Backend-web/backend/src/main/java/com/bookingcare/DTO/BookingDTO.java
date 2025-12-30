package com.bookingcare.DTO;

import com.bookingcare.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDTO {

    @JsonProperty("name")
    @NotBlank(message = "Tên bệnh nhân là bắt buộc")
    private String name;

    @JsonProperty("email")
    @NotBlank(message = "Email bệnh nhân là bắt buộc")
    private String email;

    @JsonProperty("phone")
    @NotBlank(message = "Số điện thoại bệnh nhân là bắt buộc")
    private String phone;

    @JsonProperty("date")
    @NotBlank(message = "Ngày khám là bắt buộc")
    private String date; // FE gửi dạng "yyyy-MM-dd"

    @JsonProperty("time")
    @NotBlank(message = "Giờ khám là bắt buộc")
    private String time;

    @JsonProperty("note")
    private String note;

    @JsonProperty("namsinh")
    private String namSinh;

    @JsonProperty("gioitinh")
    @Enumerated(EnumType.STRING)
    private Gender gender;


    @JsonProperty("thanhpho")
    private String thanhPho;

    @JsonProperty("xahuyen")
    private String xaHuyen;

    @JsonProperty("diachi")
    private String diaChi;

    @JsonProperty("diachikham")
    private String diaChiKham;

    @JsonProperty("bacsi")
    private String bacsi;

    @JsonProperty("idbacsi")
    private Long idBacSi;

    @JsonProperty("lydokham")
    private String lyDoKham;

    @JsonProperty("nguoidatlich_ten")
    private String nguoiDatLichTen;

    @JsonProperty("nguoidatlich_sdt")
    private String nguoiDatLichSDT;

    @JsonProperty("emailUser")
    private String emailUser;
}
