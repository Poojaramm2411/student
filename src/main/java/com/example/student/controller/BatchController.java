package com.example.student.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.dto.Request.BatchRequestDTO;
import com.example.student.dto.Response.BatchResponseDTO;
import com.example.student.service.BatchService;



@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public Page<BatchResponseDTO> getBatches(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return batchService.getBatches(search, status, pageable);
    }

    @GetMapping("/{id}")
    public BatchResponseDTO getBatchById(@PathVariable Long id) {
        return batchService.getBatchById(id);
    }

    @PostMapping
    public BatchResponseDTO createBatch(@RequestBody BatchRequestDTO dto) {
        return batchService.createBatch(dto);
    }

    @PutMapping("/{id}")
    public BatchResponseDTO updateBatch(@PathVariable Long id,
            @RequestBody BatchRequestDTO dto) {
        return batchService.updateBatch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
    }
}