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
import org.springframework.data.domain.PageImpl;
import java.util.stream.Stream;

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
        course.setStatus(dto.getStatus());
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
        course.setStatus(dto.getStatus());
        return mapToResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        courseRepository.deleteById(id);
    }

    @Override                          // ✅ added @Override
    public CourseResponseDTO updateStatus(Long id, String status) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        course.setStatus(status);
        return mapToResponse(courseRepository.save(course));
    }

    @Override
    public Page<CourseResponseDTO> getCourses(String search, String status, Pageable pageable) {
        // simple implementation: filter in-memory
        Stream<Course> stream = courseRepository.findAll().stream();
        if (search != null && !search.isEmpty()) {
            String s = search.toLowerCase();
            stream = stream.filter(c -> (c.getCourseName() != null && c.getCourseName().toLowerCase().contains(s))
                    || (c.getDepartment() != null && c.getDepartment().toLowerCase().contains(s)));
        }
        if (status != null && !status.isEmpty()) {
            String st = status.toLowerCase();
            stream = stream.filter(c -> c.getStatus() != null && c.getStatus().toLowerCase().equals(st));
        }
        List<CourseResponseDTO> filtered = stream
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<CourseResponseDTO> pageContent = start > end ? List.of() : filtered.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filtered.size());
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