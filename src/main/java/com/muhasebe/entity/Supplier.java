package com.muhasebe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

/**
 * Tedarikçi Entity Sınıfı
 * Dosya Yolu: src/main/java/com/muhasebe/entity/Supplier.java
 */
@Entity
@Table(name = "suppliers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tax_number", "company_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier extends BaseEntity {

    @NotBlank
    @Size(max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 100)
    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Size(min = 10, max = 11)
    @Column(name = "tax_number", length = 11)
    private String taxNumber;

    @Size(max = 100)
    @Column(name = "tax_office", length = 100)
    private String taxOffice;

    @Size(max = 500)
    @Column(name = "address", length = 500)
    private String address;

    @Size(max = 100)
    @Column(name = "city", length = 100)
    private String city;

    @Size(max = 100)
    @Column(name = "district", length = 100)
    private String district;

    @Size(max = 10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Size(max = 50)
    @Column(name = "country", length = 50)
    private String country = "Türkiye";

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 20)
    @Column(name = "mobile", length = 20)
    private String mobile;

    @Size(max = 20)
    @Column(name = "fax", length = 20)
    private String fax;

    @Email
    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 200)
    @Column(name = "website", length = 200)
    private String website;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "supplier_type", length = 50)
    private String supplierType; // Üretici, İthalatçı, Distribütör

    @Column(name = "payment_terms")
    private Integer paymentTerms; // Ödeme vadesi (gün)

    @Size(max = 100)
    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Size(max = 50)
    @Column(name = "bank_account_number", length = 50)
    private String bankAccountNumber;

    @Size(max = 26)
    @Column(name = "iban", length = 26)
    private String iban;

    /**
     * Tedarikçinin tam adresini döndürür
     */
    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        if (address != null) fullAddress.append(address);
        if (district != null) fullAddress.append(", ").append(district);
        if (city != null) fullAddress.append(", ").append(city);
        if (postalCode != null) fullAddress.append(" ").append(postalCode);
        if (country != null) fullAddress.append(", ").append(country);
        return fullAddress.toString();
    }

    /**
     * Tedarikçiye borç durumunu kontrol eder
     */
    public boolean hasDebt() {
        return balance != null && balance.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Tedarikçiden alacak durumunu kontrol eder
     */
    public boolean hasCredit() {
        return balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
    }
}