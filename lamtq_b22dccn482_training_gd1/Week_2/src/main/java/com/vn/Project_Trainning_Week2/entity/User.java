package com.vn.Project_Trainning_Week2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.Project_Trainning_Week2.constant.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String name;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Gender gender;

    public User(Long id, String name, String address, Gender gender) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.gender = gender;
    }
}
