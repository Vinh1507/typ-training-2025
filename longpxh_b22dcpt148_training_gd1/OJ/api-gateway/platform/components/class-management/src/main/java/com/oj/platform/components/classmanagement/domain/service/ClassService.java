package com.oj.platform.components.classmanagement.domain.service;

import com.oj.platform.components.classmanagement.domain.model.ClassEntity;
import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.entity.ClassJpa;
import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.entity.StudentClassJpa;
import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.entity.TeacherClassJpa;
import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.repository.ClassJpaRepository;
import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.repository.StudentClassJpaRepository;
import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.repository.TeacherClassJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassJpaRepository classRepository;
    private final TeacherClassJpaRepository teacherClassRepository;
    private final StudentClassJpaRepository studentClassRepository;

    @Transactional
    public ClassEntity createClass(ClassEntity classEntity) {
        if (classRepository.existsByCode(classEntity.getCode())) {
            throw new RuntimeException("Class code already exists: " + classEntity.getCode());
        }
        ClassJpa jpa = toJpa(classEntity);
        ClassJpa saved = classRepository.save(jpa);
        return toDomain(saved);
    }

    @Transactional(readOnly = true)
    public ClassEntity findById(Long id) {
        return classRepository.findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public ClassEntity findByCode(String code) {
        return classRepository.findByCode(code)
                .map(this::toDomain)
                .orElseThrow(() -> new RuntimeException("Class not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public List<ClassEntity> findAll() {
        return classRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<ClassEntity> findAll(org.springframework.data.domain.Pageable pageable) {
        return classRepository.findAll(pageable)
                .map(this::toDomain);
    }

    @Transactional
    public void assignTeacherToClass(Long teacherId, Long classId) {
        if (!classRepository.existsById(classId)) {
            throw new RuntimeException("Class not found with id: " + classId);
        }
        if (teacherClassRepository.existsByTeacherIdAndClassId(teacherId, classId)) {
            return; // Already assigned
        }
        TeacherClassJpa assignment = TeacherClassJpa.builder()
                .teacherId(teacherId)
                .classId(classId)
                .build();
        teacherClassRepository.save(assignment);
    }

    @Transactional
    public void assignStudentToClass(Long studentId, Long classId) {
        if (!classRepository.existsById(classId)) {
            throw new RuntimeException("Class not found with id: " + classId);
        }
        if (studentClassRepository.existsByStudentIdAndClassId(studentId, classId)) {
            return; // Already enrolled
        }
        StudentClassJpa enrollment = StudentClassJpa.builder()
                .studentId(studentId)
                .classId(classId)
                .build();
        studentClassRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public List<Long> getTeacherClassIds(Long teacherId) {
        return teacherClassRepository.findByTeacherId(teacherId).stream()
                .map(TeacherClassJpa::getClassId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Long> getStudentClassIds(Long studentId) {
        return studentClassRepository.findByStudentId(studentId).stream()
                .map(StudentClassJpa::getClassId)
                .collect(Collectors.toList());
    }

    private ClassEntity toDomain(ClassJpa jpa) {
        return ClassEntity.builder()
                .id(jpa.getId())
                .name(jpa.getName())
                .code(jpa.getCode())
                .description(jpa.getDescription())
                .semester(jpa.getSemester())
                .academicYear(jpa.getAcademicYear())
                .createdAt(jpa.getCreatedAt())
                .updatedAt(jpa.getUpdatedAt())
                .build();
    }

    private ClassJpa toJpa(ClassEntity domain) {
        return ClassJpa.builder()
                .id(domain.getId())
                .name(domain.getName())
                .code(domain.getCode())
                .description(domain.getDescription())
                .semester(domain.getSemester())
                .academicYear(domain.getAcademicYear())
                .build();
    }
}
