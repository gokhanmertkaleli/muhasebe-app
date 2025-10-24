package com.muhasebe.repository;

import com.muhasebe.entity.User;
import com.muhasebe.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Entity için JPA Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Kullanıcı adına göre kullanıcı bulur
     */
    Optional<User> findByUsername(String username);

    /**
     * Email adresine göre kullanıcı bulur
     */
    Optional<User> findByEmail(String email);

    /**
     * Kullanıcı adı veya email ile kullanıcı bulur
     */
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    /**
     * Kullanıcı adının kullanılıp kullanılmadığını kontrol eder
     */
    boolean existsByUsername(String username);

    /**
     * Email adresinin kullanılıp kullanılmadığını kontrol eder
     */
    boolean existsByEmail(String email);

    /**
     * Şirkete göre kullanıcıları getirir
     */
    @Query("SELECT u FROM User u WHERE u.company.id = :companyId AND u.isDeleted = false")
    List<User> findByCompanyId(@Param("companyId") Long companyId);

    /**
     * Aktif kullanıcıları getirir
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.isDeleted = false")
    List<User> findAllActiveUsers();

    /**
     * Role göre kullanıcıları getirir
     */
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isDeleted = false")
    List<User> findByRole(@Param("role") Role role);

    /**
     * Şirket ve role göre kullanıcıları getirir
     */
    @Query("SELECT u FROM User u WHERE u.company.id = :companyId AND u.role = :role AND u.isDeleted = false")
    List<User> findByCompanyIdAndRole(@Param("companyId") Long companyId, @Param("role") Role role);

    /**
     * Email doğrulaması yapılmamış kullanıcıları getirir
     */
    @Query("SELECT u FROM User u WHERE u.isEmailVerified = false AND u.isDeleted = false")
    List<User> findUnverifiedUsers();

    /**
     * Belirli bir tarihten sonra giriş yapmayan kullanıcıları getirir
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt < :date OR u.lastLoginAt IS NULL")
    List<User> findInactiveUsersSince(@Param("date") java.time.LocalDateTime date);

    /**
     * Kilitli hesapları getirir
     */
    @Query("SELECT u FROM User u WHERE u.lockedUntil > :now AND u.isDeleted = false")
    List<User> findLockedAccounts(@Param("now") java.time.LocalDateTime now);
}