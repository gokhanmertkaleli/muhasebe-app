package com.muhasebe.service;

import com.muhasebe.dto.request.LoginRequest;
import com.muhasebe.dto.request.RegisterRequest;
import com.muhasebe.dto.response.AuthResponse;
import com.muhasebe.entity.Company;
import com.muhasebe.entity.User;
import com.muhasebe.enums.Role;
import com.muhasebe.repository.CompanyRepository;
import com.muhasebe.repository.UserRepository;
import com.muhasebe.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kimlik doğrulama ve yetkilendirme işlemlerini yöneten servis
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Kullanıcı girişi yapar
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            // Kullanıcıyı bul
            User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail())
                    .orElseThrow(() -> new BadCredentialsException("Kullanıcı bulunamadı"));

            // Hesap kontrolü
            if (!user.getIsActive()) {
                throw new BadCredentialsException("Hesabınız aktif değil");
            }

            if (user.isAccountLocked()) {
                throw new BadCredentialsException("Hesabınız kilitli. Lütfen daha sonra tekrar deneyin");
            }

            // Kimlik doğrulama
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Token oluştur
            String accessToken = jwtTokenProvider.generateToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

            // Başarılı giriş kaydı
            user.resetFailedLoginAttempts();
            userRepository.save(user);

            // Response oluştur
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtExpiration)
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole())
                    .companyId(user.getCompany() != null ? user.getCompany().getId() : null)
                    .companyName(user.getCompany() != null ? user.getCompany().getName() : null)
                    .build();

        } catch (BadCredentialsException ex) {
            // Başarısız giriş kaydı
            userRepository.findByUsernameOrEmail(request.getUsernameOrEmail())
                    .ifPresent(user -> {
                        user.incrementFailedLoginAttempts();
                        userRepository.save(user);
                    });
            throw new BadCredentialsException("Kullanıcı adı veya şifre hatalı");
        }
    }

    /**
     * Yeni kullanıcı kaydı yapar
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Kullanıcı adı kontrolü
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }

        // Email kontrolü
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email adresi zaten kullanılıyor");
        }

        // Şirket kontrolü
        Company company = null;
        if (request.getCompanyId() != null) {
            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Şirket bulunamadı"));
        }

        // Kullanıcı oluştur
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .isActive(true)
                .isEmailVerified(false)
                .company(company)
                .failedLoginAttempts(0)
                .build();

        userRepository.save(user);

        // Otomatik giriş yap
        LoginRequest loginRequest = new LoginRequest(
                request.getUsername(),
                request.getPassword()
        );

        return login(loginRequest);
    }

    /**
     * Refresh token ile yeni access token üretir
     */
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Geçersiz refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Kullanıcı bulunamadı"));

        String newAccessToken = jwtTokenProvider.generateToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .companyId(user.getCompany() != null ? user.getCompany().getId() : null)
                .companyName(user.getCompany() != null ? user.getCompany().getName() : null)
                .build();
    }

    /**
     * Kullanıcı çıkışı yapar
     */
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}