package com.muhasebe.repository;

import com.muhasebe.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByTaxNumber(String taxNumber);

    boolean existsByTaxNumber(String taxNumber);

    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND c.isDeleted = false")
    List<Company> searchByName(@Param("name") String name);

    @Query("SELECT c FROM Company c WHERE c.isActive = true AND c.isDeleted = false")
    List<Company> findAllActiveCompanies();

    @Query("SELECT c FROM Company c WHERE c.eInvoiceEnabled = true AND c.isDeleted = false")
    List<Company> findEInvoiceEnabledCompanies();

    @Query("SELECT c FROM Company c WHERE c.eArchiveEnabled = true AND c.isDeleted = false")
    List<Company> findEArchiveEnabledCompanies();

    @Query("SELECT c FROM Company c WHERE c.city = :city AND c.isDeleted = false")
    List<Company> findByCity(@Param("city") String city);

    @Query("SELECT c FROM Company c WHERE c.taxOffice = :taxOffice AND c.isDeleted = false")
    List<Company> findByTaxOffice(@Param("taxOffice") String taxOffice);

    @Query("SELECT c FROM Company c WHERE c.companyType = :companyType AND c.isDeleted = false")
    List<Company> findByCompanyType(@Param("companyType") String companyType);
}