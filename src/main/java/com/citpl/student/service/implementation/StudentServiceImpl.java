package com.citpl.student.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentServiceImpl<StudentResponseDTO, StudentRequestDTO> {

    StudentResponseDTO createStudent(StudentRequestDTO dto);
    StudentResponseDTO getStudentById(Long id);
    Page<StudentResponseDTO> getAllStudents(String search, String status, Long batchId, Pageable pageable);
    StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto);
    StudentResponseDTO toggleStatus(Long id);
    void deleteStudent(Long id);
}