package com.citpl.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.citpl.student.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name, String email, Pageable pageable);

    Page<Student> findByAge(Integer age, Pageable pageable);

    Page<Student> findByNameContainingIgnoreCaseAndAge(
            String name, Integer age, Pageable pageable);

    // MUST have @Query annotation
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "CAST(s.id AS string) LIKE CONCAT('%', :search, '%')")
    Page<Student> searchStudents(@Param("search") String search, Pageable pageable);
}    