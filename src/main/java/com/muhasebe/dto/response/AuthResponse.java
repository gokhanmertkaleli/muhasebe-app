package com.muhasebe.dto.response;

import com.muhasebe.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Başarılı giriş sonrası response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;

    // Kullanıcı bilgileri
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private Long companyId;
    private String companyName;

    public AuthResponse(String accessToken, String refreshToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}