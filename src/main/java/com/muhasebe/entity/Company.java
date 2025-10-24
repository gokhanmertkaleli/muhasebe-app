package com.muhasebe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies", uniqueConstraints = {
        @UniqueConstraint(columnNames = "tax_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends BaseEntity {

    @NotBlank
    @Size(max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 100)
    @Column(name = "trade_name", length = 100)
    private String tradeName;

    @NotBlank
    @Size(min = 10, max = 11)
    @Column(name = "tax_number", nullable = false, unique = true, length = 11)
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
    @Column(name = "fax", length = 20)
    private String fax;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 200)
    @Column(name = "website", length = 200)
    private String website;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Size(max = 50)
    @Column(name = "gib_username", length = 50)
    private String gibUsername;

    @Size(max = 255)
    @Column(name = "gib_password", length = 255)
    private String gibPassword;

    @Column(name = "e_invoice_enabled")
    private Boolean eInvoiceEnabled = false;

    @Column(name = "e_archive_enabled")
    private Boolean eArchiveEnabled = false;

    @Size(max = 500)
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "company_type", length = 50)
    private String companyType;

    @Column(name = "establishment_date")
    private java.time.LocalDate establishmentDate;

    @Column(name = "mersis_no", length = 16)
    private String mersisNo;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<User> users = new ArrayList<>();

    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        if (address != null) fullAddress.append(address);
        if (district != null) fullAddress.append(", ").append(district);
        if (city != null) fullAddress.append(", ").append(city);
        if (postalCode != null) fullAddress.append(" ").append(postalCode);
        if (country != null) fullAddress.append(", ").append(country);
        return fullAddress.toString();
    }

    public boolean isEInvoiceRegistered() {
        return eInvoiceEnabled && gibUsername != null && !gibUsername.isEmpty();
    }
}