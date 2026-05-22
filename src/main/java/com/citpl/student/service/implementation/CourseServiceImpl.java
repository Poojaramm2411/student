package com.citpl.student.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.citpl.student.dto.Request.CourseRequestDTO;
import com.citpl.student.dto.Response.CourseResponseDTO;
import com.citpl.student.model.Course;
import com.citpl.student.repository.CourseRepository;
import com.citpl.student.service.CourseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO dto) {
        Course course = new Course();
        course.setCourseName(dto.getCourseName());
        course.setDepartment(dto.getDepartment());
        course.setDuration(dto.getDuration());
        return mapToResponse(courseRepository.save(course));
    }

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return mapToResponse(course);
    }

    @Override
    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        course.setCourseName(dto.getCourseName());
        course.setDepartment(dto.getDepartment());
        course.setDuration(dto.getDuration());
        // status not present on CourseRequestDTO; preserve existing status
        return mapToResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        courseRepository.deleteById(id);
    }

    @Override
    public Page<CourseResponseDTO> getCourses(String search, String status, Pageable pageable) {
        return courseRepository.findByFilters(search, status, pageable)
                .map(this::mapToResponse);
    }

    private CourseResponseDTO mapToResponse(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setCourseName(course.getCourseName());
        dto.setDepartment(course.getDepartment());
        dto.setDuration(course.getDuration());
        dto.setStatus(course.getStatus());
        return dto;
    }
}
