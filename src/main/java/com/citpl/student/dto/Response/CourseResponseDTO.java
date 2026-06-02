package com.citpl.student.dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDTO {

    private Long id;
    private String courseName;
    private String courseCode;
    private String description;
    private String department;
    private Integer duration;
    private String status;

    // Flattened batch info
    private Long batchId;
    private String batchName;
}