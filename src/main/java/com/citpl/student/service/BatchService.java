package com.citpl.student.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.citpl.student.dto.Request.BatchRequestDTO;
import com.citpl.student.dto.Response.BatchResponseDTO;

public interface BatchService {

    Page<BatchResponseDTO> getBatches(String search, String status, Pageable pageable);

    BatchResponseDTO getBatchById(Long id);

    BatchResponseDTO createBatch(BatchRequestDTO dto);

    BatchResponseDTO updateBatch(Long id, BatchRequestDTO dto);

    void deleteBatch(Long id);
}
