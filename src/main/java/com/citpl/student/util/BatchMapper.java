package com.citpl.student.util;

import org.springframework.stereotype.Component;

import com.citpl.student.dto.Request.BatchRequestDTO;
import com.citpl.student.dto.Response.BatchResponseDTO;
import com.citpl.student.model.Batch;

@Component
public class BatchMapper {

    public Batch toEntity(BatchRequestDTO dto) {
        Batch batch = new Batch();
        batch.setBatchName(dto.getBatchName());
        batch.setStartDate(dto.getStartDate());
        batch.setEndDate(dto.getEndDate());
        batch.setStatus(dto.getStatus());
        batch.setCapacity(dto.getCapacity());
        return batch;
    }

    public BatchResponseDTO toDTO(Batch batch) {
        BatchResponseDTO dto = new BatchResponseDTO();
        dto.setId(batch.getId());
        dto.setBatchName(batch.getBatchName());
        dto.setStartDate(batch.getStartDate());
        dto.setEndDate(batch.getEndDate());
        dto.setStatus(batch.getStatus());
        dto.setCapacity(batch.getCapacity());
        return dto;
    }
}