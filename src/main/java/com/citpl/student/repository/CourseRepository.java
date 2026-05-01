package com.citpl.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citpl.student.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE " +
            "(:search IS NULL OR LOWER(c.courseName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:level IS NULL OR c.courseCode = :level)")
    Page<Course> findByFilters(@Param("search") String search,
            @Param("level") String level,
            Pageable pageable);
}
