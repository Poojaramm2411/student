package com.example.student.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.student.dto.Request.InstructorRequestDTO;
import com.example.student.dto.Response.InstructorResponseDTO;
import com.example.student.model.Instructor;
import com.example.student.repository.InstructorRepository;
import com.example.student.service.InstructorService;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public InstructorResponseDTO createInstructor(InstructorRequestDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setName(dto.getName());
        instructor.setEmail(dto.getEmail());
        instructor.setPhone(dto.getPhone());
        instructor.setSpecialization(dto.getSpecialization());
        instructor.setExperience(dto.getExperience());
        return mapToResponse(instructorRepository.save(instructor));
    }

    @Override
    public List<InstructorResponseDTO> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorResponseDTO getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
        return mapToResponse(instructor);
    }

    @Override
    public InstructorResponseDTO updateInstructor(Long id, InstructorRequestDTO dto) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
        instructor.setName(dto.getName());
        instructor.setEmail(dto.getEmail());
        instructor.setPhone(dto.getPhone());
        instructor.setSpecialization(dto.getSpecialization());
        instructor.setExperience(dto.getExperience());
        return mapToResponse(instructorRepository.save(instructor));
    }

    @Override
    public void deleteInstructor(Long id) {
        instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
        instructorRepository.deleteById(id);
    }

    @Override
    public Page<InstructorResponseDTO> getInstructors(String search, String specialization, Pageable pageable) {
        return instructorRepository.findByFilters(search, specialization, pageable)
                .map(this::mapToResponse);
    }

    private InstructorResponseDTO mapToResponse(Instructor instructor) {
        InstructorResponseDTO dto = new InstructorResponseDTO();
        dto.setId(instructor.getId());
        dto.setName(instructor.getName());
        dto.setEmail(instructor.getEmail());
        dto.setPhone(instructor.getPhone());
        dto.setSpecialization(instructor.getSpecialization());
        dto.setExperience(instructor.getExperience());
        return dto;
    }
}