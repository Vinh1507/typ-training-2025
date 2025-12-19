package com.week2.phan2.db.repository;

import com.week2.phan2.db.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findById(Long id);

}
