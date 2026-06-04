package com.citpl.student.util;

import org.springframework.stereotype.Component;

import com.citpl.student.dto.Request.CourseRequestDTO;
import com.citpl.student.dto.Response.CourseResponseDTO;
import com.citpl.student.model.Course;
import com.citpl.student.model.Status;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequestDTO dto) {

        Course course = new Course();

        course.setCourseName(dto.getCourseName());
        course.setDepartment(dto.getDepartment());
        course.setDuration(dto.getDuration());
        course.setStatus(Status.valueOf(dto.getStatus())); // ← fixed typo (was getStatus  ())

        return course;
    }

    public CourseResponseDTO toDTO(Course course) {

        CourseResponseDTO dto = new CourseResponseDTO();

        dto.setId(course.getId());
        dto.setCourseName(course.getCourseName());
        dto.setDepartment(course.getDepartment());
        dto.setDuration(course.getDuration());
        dto.setStatus(course.getStatus().name()); // ← Enum to String

        // batch info (avoid null pointer)
        if (course.getBatch() != null) {
            dto.setBatchId(course.getBatch().getId());
            dto.setBatchName(course.getBatch().getBatchName());
        }

        return dto;
    }
}