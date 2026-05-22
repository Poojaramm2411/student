package com.citpl.student.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.citpl.student.dto.Request.StudentRequestDTO;
import com.citpl.student.dto.Response.StudentResponseDTO;
import com.citpl.student.model.Student;
import com.citpl.student.repository.StudentRepository;
import com.citpl.student.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        Student student = toEntity(dto);
        Student saved = studentRepository.save(student);
        return toDTO(saved);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return toDTO(student);
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setName(dto.getName());
        student.setEmail(dto.getEmail()); 
        student.setStudentCode(dto.getStudentCode());
        student.setAge(dto.getAge());
        Student updated = studentRepository.save(student);
        return toDTO(updated);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.delete(student);
    }

    @Override
    public Page<StudentResponseDTO> getStudents(String search, Integer age, Pageable pageable) {
        Page<Student> students;

        if (search != null && age != null) {
            students = studentRepository.findByNameContainingIgnoreCaseAndAge(search, age, pageable);
        } else if (search != null) {
            students = studentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else if (age != null) {
            students = studentRepository.findByAge(age, pageable);
        } else {
            students = studentRepository.findAll(pageable);
        }

        return students.map(this::toDTO);
    }

    private Student toEntity(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
        student.setStudentCode(dto.getStudentCode());
        return student;
    }

    private StudentResponseDTO toDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());
        dto.setStudentCode(student.getStudentCode());
        return dto;
    }
}