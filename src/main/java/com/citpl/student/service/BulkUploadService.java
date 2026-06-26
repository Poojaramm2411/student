package com.citpl.student.service;

import com.citpl.student.model.*;
import com.citpl.student.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BulkUploadService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;

    // ─────────────────────────────────────────────
    // STUDENT BULK UPLOAD
    // Expected PDF table columns:
    // Name | Email | Age | StudentCode | City | BatchId
    // ─────────────────────────────────────────────
    public Map<String, Object> bulkUploadStudents(MultipartFile file) throws IOException {
        List<String[]> rows = parsePdfTable(file, 6);
        int success = 0, failed = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            try {
                if (studentRepository.existsByEmail(cols[1].trim())) {
                    errors.add("Row " + (i + 2) + ": Email already exists - " + cols[1]);
                    failed++;
                    continue;
                }
                Batch batch = batchRepository.findById(Long.parseLong(cols[5].trim()))
                    .orElseThrow(() -> new RuntimeException("Batch not found"));

                Student s = Student.builder()
                    .name(cols[0].trim())
                    .email(cols[1].trim())
                    .age(Integer.parseInt(cols[2].trim()))
                    .studentCode(cols[3].trim())
                    .city(cols[4].trim())
                    .batch(batch)
                    .status(Status.ACTIVE)
                    .build();
                studentRepository.save(s);
                success++;
            } catch (Exception e) {
                errors.add("Row " + (i + 2) + ": " + e.getMessage());
                failed++;
            }
        }
        return Map.of("success", success, "failed", failed, "errors", errors);
    }

    // ─────────────────────────────────────────────
    // COURSE BULK UPLOAD
    // Expected PDF table columns:
    // CourseName | CourseCode | Department | Duration | Status
    // ─────────────────────────────────────────────
    public Map<String, Object> bulkUploadCourses(MultipartFile file) throws IOException {
        List<String[]> rows = parsePdfTable(file, 5);
        int success = 0, failed = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            try {
                if (courseRepository.existsByCourseCode(cols[1].trim())) {
                    errors.add("Row " + (i + 2) + ": Course code already exists - " + cols[1]);
                    failed++;
                    continue;
                }
                Course c = Course.builder()
                    .courseName(cols[0].trim())
                    .courseCode(cols[1].trim())
                    .department(cols[2].trim())
                    .duration(cols[3].trim())
                    .status(Status.ACTIVE)
                    .build();
                courseRepository.save(c);
                success++;
            } catch (Exception e) {
                errors.add("Row " + (i + 2) + ": " + e.getMessage());
                failed++;
            }
        }
        return Map.of("success", success, "failed", failed, "errors", errors);
    }

    // ─────────────────────────────────────────────
    // BATCH BULK UPLOAD
    // Expected PDF table columns:
    // BatchName | Status
    // ─────────────────────────────────────────────
    public Map<String, Object> bulkUploadBatches(MultipartFile file) throws IOException {
        List<String[]> rows = parsePdfTable(file, 2);
        int success = 0, failed = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            try {
                Batch b = Batch.builder()
                    .batchName(cols[0].trim())
                    .status(Status.ACTIVE)
                    .build();
                batchRepository.save(b);
                success++;
            } catch (Exception e) {
                errors.add("Row " + (i + 2) + ": " + e.getMessage());
                failed++;
            }
        }
        return Map.of("success", success, "failed", failed, "errors", errors);
    }

    // ─────────────────────────────────────────────
    // PDF TABLE PARSER
    // Skips header row, splits each line by 2+ spaces
    // ─────────────────────────────────────────────
    private List<String[]> parsePdfTable(MultipartFile file, int expectedCols) throws IOException {
        PDDocument doc = Loader.loadPDF(file.getBytes());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        doc.close();

        List<String[]> rows = new ArrayList<>();
        String[] lines = text.split("\n");
        boolean headerSkipped = false;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (!headerSkipped) { headerSkipped = true; continue; } // skip header row
            String[] cols = line.split("\\s{2,}"); // split by 2+ whitespace
            if (cols.length >= expectedCols) rows.add(cols);
        }
        return rows;
    }
}