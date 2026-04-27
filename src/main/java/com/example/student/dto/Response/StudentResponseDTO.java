package com.example.student.dto.Response;

public class StudentResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String course;
    private Integer age; 

    public StudentResponseDTO() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public Integer getAge() { return age; } 

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setCourse(String course) { this.course = course; }
    public void setAge(Integer age) { this.age = age; } 
}