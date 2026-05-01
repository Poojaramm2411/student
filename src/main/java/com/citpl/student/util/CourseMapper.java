package com.citpl.student.util;

import org.springframework.stereotype.Component;

import com.citpl.student.dto.Request.CourseRequestDTO;
import com.citpl.student.dto.Response.CourseResponseDTO;
import com.citpl.student.model.Course;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequestDTO dto) {
        Course course = new Course();
        course.setCourseName(dto.getCourseName());
        course.setCourseCode(dto.getCourseCode());
        course.setDuration(dto.getDuration());
        course.setDescription(dto.getDescription());
        return course;
    }

    public CourseResponseDTO toDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setCourseName(course.getCourseName());
        dto.setCourseCode(course.getCourseCode());
        dto.setDuration(course.getDuration());
        dto.setDescription(course.getDescription());
        return dto;
    }
}