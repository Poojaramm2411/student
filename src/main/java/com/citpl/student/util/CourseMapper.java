package com.citpl.student.util;

import org.springframework.stereotype.Component;

import com.citpl.student.dto.Request.CourseRequestDTO;
import com.citpl.student.dto.Response.CourseResponseDTO;
import com.citpl.student.model.Course;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequestDTO dto) {

        Course course = new Course();

        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setDuration(dto.getDuration());
        course.setLevel(dto.getLevel());

        return course;
    }

    public CourseResponseDTO toDTO(Course course) {

        CourseResponseDTO dto = new CourseResponseDTO();

        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDuration(course.getDuration());
        dto.setLevel(course.getLevel());

        return dto;
    }
}