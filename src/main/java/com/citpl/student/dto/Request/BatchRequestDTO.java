package com.citpl.student.dto.Request;

public class BatchRequestDTO {

    private String batchName;
    private String startDate;
    private String endDate;
    private String status;
    private Integer capacity;

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Object getCourse() {
     
        throw new UnsupportedOperationException("Unimplemented method 'getCourse'");
    }
}