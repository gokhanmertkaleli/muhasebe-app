package com.muhasebe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "Müşteri adı boş olamaz")
    @Size(max = 200, message = "Müşteri adı 200 karakterden fazla olamaz")
    private String name;

    @Size(max = 100, message = "İletişim kişisi 100 karakterden fazla olamaz")
    private String contactPerson;

    @Size(min = 10, max = 11, message = "Vergi numarası 10-11 karakter olmalıdır")
    private String taxNumber;

    @Size(max = 11, message = "TC Kimlik No 11 karakter olmalıdır")
    private String identityNumber;

    @Size(max = 100, message = "Vergi dairesi 100 karakterden fazla olamaz")
    private String taxOffice;

    @Size(max = 500, message = "Adres 500 karakterden fazla olamaz")
    private String address;

    @Size(max = 100, message = "Şehir 100 karakterden fazla olamaz")
    private String city;

    @Size(max = 100, message = "İlçe 100 karakterden fazla olamaz")
    private String district;

    @Size(max = 10, message = "Posta kodu 10 karakterden fazla olamaz")
    private String postalCode;

    @Size(max = 50, message = "Ülke 50 karakterden fazla olamaz")
    private String country;

    @Size(max = 20, message = "Telefon 20 karakterden fazla olamaz")
    private String phone;

    @Size(max = 20, message = "Mobil 20 karakterden fazla olamaz")
    private String mobile;

    @Size(max = 20, message = "Fax 20 karakterden fazla olamaz")
    private String fax;

    @Email(message = "Geçerli bir email adresi giriniz")
    @Size(max = 100, message = "Email 100 karakterden fazla olamaz")
    private String email;

    @Size(max = 200, message = "Website 200 karakterden fazla olamaz")
    private String website;

    private Boolean isActive;

    private BigDecimal balance;

    @Size(max = 500, message = "Notlar 500 karakterden fazla olamaz")
    private String notes;

    @Size(max = 50, message = "Müşteri tipi 50 karakterden fazla olamaz")
    private String customerType;

    private Integer paymentTerms;

    private BigDecimal creditLimit;
}
