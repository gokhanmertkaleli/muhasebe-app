package com.muhasebe.dto.request;

import com.muhasebe.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Kullanıcı kaydı için request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 50, message = "Kullanıcı adı 3-50 karakter arasında olmalıdır")
    private String username;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    @Size(max = 100, message = "Email 100 karakterden fazla olamaz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, max = 100, message = "Şifre 6-100 karakter arasında olmalıdır")
    private String password;

    @NotBlank(message = "Ad boş olamaz")
    @Size(max = 100, message = "Ad 100 karakterden fazla olamaz")
    private String firstName;

    @NotBlank(message = "Soyad boş olamaz")
    @Size(max = 100, message = "Soyad 100 karakterden fazla olamaz")
    private String lastName;

    @Size(max = 20, message = "Telefon 20 karakterden fazla olamaz")
    private String phone;

    private Role role = Role.USER;

    private Long companyId;
}