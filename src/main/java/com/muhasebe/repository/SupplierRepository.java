package com.muhasebe.repository;

import com.muhasebe.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND s.isDeleted = false")
    Page<Supplier> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND s.isDeleted = false")
    List<Supplier> findAllByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND s.isActive = true AND s.isDeleted = false")
    Page<Supplier> findActiveSuppliersByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT s FROM Supplier s WHERE s.taxNumber = :taxNumber AND s.company.id = :companyId AND s.isDeleted = false")
    Optional<Supplier> findByTaxNumberAndCompanyId(@Param("taxNumber") String taxNumber, @Param("companyId") Long companyId);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.isDeleted = false")
    Page<Supplier> searchByName(@Param("companyId") Long companyId, @Param("name") String name, Pageable pageable);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND s.city = :city AND s.isDeleted = false")
    Page<Supplier> findByCity(@Param("companyId") Long companyId, @Param("city") String city, Pageable pageable);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND s.supplierType = :supplierType AND s.isDeleted = false")
    Page<Supplier> findBySupplierType(@Param("companyId") Long companyId, @Param("supplierType") String supplierType, Pageable pageable);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND s.balance < 0 AND s.isDeleted = false")
    List<Supplier> findSuppliersWithDebt(@Param("companyId") Long companyId);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND s.balance > 0 AND s.isDeleted = false")
    List<Supplier> findSuppliersWithCredit(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(s) > 0 FROM Supplier s WHERE s.taxNumber = :taxNumber AND s.company.id = :companyId AND s.isDeleted = false")
    boolean existsByTaxNumberAndCompanyId(@Param("taxNumber") String taxNumber, @Param("companyId") Long companyId);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.company WHERE s.company.id = :companyId AND " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.taxNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "s.isDeleted = false")
    Page<Supplier> globalSearch(@Param("companyId") Long companyId, @Param("keyword") String keyword, Pageable pageable);
}