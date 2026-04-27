package com.example.student.dto.Request;

public class StudentRequestDTO {

    private String name;
    private String email;
    private String course;
    private Integer age; 

    public StudentRequestDTO() {}

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public Integer getAge() { return age; } 

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setCourse(String course) { this.course = course; }
    public void setAge(Integer age) { this.age = age; } 
}