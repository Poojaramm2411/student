package com.citpl.student.dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequestDTO {

    private String courseName;
    private String courseCode;
    private String description;
    private String department;
    private Integer duration;
    private String status;
    private Long batchId;  // FK to Batch
}