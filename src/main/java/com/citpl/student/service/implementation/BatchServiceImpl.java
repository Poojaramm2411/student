package com.citpl.student.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.citpl.student.dto.Request.BatchRequestDTO;
import com.citpl.student.dto.Response.BatchResponseDTO;
import com.citpl.student.model.Batch;
import com.citpl.student.repository.BatchRepository;
import com.citpl.student.service.BatchService;

@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;

    public BatchServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    public BatchResponseDTO createBatch(BatchRequestDTO dto) {
        Batch batch = new Batch();
        batch.setBatchName(dto.getBatchName());
        batch.setStartDate(dto.getStartDate());
        batch.setEndDate(dto.getEndDate());
        batch.setStatus(dto.getStatus());
        batch.setCapacity(dto.getCapacity());
        return mapToResponse(batchRepository.save(batch));
    }

    @Override
    public BatchResponseDTO getBatchById(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));
        return mapToResponse(batch);
    }

    @Override
    public BatchResponseDTO updateBatch(Long id, BatchRequestDTO dto) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));
        batch.setBatchName(dto.getBatchName());
        batch.setStartDate(dto.getStartDate());
        batch.setEndDate(dto.getEndDate());
        batch.setStatus(dto.getStatus());
        batch.setCapacity(dto.getCapacity());
        return mapToResponse(batchRepository.save(batch));
    }

    @Override
    public void deleteBatch(Long id) {
        batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));
        batchRepository.deleteById(id);
    }

    @Override
    public Page<BatchResponseDTO> getBatches(String search, String status, Pageable pageable) {
        return batchRepository.findByFilters(search, status, pageable)
                .map(this::mapToResponse);
    }

    private BatchResponseDTO mapToResponse(Batch batch) {
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