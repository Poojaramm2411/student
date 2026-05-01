package com.citpl.student.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;    
import org.springframework.web.bind.annotation.RestController;

import com.citpl.student.dto.Request.InstructorRequestDTO;
import com.citpl.student.dto.Response.InstructorResponseDTO;
import com.citpl.student.service.InstructorService;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public Page<InstructorResponseDTO> getInstructors(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String specialization,
            Pageable pageable) {
        return instructorService.getInstructors(search, specialization, pageable);
    }

    @GetMapping("/{id}")
    public InstructorResponseDTO getInstructorById(@PathVariable Long id) {
        return instructorService.getInstructorById(id);
    }

    @PostMapping
    public InstructorResponseDTO createInstructor(@RequestBody InstructorRequestDTO dto) {
        return instructorService.createInstructor(dto);
    }

    @PutMapping("/{id}")
    public InstructorResponseDTO updateInstructor(@PathVariable Long id,
            @RequestBody InstructorRequestDTO dto) {
        return instructorService.updateInstructor(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
    }
}