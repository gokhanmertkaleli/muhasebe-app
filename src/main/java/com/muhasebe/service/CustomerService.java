package com.muhasebe.service;

import com.muhasebe.dto.request.CustomerRequest;
import com.muhasebe.dto.response.CustomerResponse;
import com.muhasebe.entity.Customer;
import com.muhasebe.entity.Company;
import com.muhasebe.exception.ResourceNotFoundException;
import com.muhasebe.exception.BadRequestException;
import com.muhasebe.repository.CustomerRepository;
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
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Yeni müşteri oluşturur
     */
    @Transactional
    public CustomerResponse createCustomer(Long companyId, CustomerRequest request) {
        // Şirket kontrolü
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Şirket bulunamadı"));

        // Vergi numarası kontrolü
        if (request.getTaxNumber() != null &&
                customerRepository.existsByTaxNumberAndCompanyId(request.getTaxNumber(), companyId)) {
            throw new BadRequestException("Bu vergi numarası zaten kayıtlı");
        }

        // Customer entity oluştur
        Customer customer = Customer.builder()
                .name(request.getName())
                .contactPerson(request.getContactPerson())
                .taxNumber(request.getTaxNumber())
                .identityNumber(request.getIdentityNumber())
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
                .customerType(request.getCustomerType())
                .paymentTerms(request.getPaymentTerms())
                .creditLimit(request.getCreditLimit())
                .company(company)
                .build();

        customer = customerRepository.save(customer);
        return mapToResponse(customer);
    }

    /**
     * Müşteri günceller
     */
    @Transactional
    public CustomerResponse updateCustomer(Long companyId, Long customerId, CustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Müşteri bulunamadı"));

        // Şirket kontrolü
        if (!customer.getCompany().getId().equals(companyId)) {
            throw new BadRequestException("Bu müşteri bu şirkete ait değil");
        }

        // Vergi numarası kontrolü (değiştirilmişse)
        if (request.getTaxNumber() != null &&
                !request.getTaxNumber().equals(customer.getTaxNumber()) &&
                customerRepository.existsByTaxNumberAndCompanyId(request.getTaxNumber(), companyId)) {
            throw new BadRequestException("Bu vergi numarası zaten kayıtlı");
        }

        // Güncelle
        customer.setName(request.getName());
        customer.setContactPerson(request.getContactPerson());
        customer.setTaxNumber(request.getTaxNumber());
        customer.setIdentityNumber(request.getIdentityNumber());
        customer.setTaxOffice(request.getTaxOffice());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setDistrict(request.getDistrict());
        customer.setPostalCode(request.getPostalCode());
        customer.setCountry(request.getCountry());
        customer.setPhone(request.getPhone());
        customer.setMobile(request.getMobile());
        customer.setFax(request.getFax());
        customer.setEmail(request.getEmail());
        customer.setWebsite(request.getWebsite());
        customer.setIsActive(request.getIsActive());
        customer.setNotes(request.getNotes());
        customer.setCustomerType(request.getCustomerType());
        customer.setPaymentTerms(request.getPaymentTerms());
        customer.setCreditLimit(request.getCreditLimit());

        customer = customerRepository.save(customer);
        return mapToResponse(customer);
    }

    /**
     * Müşteri siler (soft delete)
     */
    @Transactional
    public void deleteCustomer(Long companyId, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Müşteri bulunamadı"));

        if (!customer.getCompany().getId().equals(companyId)) {
            throw new BadRequestException("Bu müşteri bu şirkete ait değil");
        }

        customer.setIsDeleted(true);
        customerRepository.save(customer);
    }

    /**
     * Müşteri detayını getirir
     */
    public CustomerResponse getCustomer(Long companyId, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Müşteri bulunamadı"));

        if (!customer.getCompany().getId().equals(companyId)) {
            throw new BadRequestException("Bu müşteri bu şirkete ait değil");
        }

        return mapToResponse(customer);
    }

    /**
     * Şirkete ait tüm müşterileri getirir (sayfalı)
     */
    public Page<CustomerResponse> getAllCustomers(Long companyId, Pageable pageable) {
        Page<Customer> customers = customerRepository.findByCompanyId(companyId, pageable);
        return customers.map(this::mapToResponse);
    }

    /**
     * Aktif müşterileri getirir
     */
    public Page<CustomerResponse> getActiveCustomers(Long companyId, Pageable pageable) {
        Page<Customer> customers = customerRepository.findActiveCustomersByCompanyId(companyId, pageable);
        return customers.map(this::mapToResponse);
    }

    /**
     * İsme göre arama yapar
     */
    public Page<CustomerResponse> searchCustomersByName(Long companyId, String name, Pageable pageable) {
        Page<Customer> customers = customerRepository.searchByName(companyId, name, pageable);
        return customers.map(this::mapToResponse);
    }

    /**
     * Genel arama yapar
     */
    public Page<CustomerResponse> globalSearch(Long companyId, String keyword, Pageable pageable) {
        Page<Customer> customers = customerRepository.globalSearch(companyId, keyword, pageable);
        return customers.map(this::mapToResponse);
    }

    /**
     * Borçlu müşterileri getirir
     */
    public List<CustomerResponse> getCustomersWithDebt(Long companyId) {
        List<Customer> customers = customerRepository.findCustomersWithDebt(companyId);
        return customers.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Customer'ı CustomerResponse'a dönüştürür
     */
    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .contactPerson(customer.getContactPerson())
                .taxNumber(customer.getTaxNumber())
                .identityNumber(customer.getIdentityNumber())
                .taxOffice(customer.getTaxOffice())
                .address(customer.getAddress())
                .city(customer.getCity())
                .district(customer.getDistrict())
                .postalCode(customer.getPostalCode())
                .country(customer.getCountry())
                .phone(customer.getPhone())
                .mobile(customer.getMobile())
                .fax(customer.getFax())
                .email(customer.getEmail())
                .website(customer.getWebsite())
                .isActive(customer.getIsActive())
                .balance(customer.getBalance())
                .notes(customer.getNotes())
                .customerType(customer.getCustomerType())
                .paymentTerms(customer.getPaymentTerms())
                .creditLimit(customer.getCreditLimit())
                .companyId(customer.getCompany().getId())
                .companyName(customer.getCompany().getName())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .createdBy(customer.getCreatedBy())
                .updatedBy(customer.getUpdatedBy())
                .hasDebt(customer.hasDebt())
                .hasCredit(customer.hasCredit())
                .isOverCreditLimit(customer.isOverCreditLimit())
                .build();
    }
}