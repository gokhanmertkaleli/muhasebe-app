package com.muhasebe.controller;

import com.muhasebe.dto.request.CustomerRequest;
import com.muhasebe.dto.response.CustomerResponse;
import com.muhasebe.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Müşteri yönetimi için REST Controller
 */
@RestController
@RequestMapping("/api/companies/{companyId}/customers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Yeni müşteri oluştur
     * POST /api/companies/{companyId}/customers
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<?> createCustomer(
            @PathVariable Long companyId,
            @Valid @RequestBody CustomerRequest request) {
        try {
            CustomerResponse response = customerService.createCustomer(companyId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Müşteri güncelle
     * PUT /api/companies/{companyId}/customers/{customerId}
     */
    @PutMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long companyId,
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerRequest request) {
        try {
            CustomerResponse response = customerService.updateCustomer(companyId, customerId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Müşteri sil
     * DELETE /api/companies/{companyId}/customers/{customerId}
     */
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT')")
    public ResponseEntity<?> deleteCustomer(
            @PathVariable Long companyId,
            @PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(companyId, customerId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Müşteri başarıyla silindi");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Müşteri detayı
     * GET /api/companies/{companyId}/customers/{customerId}
     */
    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER', 'VIEWER')")
    public ResponseEntity<?> getCustomer(
            @PathVariable Long companyId,
            @PathVariable Long customerId) {
        try {
            CustomerResponse response = customerService.getCustomer(companyId, customerId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Tüm müşterileri listele (sayfalı)
     * GET /api/companies/{companyId}/customers
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER', 'VIEWER')")
    public ResponseEntity<?> getAllCustomers(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<CustomerResponse> customers = customerService.getAllCustomers(companyId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("customers", customers.getContent());
            response.put("currentPage", customers.getNumber());
            response.put("totalItems", customers.getTotalElements());
            response.put("totalPages", customers.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Aktif müşterileri listele
     * GET /api/companies/{companyId}/customers/active
     */
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER', 'VIEWER')")
    public ResponseEntity<?> getActiveCustomers(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            Page<CustomerResponse> customers = customerService.getActiveCustomers(companyId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("customers", customers.getContent());
            response.put("currentPage", customers.getNumber());
            response.put("totalItems", customers.getTotalElements());
            response.put("totalPages", customers.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Müşteri ara
     * GET /api/companies/{companyId}/customers/search?keyword=...
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER', 'VIEWER')")
    public ResponseEntity<?> searchCustomers(
            @PathVariable Long companyId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            Page<CustomerResponse> customers = customerService.globalSearch(companyId, keyword, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("customers", customers.getContent());
            response.put("currentPage", customers.getNumber());
            response.put("totalItems", customers.getTotalElements());
            response.put("totalPages", customers.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Borçlu müşterileri listele
     * GET /api/companies/{companyId}/customers/with-debt
     */
    @GetMapping("/with-debt")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'VIEWER')")
    public ResponseEntity<?> getCustomersWithDebt(@PathVariable Long companyId) {
        try {
            List<CustomerResponse> customers = customerService.getCustomersWithDebt(companyId);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}