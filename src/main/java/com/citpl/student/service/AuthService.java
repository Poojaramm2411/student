package com.citpl.service;

import com.citpl.dto.request.AdminLoginRequest;
import com.citpl.dto.response.AuthResponse;
import com.citpl.model.Admin;

public interface AuthService {
 
    AuthResponse login(AdminLoginRequest request);

    AuthResponse refreshToken(AdminLoginRequest.RefreshTokenRequest request);

    void logout(String email);

    // ─────────────────────────────────────────────────
    //  RefreshTokenService — inner interface
    //  (no separate RefreshTokenService.java needed)
    // ─────────────────────────────────────────────────
    interface RefreshTokenService {

        Admin.RefreshToken createRefreshToken(Admin admin);

        Admin.RefreshToken verifyExpiration(Admin.RefreshToken token);

        void deleteByAdmin(Admin admin);
    }
}