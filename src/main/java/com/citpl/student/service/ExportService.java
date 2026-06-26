package com.citpl.student.service;

import com.citpl.student.model.Student;
import com.citpl.student.model.Course;
import com.citpl.student.model.Batch;
import com.citpl.student.repository.StudentRepository;
import com.citpl.student.repository.CourseRepository;
import com.citpl.student.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExportService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;

    // ─────────────────────────────────────────────
    // STUDENT EXPORTS
    // ─────────────────────────────────────────────

    public byte[] exportStudentsPdf() throws IOException {
    List<Student> students = studentRepository.findAll();
    String[] headers = {"ID", "Name", "Email", "Age", "Student Code", "City", "Status"};

    String[][] rows = students.stream().map(s -> new String[]{
        String.valueOf(s.getId()),
        s.getName() != null ? s.getName() : "-",
        s.getEmail() != null ? s.getEmail() : "-",
        s.getAge() != null ? String.valueOf(s.getAge()) : "-",
        s.getStudentCode() != null ? s.getStudentCode() : "-",
        s.getCity() != null ? s.getCity() : "-",
        s.getStatus() != null ? s.getStatus().name() : "-"
    }).toArray(String[][]::new);

    return generatePdf("Students Report", headers, rows);
}
    public byte[] exportStudentsExcel() throws IOException {
        List<Student> students = studentRepository.findAll();
        String[] headers = {"ID", "Name", "Email", "Age", "Student Code", "City", "Status"};

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");
            writeExcelHeaders(workbook, sheet, headers);

            int rowNum = 1;
            for (Student s : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getName());
                row.createCell(2).setCellValue(s.getEmail());
                row.createCell(3).setCellValue(s.getAge() != null ? s.getAge() : 0);
                row.createCell(4).setCellValue(s.getStudentCode());
                row.createCell(5).setCellValue(s.getCity() != null ? s.getCity() : "-");
                row.createCell(6).setCellValue(s.getStatus().name());
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            return toBytes(workbook);
        }
    }

    // ─────────────────────────────────────────────
    // COURSE EXPORTS
    // ─────────────────────────────────────────────

    public byte[] exportCoursesPdf() throws IOException {
        List<Course> courses = courseRepository.findAll();
        String[] headers = {"ID", "Course Name", "Code", "Department", "Duration", "Status"};

        String[][] rows = courses.stream().map(c -> new String[]{
            String.valueOf(c.getId()),
            c.getCourseName(),
            c.getCourseCode(),
            c.getDepartment() != null ? c.getDepartment() : "-",
            c.getDuration() != null ? c.getDuration() : "-",
            c.getStatus().name()
        }).toArray(String[][]::new);

        return generatePdf("Courses Report", headers, rows);
    }

    public byte[] exportCoursesExcel() throws IOException {
        List<Course> courses = courseRepository.findAll();
        String[] headers = {"ID", "Course Name", "Code", "Department", "Duration", "Status"};

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Courses");
            writeExcelHeaders(workbook, sheet, headers);

            int rowNum = 1;
            for (Course c : courses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(c.getId());
                row.createCell(1).setCellValue(c.getCourseName());
                row.createCell(2).setCellValue(c.getCourseCode());
                row.createCell(3).setCellValue(c.getDepartment() != null ? c.getDepartment() : "-");
                row.createCell(4).setCellValue(c.getDuration() != null ? c.getDuration() : "-");
                row.createCell(5).setCellValue(c.getStatus().name());
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            return toBytes(workbook);
        }
    }

    // ─────────────────────────────────────────────
    // BATCH EXPORTS
    // ─────────────────────────────────────────────

    public byte[] exportBatchesPdf() throws IOException {
        List<Batch> batches = batchRepository.findAll();
        String[] headers = {"ID", "Batch Name", "Status"};

        String[][] rows = batches.stream().map(b -> new String[]{
            String.valueOf(b.getId()),
            b.getBatchName(),
            b.getStatus().name()
        }).toArray(String[][]::new);

        return generatePdf("Batches Report", headers, rows);
    }

    public byte[] exportBatchesExcel() throws IOException {
        List<Batch> batches = batchRepository.findAll();
        String[] headers = {"ID", "Batch Name", "Status"};

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Batches");
            writeExcelHeaders(workbook, sheet, headers);

            int rowNum = 1;
            for (Batch b : batches) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(b.getId());
                row.createCell(1).setCellValue(b.getBatchName());
                row.createCell(2).setCellValue(b.getStatus().name());
            }
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            return toBytes(workbook);
        }
    }

    // ─────────────────────────────────────────────
    // SHARED HELPERS
    // ─────────────────────────────────────────────

    private byte[] generatePdf(String title, String[] headers, String[][] rows) throws IOException {
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDType1Font boldFont   = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font normalFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            float margin = 40, y = 780, rowHeight = 20, colWidth = (PDRectangle.A4.getWidth() - 2 * margin) / headers.length;

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                // Title
                cs.beginText();
                cs.setFont(boldFont, 16);
                cs.newLineAtOffset(margin, y);
                cs.showText(title);
                cs.endText();
                y -= 30;

                // Header row background
                cs.setNonStrokingColor(63, 81, 181);
                cs.addRect(margin, y - 5, PDRectangle.A4.getWidth() - 2 * margin, rowHeight);
                cs.fill();

                // Header text
                cs.setNonStrokingColor(255, 255, 255);
                cs.beginText();
                cs.setFont(boldFont, 9);
                for (int i = 0; i < headers.length; i++) {
                    cs.newLineAtOffset(i == 0 ? margin + 4 : colWidth, 0);
                    cs.showText(headers[i]);
                }
                cs.newLineAtOffset(-colWidth * (headers.length - 1) - margin - 4, 0);
                cs.endText();
                y -= rowHeight;

                // Data rows
                boolean alternate = false;
                for (String[] row : rows) {
                    if (y < 60) { // new page if needed
                        cs.close();
                        PDPage newPage = new PDPage(PDRectangle.A4);
                        doc.addPage(newPage);
                        y = 780;
                    }
                    if (alternate) {
                        cs.setNonStrokingColor(240, 240, 255);
                        cs.addRect(margin, y - 5, PDRectangle.A4.getWidth() - 2 * margin, rowHeight);
                        cs.fill();
                    }
                    alternate = !alternate;

                    cs.setNonStrokingColor(30, 30, 30);
                    cs.beginText();
                    cs.setFont(normalFont, 9);
                    for (int i = 0; i < row.length; i++) {
                        cs.newLineAtOffset(i == 0 ? margin + 4 : colWidth, 0);
                        String cell = row[i] != null ? row[i] : "-";
                        cs.showText(cell.length() > 25 ? cell.substring(0, 22) + "..." : cell);
                    }
                    cs.newLineAtOffset(-colWidth * (row.length - 1) - margin - 4, 0);
                    cs.endText();
                    y -= rowHeight;
                }
            }
            doc.save(out);
            return out.toByteArray();
        }
    }

    private void writeExcelHeaders(XSSFWorkbook workbook, Sheet sheet, String[] headers) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.INDIGO.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private byte[] toBytes(XSSFWorkbook workbook) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }
}