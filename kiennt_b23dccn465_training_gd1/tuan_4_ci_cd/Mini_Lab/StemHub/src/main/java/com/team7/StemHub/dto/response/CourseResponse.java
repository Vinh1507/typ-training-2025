package com.team7.StemHub.dto.response;

import com.team7.StemHub.model.Course;
import lombok.Data;
import lombok.Getter;

@Getter
public class CourseResponse {
    private final String courseId;
    private final String courseName;

    public CourseResponse(Course course) {
        this.courseId = course.getCourseId();
        this.courseName = course.getCourseName();
    }
}
