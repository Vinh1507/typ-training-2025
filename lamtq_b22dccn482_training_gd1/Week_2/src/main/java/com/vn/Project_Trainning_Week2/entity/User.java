package com.vn.Project_Trainning_Week2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.Project_Trainning_Week2.constant.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public User(String name, String address, Gender gender) {
        this.name = name;
        this.address = address;
        this.gender = gender;
    }

    public User() {

    }
}
