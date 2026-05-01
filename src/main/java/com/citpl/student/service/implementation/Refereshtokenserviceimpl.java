package com.example.service.impl;

import com.example.exception.TokenRefreshException;
import com.example.model.Admin;
import com.example.repository.AdminRepository;
import com.example.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements AuthService.RefreshTokenService {

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshTokenDurationMs;

    private final AdminRepository.RefreshTokenRepository refreshTokenRepository;

    // ─────────────────────────────────────────────────
    //  Create refresh token (replaces old one if exists)
    // ─────────────────────────────────────────────────
    @Override
    @Transactional
    public Admin.RefreshToken createRefreshToken(Admin admin) {
        refreshTokenRepository.findByAdmin(admin)
                .ifPresent(refreshTokenRepository::delete);

        Admin.RefreshToken refreshToken = Admin.RefreshToken.builder()
                .admin(admin)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // ─────────────────────────────────────────────────
    //  Verify expiry — throws if expired or not found
    // ─────────────────────────────────────────────────
    @Override
    public Admin.RefreshToken verifyExpiration(Admin.RefreshToken token) {
        Admin.RefreshToken found = refreshTokenRepository.findByToken(token.getToken())
                .orElseThrow(() -> new TokenRefreshException(
                        token.getToken(), "Refresh token not found in database"));

        if (found.isExpired()) {
            refreshTokenRepository.delete(found);
            throw new TokenRefreshException(
                    found.getToken(), "Refresh token has expired. Please log in again.");
        }

        return found;
    }

    // ─────────────────────────────────────────────────
    //  Delete on logout
    // ─────────────────────────────────────────────────
    @Override
    @Transactional
    public void deleteByAdmin(Admin admin) {
        refreshTokenRepository.deleteByAdmin(admin);
    }
}