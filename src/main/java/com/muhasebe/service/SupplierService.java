package com.muhasebe.service;

import com.muhasebe.dto.request.SupplierRequest;
import com.muhasebe.dto.response.SupplierResponse;
import com.muhasebe.entity.Supplier;
import com.muhasebe.entity.Company;
import com.muhasebe.exception.ResourceNotFoundException;
import com.muhasebe.exception.BadRequestException;
import com.muhasebe.repository.SupplierRepository;
import com.muhasebe.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Yeni tedarikçi oluşturur
     */
    @Transactional
    public SupplierResponse createSupplier(Long companyId, SupplierRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Şirket bulunamadı"));

        if (request.getTaxNumber() != null &&
                supplierRepository.existsByTaxNumberAndCompanyId(request.getTaxNumber(), companyId)) {
            throw new BadRequestException("Bu vergi numarası zaten kayıtlı");
        }

        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .contactPerson(request.getContactPerson())
                .taxNumber(request.getTaxNumber())
                .taxOffice(request.getTaxOffice())
                .address(request.getAddress())
                .city(request.getCity())
                .district(request.getDistrict())
                .postalCode(request.getPostalCode())
                .country(request.getCountry() != null ? request.getCountry() : "Türkiye")
                .phone(request.getPhone())
                .mobile(request.getMobile())
                .fax(request.getFax())
                .email(request.getEmail())
                .website(request.getWebsite())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .balance(request.getBalance() != null ? request.getBalance() : BigDecimal.ZERO)
                .notes(request.getNotes())
                .supplierType(request.getSupplierType())
                .paymentTerms(request.getPaymentTerms())
                .bankName(request.getBankName())
                .bankAccountNumber(request.getBankAccountNumber())
                .iban(request.getIban())
                .company(company)
                .build();

        supplier = supplierRepository.save(supplier);
        return mapToResponse(supplier);
    }

    /**
     * Tedarikçi günceller
     */
    @Transactional
    public SupplierResponse updateSupplier(Long companyId, Long supplierId, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Tedarikçi bulunamadı"));

        if (!supplier.getCompany().getId().equals(companyId)) {
            throw new BadRequestException("Bu tedarikçi bu şirkete ait değil");
        }

        if (request.getTaxNumber() != null &&
                !request.getTaxNumber().equals(supplier.getTaxNumber()) &&
                supplierRepository.existsByTaxNumberAndCompanyId(request.getTaxNumber(), companyId)) {
            throw new BadRequestException("Bu vergi numarası zaten kayıtlı");
        }

        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setTaxNumber(request.getTaxNumber());
        supplier.setTaxOffice(request.getTaxOffice());
        supplier.setAddress(request.getAddress());
        supplier.setCity(request.getCity());
        supplier.setDistrict(request.getDistrict());
        supplier.setPostalCode(request.getPostalCode());
        supplier.setCountry(request.getCountry());
        supplier.setPhone(request.getPhone());
        supplier.setMobile(request.getMobile());
        supplier.setFax(request.getFax());
        supplier.setEmail(request.getEmail());
        supplier.setWebsite(request.getWebsite());
        supplier.setIsActive(request.getIsActive());
        supplier.setNotes(request.getNotes());
        supplier.setSupplierType(request.getSupplierType());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setBankName(request.getBankName());
        supplier.setBankAccountNumber(request.getBankAccountNumber());
        supplier.setIban(request.getIban());

        supplier = supplierRepository.save(supplier);
        return mapToResponse(supplier);
    }

    /**
     * Tedarikçi siler (soft delete)
     */
    @Transactional
    public void deleteSupplier(Long companyId, Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Tedarikçi bulunamadı"));

        if (!supplier.getCompany().getId().equals(companyId)) {
            throw new BadRequestException("Bu tedarikçi bu şirkete ait değil");
        }

        supplier.setIsDeleted(true);
        supplierRepository.save(supplier);
    }

    /**
     * Tedarikçi detayını getirir
     */
    public SupplierResponse getSupplier(Long companyId, Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Tedarikçi bulunamadı"));

        if (!supplier.getCompany().getId().equals(companyId)) {
            throw new BadRequestException("Bu tedarikçi bu şirkete ait değil");
        }

        return mapToResponse(supplier);
    }

    /**
     * Şirkete ait tüm tedarikçileri getirir
     */
    public Page<SupplierResponse> getAllSuppliers(Long companyId, Pageable pageable) {
        Page<Supplier> suppliers = supplierRepository.findByCompanyId(companyId, pageable);
        return suppliers.map(this::mapToResponse);
    }

    /**
     * Genel arama yapar
     */
    public Page<SupplierResponse> globalSearch(Long companyId, String keyword, Pageable pageable) {
        Page<Supplier> suppliers = supplierRepository.globalSearch(companyId, keyword, pageable);
        return suppliers.map(this::mapToResponse);
    }

    /**
     * Supplier'ı SupplierResponse'a dönüştürür
     */
    private SupplierResponse mapToResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .taxNumber(supplier.getTaxNumber())
                .taxOffice(supplier.getTaxOffice())
                .address(supplier.getAddress())
                .city(supplier.getCity())
                .district(supplier.getDistrict())
                .postalCode(supplier.getPostalCode())
                .country(supplier.getCountry())
                .phone(supplier.getPhone())
                .mobile(supplier.getMobile())
                .fax(supplier.getFax())
                .email(supplier.getEmail())
                .website(supplier.getWebsite())
                .isActive(supplier.getIsActive())
                .balance(supplier.getBalance())
                .notes(supplier.getNotes())
                .supplierType(supplier.getSupplierType())
                .paymentTerms(supplier.getPaymentTerms())
                .bankName(supplier.getBankName())
                .bankAccountNumber(supplier.getBankAccountNumber())
                .iban(supplier.getIban())
                .companyId(supplier.getCompany().getId())
                .companyName(supplier.getCompany().getName())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .createdBy(supplier.getCreatedBy())
                .updatedBy(supplier.getUpdatedBy())
                .hasDebt(supplier.hasDebt())
                .hasCredit(supplier.hasCredit())
                .build();
    }
}