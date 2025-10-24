package com.muhasebe.controller;

import com.muhasebe.dto.request.SupplierRequest;
import com.muhasebe.dto.response.SupplierResponse;
import com.muhasebe.service.SupplierService;
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
import java.util.Map;

/**
 * Tedarikçi yönetimi için REST Controller
 */
@RestController
@RequestMapping("/api/companies/{companyId}/suppliers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * Yeni tedarikçi oluştur
     * POST /api/companies/{companyId}/suppliers
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<?> createSupplier(
            @PathVariable Long companyId,
            @Valid @RequestBody SupplierRequest request) {
        try {
            SupplierResponse response = supplierService.createSupplier(companyId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Tedarikçi güncelle
     * PUT /api/companies/{companyId}/suppliers/{supplierId}
     */
    @PutMapping("/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<?> updateSupplier(
            @PathVariable Long companyId,
            @PathVariable Long supplierId,
            @Valid @RequestBody SupplierRequest request) {
        try {
            SupplierResponse response = supplierService.updateSupplier(companyId, supplierId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Tedarikçi sil
     * DELETE /api/companies/{companyId}/suppliers/{supplierId}
     */
    @DeleteMapping("/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT')")
    public ResponseEntity<?> deleteSupplier(
            @PathVariable Long companyId,
            @PathVariable Long supplierId) {
        try {
            supplierService.deleteSupplier(companyId, supplierId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Tedarikçi başarıyla silindi");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Tedarikçi detayı
     * GET /api/companies/{companyId}/suppliers/{supplierId}
     */
    @GetMapping("/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER', 'VIEWER')")
    public ResponseEntity<?> getSupplier(
            @PathVariable Long companyId,
            @PathVariable Long supplierId) {
        try {
            SupplierResponse response = supplierService.getSupplier(companyId, supplierId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Tüm tedarikçileri listele (sayfalı)
     * GET /api/companies/{companyId}/suppliers
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER', 'VIEWER')")
    public ResponseEntity<?> getAllSuppliers(
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
            Page<SupplierResponse> suppliers = supplierService.getAllSuppliers(companyId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("suppliers", suppliers.getContent());
            response.put("currentPage", suppliers.getNumber());
            response.put("totalItems", suppliers.getTotalElements());
            response.put("totalPages", suppliers.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * Tedarikçi ara
     * GET /api/companies/{companyId}/suppliers/search?keyword=...
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'ACCOUNTANT', 'USER', 'VIEWER')")
    public ResponseEntity<?> searchSuppliers(
            @PathVariable Long companyId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            Page<SupplierResponse> suppliers = supplierService.globalSearch(companyId, keyword, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("suppliers", suppliers.getContent());
            response.put("currentPage", suppliers.getNumber());
            response.put("totalItems", suppliers.getTotalElements());
            response.put("totalPages", suppliers.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}