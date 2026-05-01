package com.example.service.impl;

import com.example.dto.request.AdminLoginRequest;
import com.example.dto.response.AuthResponse;
import com.example.model.Admin;
import com.example.repository.AdminRepository;
import com.example.security.JwtUtils;
import com.example.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final AdminRepository.RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final AuthService.RefreshTokenService refreshTokenService;

    // ─────────────────────────────────────────────────
    //  Login
    // ─────────────────────────────────────────────────
    @Override
    @Transactional
    public AuthResponse login(AdminLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Admin admin = (Admin) authentication.getPrincipal();

        String accessToken = jwtUtils.generateToken(admin);
        Admin.RefreshToken refreshToken = refreshTokenService.createRefreshToken(admin);

        log.info("Admin logged in: {}", admin.getEmail());
        return buildAuthResponse(accessToken, refreshToken.getToken(), admin);
    }

    // ─────────────────────────────────────────────────
    //  Refresh Token
    // ─────────────────────────────────────────────────
    @Override
    @Transactional
    public AuthResponse refreshToken(AdminLoginRequest.RefreshTokenRequest request) {
        String tokenValue = request.getRefreshToken();

        Admin.RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new com.example.exception.TokenRefreshException(
                        tokenValue, "Refresh token not found"));

        refreshTokenService.verifyExpiration(refreshToken);

        Admin admin = refreshToken.getAdmin();
        String newAccessToken = jwtUtils.generateToken(admin);

        log.info("Token refreshed for admin: {}", admin.getEmail());
        return buildAuthResponse(newAccessToken, tokenValue, admin);
    }

    // ─────────────────────────────────────────────────
    //  Logout
    // ─────────────────────────────────────────────────
    @Override
    @Transactional
    public void logout(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + email));
        refreshTokenService.deleteByAdmin(admin);
        SecurityContextHolder.clearContext();
        log.info("Admin logged out: {}", email);
    }

    // ─────────────────────────────────────────────────
    //  Helper
    // ─────────────────────────────────────────────────
    private AuthResponse buildAuthResponse(String accessToken, String refreshToken, Admin admin) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getExpirationMs() / 1000)
                .admin(AuthResponse.AdminInfo.builder()
                        .id(admin.getId())
                        .username(admin.getUsername())
                        .email(admin.getEmail())
                        .role(admin.getRole())
                        .build())
                .build();
    }
}