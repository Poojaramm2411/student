package com.example.student.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.student.dto.Request.StudentRequestDTO;
import com.example.student.dto.Response.StudentResponseDTO;
import com.example.student.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET ALL WITH SEARCH & PAGINATION (handles all GET /api/students cases)
    @GetMapping
    public Page<StudentResponseDTO> getStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer age,
            Pageable pageable) {
        return studentService.getStudents(search, age, pageable);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // CREATE
    @PostMapping
    public StudentResponseDTO createStudent(@RequestBody StudentRequestDTO dto) {
        return studentService.createStudent(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(@PathVariable Long id,
                                            @RequestBody StudentRequestDTO dto) {
        return studentService.updateStudent(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}