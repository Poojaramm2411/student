package com.citpl.student.controller;

import com.citpl.student.dto.Request.StudentRequestDTO;
import com.citpl.student.dto.Response.StudentResponseDTO;
import com.citpl.student.service.BulkUploadService;
import com.citpl.student.service.ExportService;
import com.citpl.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final BulkUploadService bulkUploadService;
    private final ExportService exportService;

    // ── CREATE ──
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(dto));
    }

    // ── GET BY ID ──
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // ── GET ALL (paginated) ──
    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long batchId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending());
        return ResponseEntity.ok(studentService.getAllStudents(search, status, batchId, pageable));
    }

    // ── UPDATE ──
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id, @RequestBody StudentRequestDTO dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    // ── TOGGLE STATUS ──
    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentResponseDTO> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.toggleStatus(id));
    }

    // ── DELETE ──
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // ── BULK UPLOAD (PDF) ──
    @PostMapping("/bulk-upload")
    public ResponseEntity<Map<String, Object>> bulkUpload(
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(bulkUploadService.bulkUploadStudents(file));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BULK UPLOAD ERROR: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── EXPORT PDF ──
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf() {
        try {
            byte[] data = exportService.exportStudentsPdf();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PDF EXPORT ERROR: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // ── EXPORT EXCEL ──
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel() {
        try {
            byte[] data = exportService.exportStudentsExcel();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.xlsx")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EXCEL EXPORT ERROR: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}