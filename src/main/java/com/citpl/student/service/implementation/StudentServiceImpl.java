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
        return toDTO(studentRepository.save(student));
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
        // ✅ fixed - removed toggle code from here
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
        student.setIsActive(dto.getIsActive());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setAddress(dto.getAddress());
        student.setCity(dto.getCity());
        student.setState(dto.getState());
        student.setPinCode(dto.getPinCode());
        return toDTO(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.deleteById(id);
    }

    @Override
    public StudentResponseDTO toggleStatus(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Boolean current = student.getIsActive();
        student.setIsActive(current == null ? true : !current); // ✅ null safe
        return toDTO(studentRepository.save(student));
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

    // ✅ Search by id, name, email, city
    @Override
    public Page<StudentResponseDTO> searchStudents(String search, Pageable pageable) {
        return (Page<StudentResponseDTO>) studentRepository.searchStudents(search, pageable)
                .map(this::toDTO);
    }

    private Student toEntity(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
        student.setStudentCode(dto.getStudentCode());
        student.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setAddress(dto.getAddress());
        student.setCity(dto.getCity());
        student.setState(dto.getState());
        student.setPinCode(dto.getPinCode());
        return student;
    }

    private StudentResponseDTO toDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());
        dto.setStudentCode(student.getStudentCode());
        dto.setIsActive(student.getIsActive() != null ? student.getIsActive() : true);
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setAddress(student.getAddress());
        dto.setCity(student.getCity());
        dto.setState(student.getState());
        dto.setPinCode(student.getPinCode());
        return dto;
    }
}