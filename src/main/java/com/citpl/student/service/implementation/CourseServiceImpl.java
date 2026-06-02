package com.citpl.student.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseServiceImpl<CourseResponseDTO, CourseRequestDTO> {

    CourseResponseDTO createCourse(CourseRequestDTO dto);
    CourseResponseDTO getCourseById(Long id);
    Page<CourseResponseDTO> getAllCourses(String search, String status, Long batchId, Pageable pageable);
    CourseResponseDTO updateCourse(Long id, CourseRequestDTO dto);
    CourseResponseDTO toggleStatus(Long id);
    void deleteCourse(Long id);
}