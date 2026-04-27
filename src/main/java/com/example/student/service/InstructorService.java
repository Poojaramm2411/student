package com.example.student.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.student.dto.Request.InstructorRequestDTO;
import com.example.student.dto.Response.InstructorResponseDTO;

public interface InstructorService {

    InstructorResponseDTO createInstructor(InstructorRequestDTO dto);

    List<InstructorResponseDTO> getAllInstructors();

    InstructorResponseDTO getInstructorById(Long id);

    InstructorResponseDTO updateInstructor(Long id, InstructorRequestDTO dto);

    void deleteInstructor(Long id);

    Page<InstructorResponseDTO> getInstructors(String search, String specialization, Pageable pageable);
}