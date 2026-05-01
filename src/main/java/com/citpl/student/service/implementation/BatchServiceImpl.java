package com.citpl.student.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.citpl.student.dto.Request.BatchRequestDTO;
import com.citpl.student.dto.Response.BatchResponseDTO;
import com.citpl.student.model.Batch;
import com.citpl.student.repository.BatchRepository;
import com.citpl.student.service.BatchService;
import com.citpl.student.util.BatchMapper;

@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final BatchMapper batchMapper;

    public BatchServiceImpl(BatchRepository batchRepository, BatchMapper batchMapper) {
        this.batchRepository = batchRepository;
        this.batchMapper = batchMapper;
    }

    @Override
    public BatchResponseDTO createBatch(BatchRequestDTO dto) {
        Batch batch = batchMapper.toEntity(dto);
        return batchMapper.toDTO(batchRepository.save(batch));
    }

    @Override
    public BatchResponseDTO getBatchById(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));
        return batchMapper.toDTO(batch);
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
        return batchMapper.toDTO(batchRepository.save(batch));
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
                .map(batchMapper::toDTO);
    }
}