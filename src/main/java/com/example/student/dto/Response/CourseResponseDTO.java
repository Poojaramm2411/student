package com.example.student.dto.Response;

public class CourseResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String duration;
    private String level;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public void setCourseCode(Object courseCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCourseCode'");
    }
    public void setCourseName(Object courseName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCourseName'");
    }
}