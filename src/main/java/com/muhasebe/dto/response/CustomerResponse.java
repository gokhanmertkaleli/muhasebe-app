package com.muhasebe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private String name;
    private String contactPerson;
    private String taxNumber;
    private String identityNumber;
    private String taxOffice;
    private String address;
    private String city;
    private String district;
    private String postalCode;
    private String country;
    private String phone;
    private String mobile;
    private String fax;
    private String email;
    private String website;
    private Boolean isActive;
    private BigDecimal balance;
    private String notes;
    private String customerType;
    private Integer paymentTerms;
    private BigDecimal creditLimit;
    private Long companyId;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    // Helper methodlar
    private Boolean hasDebt;
    private Boolean hasCredit;
    private Boolean isOverCreditLimit;
}