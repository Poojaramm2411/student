package com.example.student.util;

import com.example.student.dto.Request.StudentRequestDTO;
import com.example.student.dto.Response.StudentResponseDTO;
import com.example.student.model.Student;

import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
        student.setCourse(dto.getCourse());
        return student;
    }

    public StudentResponseDTO toDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());
        dto.setCourse(student.getCourse());
        return dto;
    }
}