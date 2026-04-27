package com.example.student.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.student.dto.Request.BatchRequestDTO;
import com.example.student.dto.Response.BatchResponseDTO;

public interface BatchService {

    Page<BatchResponseDTO> getBatches(String search, String status, Pageable pageable);

    BatchResponseDTO getBatchById(Long id);

    BatchResponseDTO createBatch(BatchRequestDTO dto);

    BatchResponseDTO updateBatch(Long id, BatchRequestDTO dto);

    void deleteBatch(Long id);
}
