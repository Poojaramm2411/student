package com.citpl.student.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.citpl.student.dto.Request.InstructorRequestDTO;
import com.citpl.student.dto.Response.InstructorResponseDTO;

public interface InstructorService {

    InstructorResponseDTO createInstructor(InstructorRequestDTO dto);

    List<InstructorResponseDTO> getAllInstructors();

    InstructorResponseDTO getInstructorById(Long id);

    InstructorResponseDTO updateInstructor(Long id, InstructorRequestDTO dto);

    void deleteInstructor(Long id);

    Page<InstructorResponseDTO> getInstructors(String search, String specialization, Pageable pageable);
}