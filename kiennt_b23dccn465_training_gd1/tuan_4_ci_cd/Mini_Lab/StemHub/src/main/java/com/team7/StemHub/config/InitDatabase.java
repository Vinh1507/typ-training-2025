package com.team7.StemHub.config;

import com.team7.StemHub.dao.CourseRepo;
import com.team7.StemHub.model.Course;
import com.team7.StemHub.service.CourseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class InitDatabase {
    @Bean
    @Order(1)
    CommandLineRunner initCourse(CourseRepo courseRepo) {
        return args -> {
            if (courseRepo.count() == 0) {
                courseRepo.saveAll(List.of(
                        Course.builder().courseId("ALL_STEAM").courseName("STEM").otherName("Tổng hợp, tong hop, stem, steam").build(),
                        Course.builder().courseId("STEAM_M01").courseName("Toán học").otherName("Toan hoc, Math, Toan").build(),
                        Course.builder().courseId("STEAM_S02").courseName("Khoa học").otherName("Khoa hoc, KHTN, Science, Natural Science").build(),
                        Course.builder().courseId("STEAM_S03").courseName("Lịch sử và Địa lý").otherName("Lich su va Dia ly, LSDI, History, Geography, KHXH").build(),
                        Course.builder().courseId("STEAM_S01").courseName("Tự nhiên và Xã hội").otherName("Tu nhien va Xa hoi, TNXH, Nature, Society").build(),
                        Course.builder().courseId("STEAM_T01").courseName("Tin học").otherName("Tin hoc, ICT, Informatics").build(),
                        Course.builder().courseId("STEAM_E01").courseName("Công nghệ").otherName("Cong nghe, Technology").build(),
                        Course.builder().courseId("STEAM_A01").courseName("Âm nhạc").otherName("Am nhac, Hat nhac, Music").build(),
                        Course.builder().courseId("STEAM_A02").courseName("Mỹ thuật").otherName("My thuat, Ve, Art").build(),
                        Course.builder().courseId("THE_OTHER").courseName("Khác").otherName("").build()
                ));
            }
        };
    }
}
