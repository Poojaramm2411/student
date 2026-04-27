package com.example.student.util;

import com.example.student.dto.Request.InstructorRequestDTO;
import com.example.student.dto.Response.InstructorResponseDTO;
import com.example.student.model.Instructor;

import org.springframework.stereotype.Component;

@Component
public class InstructorMapper {

    public Instructor toEntity(InstructorRequestDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setName(dto.getName());
        instructor.setEmail(dto.getEmail());
        instructor.setPhone(dto.getPhone());
        instructor.setSpecialization(dto.getSpecialization());
        return instructor;
    }

    public InstructorResponseDTO toDTO(Instructor instructor) {
        InstructorResponseDTO dto = new InstructorResponseDTO();
        dto.setId(instructor.getId());
        dto.setName(instructor.getName());
        dto.setEmail(instructor.getEmail());
        dto.setPhone(instructor.getPhone());
        dto.setSpecialization(instructor.getSpecialization());
        return dto;
    }
}
