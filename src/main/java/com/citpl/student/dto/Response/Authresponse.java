package com.citpl.dto.response;

import com.example.model.Admin;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;         // seconds
    private AdminInfo admin;

    // ─────────────────────────────────────────────────
    //  Admin Info — inner static class
    // ─────────────────────────────────────────────────
    @Data
    @Builder
    public static class AdminInfo {
        private Long id;
        private String username;
        private String email;
        private Admin.Role role;
    }

    // ─────────────────────────────────────────────────
    //  Generic API Response — inner static class
    //  (replaces separate ApiResponse.java)
    // ─────────────────────────────────────────────────
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApiResponse<T> {

        private boolean success;
        private String message;
        private T data;
        private String error;

        @Builder.Default
        private LocalDateTime timestamp = LocalDateTime.now();

        public static <T> ApiResponse<T> success(String message, T data) {
            return AuthResponse.ApiResponse.<T>builder()
                    .success(true)
                    .message(message)
                    .data(data)
                    .build();
        }

        public static <T> ApiResponse<T> error(String message, String error) {
            return AuthResponse.ApiResponse.<T>builder()
                    .success(false)
                    .message(message)
                    .error(error)
                    .build();
        }
    }
}