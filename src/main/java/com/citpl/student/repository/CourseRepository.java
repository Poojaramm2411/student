package com.citpl.student.repository;

import com.citpl.student.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE " +
           "(:search IS NULL OR LOWER(c.courseName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(c.department) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:status IS NULL OR c.status = :status)")
    Page<Course> findByFilters(@Param("search") String search, 
                               @Param("status") String status, 
                               Pageable pageable);
}