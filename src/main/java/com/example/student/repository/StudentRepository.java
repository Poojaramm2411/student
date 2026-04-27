package com.example.student.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.student.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name, String email, Pageable pageable);

    
    Page<Student> findByAge(Integer age, Pageable pageable);

    Page<Student> findByNameContainingIgnoreCaseAndAge(
            String name, Integer age, Pageable pageable);
    
}
