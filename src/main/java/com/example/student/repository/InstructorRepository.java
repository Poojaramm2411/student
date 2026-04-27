package com.example.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.student.model.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Query("SELECT i FROM Instructor i WHERE " +
            "(:search IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(i.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:specialization IS NULL OR i.specialization = :specialization)")
    Page<Instructor> findByFilters(@Param("search") String search,
            @Param("specialization") String specialization,
            Pageable pageable);
}