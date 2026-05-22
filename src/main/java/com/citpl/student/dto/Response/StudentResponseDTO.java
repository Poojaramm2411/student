package com.citpl.student.dto.Response;

public class StudentResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String studentCode; // ✅ changed
    private Integer age; 
    private Boolean isActive;


    public StudentResponseDTO() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getStudentCode() { return studentCode; } // ✅ changed
    public Integer getAge() { return age; } 

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; } // ✅ changed
    public void setAge(Integer age) { this.age = age; } 
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}