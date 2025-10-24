package com.muhasebe.enums;

/**
 * Kullanıcı Rol Enum'u
 * Sistemdeki kullanıcı rollerini tanımlar
 */
public enum Role {
    /**
     * Sistem yöneticisi - Tüm yetkilere sahip
     */
    ADMIN("Yönetici", "Tüm yetkilere sahip sistem yöneticisi"),

    /**
     * Muhasebe personeli - Muhasebe işlemlerini yapabilir
     */
    ACCOUNTANT("Muhasebeci", "Muhasebe işlemlerini gerçekleştirebilir"),

    /**
     * Normal kullanıcı - Sınırlı yetkiler
     */
    USER("Kullanıcı", "Temel işlemleri gerçekleştirebilir"),

    /**
     * Görüntüleyici - Sadece okuma yetkisi
     */
    VIEWER("Görüntüleyici", "Sadece raporları görüntüleyebilir"),

    /**
     * Şirket sahibi - Şirket yönetimi yetkileri
     */
    OWNER("Şirket Sahibi", "Şirket yönetimi ve tüm işlemleri yapabilir");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * String değerden Role enum'una dönüştürür
     */
    public static Role fromString(String role) {
        if (role == null) {
            return USER;
        }
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return USER;
        }
    }

    /**
     * Role'ün yönetici seviyesinde olup olmadığını kontrol eder
     */
    public boolean isAdmin() {
        return this == ADMIN || this == OWNER;
    }

    /**
     * Role'ün muhasebe işlemleri yapabilecek seviyede olup olmadığını kontrol eder
     */
    public boolean canManageAccounting() {
        return this == ADMIN || this == OWNER || this == ACCOUNTANT;
    }

    /**
     * Role'ün sadece okuma yetkisi olup olmadığını kontrol eder
     */
    public boolean isViewOnly() {
        return this == VIEWER;
    }
}