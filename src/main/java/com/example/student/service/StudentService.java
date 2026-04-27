package com.example.student.service;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;

import com.example.student.dto.Request.StudentRequestDTO;
import com.example.student.dto.Response.StudentResponseDTO;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO dto);

    List<StudentResponseDTO> getAllStudents();

    StudentResponseDTO getStudentById(Long id);

    StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto);

    void deleteStudent(Long id);

    Page<StudentResponseDTO> getStudents(String search, Integer age, Pageable pageable);

    Page<StudentResponseDTO> getStudents(String search, Integer age, org.springframework.data.domain.Pageable pageable);
}