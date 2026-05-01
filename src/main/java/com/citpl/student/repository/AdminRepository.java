package com.citpl.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.citpl.student.controller.Admin;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    // ─────────────────────────────────────────────────
    //  RefreshToken Repository — inner interface
    // ─────────────────────────────────────────────────
    @Repository
    interface RefreshTokenRepository extends JpaRepository<Admin.RefreshToken, Long> {

        Optional<Admin.RefreshToken> findByToken(String token);

        Optional<Admin.RefreshToken> findByAdmin(Admin admin);

        @Modifying
        int deleteByAdmin(Admin admin);
    }
}