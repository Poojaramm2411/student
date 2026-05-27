package com.citpl.student.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.citpl.student.dto.Request.StudentRequestDTO;
import com.citpl.student.dto.Response.StudentResponseDTO;
import com.citpl.student.service.StudentService;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Page<StudentResponseDTO> getStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer age,
            Pageable pageable) {
        return studentService.getStudents(search, age, pageable);
    }

    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public StudentResponseDTO createStudent(@RequestBody StudentRequestDTO dto) {
        return studentService.createStudent(dto);
    }

    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(@PathVariable Long id,
                                            @RequestBody StudentRequestDTO dto) {
        return studentService.updateStudent(id, dto);
    }

    @PatchMapping("/{id}/status")
    public StudentResponseDTO toggleStatus(@PathVariable Long id) {
        return studentService.toggleStatus(id);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/search")
    public Page<StudentResponseDTO> searchStudents(
            @RequestParam String search,
            Pageable pageable) {
        return studentService.searchStudents(search, pageable);
    }

    // ✅ DOWNLOAD TEMPLATE
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Row header = sheet.createRow(0);
        String[] columns = {
            "Name", "Email", "Age", "Student Code",
            "City", "State", "Address", "Pin Code",
            "Date Of Birth", "Status"
        };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "students_template.xlsx");

        return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    }

    // ✅ EXPORT EXCEL
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportStudents() throws Exception {
        List<StudentResponseDTO> students = studentService.getAllStudents();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Row header = sheet.createRow(0);
        String[] columns = {
            "ID", "Name", "Email", "Age", "Student Code",
            "City", "State", "Address", "Pin Code",
            "Date Of Birth", "Status"
        };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (StudentResponseDTO s : students) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(s.getId() != null ? s.getId() : 0);
            row.createCell(1).setCellValue(s.getName() != null ? s.getName() : "");
            row.createCell(2).setCellValue(s.getEmail() != null ? s.getEmail() : "");
            row.createCell(3).setCellValue(s.getAge() != null ? s.getAge() : 0);
            row.createCell(4).setCellValue(s.getStudentCode() != null ? s.getStudentCode() : "");
            row.createCell(5).setCellValue(s.getCity() != null ? s.getCity() : "");
            row.createCell(6).setCellValue(s.getState() != null ? s.getState() : "");
            row.createCell(7).setCellValue(s.getAddress() != null ? s.getAddress() : "");
            row.createCell(8).setCellValue(s.getPinCode() != null ? s.getPinCode() : "");
            row.createCell(9).setCellValue(s.getDateOfBirth() != null ? s.getDateOfBirth() : "");
            row.createCell(10).setCellValue(
                s.getIsActive() != null && s.getIsActive() ? "Active" : "Inactive");
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "students.xlsx");

        return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    }

    // ✅ IMPORT EXCEL
    @PostMapping("/import")
    public ResponseEntity<String> importStudents(@RequestParam("file") MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;
            boolean isHeader = true;

            for (Row row : sheet) {
                if (isHeader) { isHeader = false; continue; }

                StudentRequestDTO dto = new StudentRequestDTO();
                dto.setName(getCellValue(row, 0));
                dto.setEmail(getCellValue(row, 1));
                String ageStr = getCellValue(row, 2);
                dto.setAge(ageStr.isEmpty() ? null : (int) Double.parseDouble(ageStr));
                dto.setStudentCode(getCellValue(row, 3));
                dto.setCity(getCellValue(row, 4));
                dto.setState(getCellValue(row, 5));
                dto.setAddress(getCellValue(row, 6));
                dto.setPinCode(getCellValue(row, 7));
                dto.setDateOfBirth(getCellValue(row, 8));
                dto.setIsActive(getCellValue(row, 9).equalsIgnoreCase("Active"));

                studentService.createStudent(dto);
                count++;
            }

            workbook.close();
            return ResponseEntity.ok("Successfully imported " + count + " students");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Import failed: " + e.getMessage());
        }
    }

    // ✅ Helper
    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }
}