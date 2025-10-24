package com.muhasebe.repository;

import com.muhasebe.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND c.isDeleted = false")
    Page<Customer> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND c.isDeleted = false")
    List<Customer> findAllByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND c.isActive = true AND c.isDeleted = false")
    Page<Customer> findActiveCustomersByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.taxNumber = :taxNumber AND c.company.id = :companyId AND c.isDeleted = false")
    Optional<Customer> findByTaxNumberAndCompanyId(@Param("taxNumber") String taxNumber, @Param("companyId") Long companyId);

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND c.isDeleted = false")
    Page<Customer> searchByName(@Param("companyId") Long companyId, @Param("name") String name, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND c.city = :city AND c.isDeleted = false")
    Page<Customer> findByCity(@Param("companyId") Long companyId, @Param("city") String city, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND c.customerType = :customerType AND c.isDeleted = false")
    Page<Customer> findByCustomerType(@Param("companyId") Long companyId, @Param("customerType") String customerType, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND c.balance < 0 AND c.isDeleted = false")
    List<Customer> findCustomersWithDebt(@Param("companyId") Long companyId);

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND c.balance > 0 AND c.isDeleted = false")
    List<Customer> findCustomersWithCredit(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.taxNumber = :taxNumber AND c.company.id = :companyId AND c.isDeleted = false")
    boolean existsByTaxNumberAndCompanyId(@Param("taxNumber") String taxNumber, @Param("companyId") Long companyId);

    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId AND " +
            "(LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.taxNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "c.isDeleted = false")
    Page<Customer> globalSearch(@Param("companyId") Long companyId, @Param("keyword") String keyword, Pageable pageable);
}