-- Örnek test verileri
-- Bu dosya manuel olarak çalıştırılabilir veya application.properties'e eklenebilir

-- Örnek Şirket
INSERT INTO companies (name, trade_name, tax_number, tax_office, address, city, country, phone, email, is_active, created_at)
VALUES ('Örnek Ticaret A.Ş.', 'Örnek Ticaret', '1234567890', 'Kadıköy', 'Atatürk Cad. No:1', 'İstanbul', 'Türkiye', '0216 123 45 67', 'info@ornek.com', true, NOW())
ON CONFLICT (tax_number) DO NOTHING;

-- Örnek Kullanıcı (şifre: 123456)
INSERT INTO users (username, email, password, first_name, last_name, role, is_active, company_id, created_at, failed_login_attempts)
SELECT 'admin', 'admin@ornek.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5Y5myX7Aj6q7q', 'Admin', 'User', 'ADMIN', true, id, NOW(), 0
FROM companies WHERE tax_number = '1234567890'
ON CONFLICT (username) DO NOTHING;
