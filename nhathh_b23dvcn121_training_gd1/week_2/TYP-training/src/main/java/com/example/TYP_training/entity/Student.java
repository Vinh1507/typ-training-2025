package com.example.TYP_training.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private String code;
    private String fullName;
    private String phoneNumber;
    private String homeTown;

    @Override
    public String toString() {
        return "Student{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", fullName='" + fullName + '\'' +
            ", phoneNumber=" + phoneNumber +
            ", homeTown='" + homeTown + '\'' +
            '}';
    }
}
