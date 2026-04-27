package com.example.student.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.student.dto.Request.StudentRequestDTO;
import com.example.student.dto.Response.StudentResponseDTO;
import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;
import com.example.student.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CREATE
    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        Student student = toEntity(dto);
        Student saved = studentRepository.save(student);
        return toDTO(saved);
    }

    // GET ALL
    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // GET BY ID
    @Override
    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return toDTO(student);
    }

    // UPDATE
    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setCourse(dto.getCourse());
        student.setAge(dto.getAge());

        Student updated = studentRepository.save(student);
        return toDTO(updated);
    }

    // DELETE
    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.delete(student);
    }

    // GET WITH SEARCH & PAGINATION
    @Override
    public Page<StudentResponseDTO> getStudents(String search, Integer age, Pageable pageable) {

        Page<Student> students;

        if (search != null && age != null) {
            students = studentRepository
                    .findByNameContainingIgnoreCaseAndAge(search, age, pageable);

        } else if (search != null) {
            students = studentRepository
                    .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);

        } else if (age != null) {
            students = studentRepository.findByAge(age, pageable);

        } else {
            students = studentRepository.findAll(pageable); // ✅ Fixed: lowercase instance call
        }

        return students.map(this::toDTO); // ✅ Reusing toDTO mapper
    }

    // MAPPER METHODS
    private Student toEntity(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
        student.setCourse(dto.getCourse());
        return student;
    }

    private StudentResponseDTO toDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());
        dto.setCourse(student.getCourse()); // ✅ Added missing course field
        return dto;
    }

    @Override
    public Page<StudentResponseDTO> getStudents(String search, Integer age,
            org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudents'");
    }
}