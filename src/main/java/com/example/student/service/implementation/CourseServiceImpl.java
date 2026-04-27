package com.example.student.service.implementation;

import com.example.student.dto.Request.CourseRequestDTO;
import com.example.student.dto.Response.CourseResponseDTO;
import com.example.student.model.Course;
import com.example.student.repository.CourseRepository;
import com.example.student.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setDuration(dto.getDuration());
        course.setLevel(dto.getLevel());
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
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setDuration(dto.getDuration());
        course.setLevel(dto.getLevel());
        return mapToResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        courseRepository.deleteById(id);
    }

    @Override
    public Page<CourseResponseDTO> getCourses(String search, String level, Pageable pageable) {
        return courseRepository.findByFilters(search, level, pageable)
                .map(this::mapToResponse);
    }

    private CourseResponseDTO mapToResponse(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDuration(course.getDuration());
        dto.setLevel(course.getLevel());
        return dto;
    }
}
