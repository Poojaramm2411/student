package com.citpl.student.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citpl.student.model.Batch;

public interface BatchRepository extends JpaRepository<Batch, Long> {

    @Query("SELECT b FROM Batch b WHERE " +
            "(:search IS NULL OR LOWER(b.batchName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:status IS NULL OR b.status = :status)")
    Page<Batch> findByFilters(@Param("search") String search,
            @Param("status") String status,
            Pageable pageable);
}