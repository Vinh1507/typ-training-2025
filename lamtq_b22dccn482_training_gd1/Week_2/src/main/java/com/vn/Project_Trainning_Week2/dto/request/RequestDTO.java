package com.vn.Project_Trainning_Week2.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.Project_Trainning_Week2.constant.Gender;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestDTO {
    private String name;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Gender gender;
}
