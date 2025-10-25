# cURL Komut Örnekleri

Terminal veya Command Prompt üzerinden API testleri için cURL komutları.

## 🔐 Authentication

### Register (Kayıt)
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@muhasebe.com",
    "password": "Test123!",
    "firstName": "Test",
    "lastName": "User",
    "phone": "05551234567"
  }'
```

### Login (Giriş)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "Test123!"
  }'
```

**Response'dan alacağınız değerler:**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "companyId": 1,
  "username": "testuser",
  ...
}
```

## 👥 Customers (Müşteriler)

### Müşteri Oluştur
```bash
# TOKEN ve COMPANY_ID değişkenlerini yukarıdaki login response'undan alın
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
COMPANY_ID=1

curl -X POST http://localhost:8080/api/companies/$COMPANY_ID/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "ABC Ticaret Ltd. Şti.",
    "contactPerson": "Ahmet Yılmaz",
    "taxNumber": "1234567891",
    "taxOffice": "Kadıköy",
    "address": "Sahil Caddesi No:15",
    "city": "İstanbul",
    "district": "Kadıköy",
    "phone": "0216 555 1234",
    "mobile": "0532 123 4567",
    "email": "abc@ticaret.com",
    "customerType": "Kurumsal",
    "paymentTerms": 30,
    "creditLimit": 50000.00
  }'
```

### Tüm Müşterileri Listele
```bash
curl -X GET "http://localhost:8080/api/companies/$COMPANY_ID/customers?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

### Müşteri Detayı
```bash
CUSTOMER_ID=1

curl -X GET http://localhost:8080/api/companies/$COMPANY_ID/customers/$CUSTOMER_ID \
  -H "Authorization: Bearer $TOKEN"
```

### Müşteri Güncelle
```bash
curl -X PUT http://localhost:8080/api/companies/$COMPANY_ID/customers/$CUSTOMER_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "ABC Ticaret Ltd. (Güncellendi)",
    "taxNumber": "1234567891",
    "taxOffice": "Kadıköy",
    "address": "Yeni Adres",
    "city": "İstanbul",
    "phone": "0216 555 9999",
    "email": "abc@ticaret.com",
    "customerType": "Kurumsal",
    "paymentTerms": 45
  }'
```

### Müşteri Ara
```bash
curl -X GET "http://localhost:8080/api/companies/$COMPANY_ID/customers/search?keyword=ABC" \
  -H "Authorization: Bearer $TOKEN"
```

### Aktif Müşteriler
```bash
curl -X GET "http://localhost:8080/api/companies/$COMPANY_ID/customers/active?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

### Borçlu Müşteriler
```bash
curl -X GET http://localhost:8080/api/companies/$COMPANY_ID/customers/with-debt \
  -H "Authorization: Bearer $TOKEN"
```

### Müşteri Sil
```bash
curl -X DELETE http://localhost:8080/api/companies/$COMPANY_ID/customers/$CUSTOMER_ID \
  -H "Authorization: Bearer $TOKEN"
```

## 🏢 Suppliers (Tedarikçiler)

### Tedarikçi Oluştur
```bash
curl -X POST http://localhost:8080/api/companies/$COMPANY_ID/suppliers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "XYZ Tedarik A.Ş.",
    "contactPerson": "Ayşe Kaya",
    "taxNumber": "9876543210",
    "taxOffice": "Beşiktaş",
    "address": "Deniz Sokak No:10",
    "city": "İstanbul",
    "district": "Beşiktaş",
    "phone": "0212 555 6789",
    "mobile": "0535 678 9012",
    "email": "xyz@tedarik.com",
    "supplierType": "Distribütör",
    "paymentTerms": 60,
    "bankName": "İş Bankası",
    "iban": "TR330006100519786457841326"
  }'
```

### Tüm Tedarikçileri Listele
```bash
curl -X GET "http://localhost:8080/api/companies/$COMPANY_ID/suppliers?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

### Tedarikçi Detayı
```bash
SUPPLIER_ID=1

curl -X GET http://localhost:8080/api/companies/$COMPANY_ID/suppliers/$SUPPLIER_ID \
  -H "Authorization: Bearer $TOKEN"
```

### Tedarikçi Güncelle
```bash
curl -X PUT http://localhost:8080/api/companies/$COMPANY_ID/suppliers/$SUPPLIER_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "XYZ Tedarik A.Ş. (Güncellendi)",
    "taxNumber": "9876543210",
    "taxOffice": "Beşiktaş",
    "address": "Yeni Adres",
    "city": "İstanbul",
    "phone": "0212 555 9999",
    "email": "xyz@tedarik.com",
    "supplierType": "Üretici",
    "paymentTerms": 90
  }'
```

### Tedarikçi Ara
```bash
curl -X GET "http://localhost:8080/api/companies/$COMPANY_ID/suppliers/search?keyword=XYZ" \
  -H "Authorization: Bearer $TOKEN"
```

### Aktif Tedarikçiler
```bash
curl -X GET "http://localhost:8080/api/companies/$COMPANY_ID/suppliers/active?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"
```

### Tedarikçi Sil
```bash
curl -X DELETE http://localhost:8080/api/companies/$COMPANY_ID/suppliers/$SUPPLIER_ID \
  -H "Authorization: Bearer $TOKEN"
```

## 🔄 Token Yenileme

### Refresh Token Kullanımı
```bash
REFRESH_TOKEN="eyJhbGc..."

curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d "{\"refreshToken\": \"$REFRESH_TOKEN\"}"
```

## 📊 Pretty Print (Formatted Output)

### jq ile formatlanmış çıktı (Linux/Mac)
```bash
curl -X GET http://localhost:8080/api/companies/$COMPANY_ID/customers \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

### Python ile formatlanmış çıktı (Windows)
```bash
curl -X GET http://localhost:8080/api/companies/$COMPANY_ID/customers ^
  -H "Authorization: Bearer %TOKEN%" | python -m json.tool
```

## 🎯 Hızlı Test Scripti

### Bash Script (Linux/Mac)
```bash
#!/bin/bash

# 1. Login
echo "=== LOGIN ==="
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "Test123!"
  }')

TOKEN=$(echo $RESPONSE | jq -r '.accessToken')
COMPANY_ID=$(echo $RESPONSE | jq -r '.companyId')

echo "Token: $TOKEN"
echo "Company ID: $COMPANY_ID"

# 2. Create Customer
echo ""
echo "=== CREATE CUSTOMER ==="
curl -X POST http://localhost:8080/api/companies/$COMPANY_ID/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Test Customer",
    "taxNumber": "1111111111",
    "city": "İstanbul",
    "phone": "0216 555 0000"
  }' | jq '.'

# 3. List Customers
echo ""
echo "=== LIST CUSTOMERS ==="
curl -X GET "http://localhost:8080/api/companies/$COMPANY_ID/customers?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

### Batch Script (Windows)
```batch
@echo off

REM 1. Login
echo === LOGIN ===
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\": \"testuser\", \"password\": \"Test123!\"}" > temp.json

REM Token ve Company ID'yi manuel olarak temp.json'dan alın
set /p TOKEN="Token girin: "
set /p COMPANY_ID="Company ID girin: "

REM 2. Create Customer
echo.
echo === CREATE CUSTOMER ===
curl -X POST http://localhost:8080/api/companies/%COMPANY_ID%/customers ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"name\": \"Test Customer\", \"taxNumber\": \"1111111111\", \"city\": \"Istanbul\"}"

REM 3. List Customers
echo.
echo === LIST CUSTOMERS ===
curl -X GET "http://localhost:8080/api/companies/%COMPANY_ID%/customers?page=0&size=10" ^
  -H "Authorization: Bearer %TOKEN%"
```

## 💡 İpuçları

1. **Windows PowerShell**: `\` yerine `` ` `` (backtick) kullanın
2. **Windows CMD**: `\` yerine `^` kullanın
3. **JSON escape**: Windows'ta çift tırnakları `\"` ile escape edin
4. **Response kaydetme**: `> output.json` ekleyin
5. **Verbose mode**: `-v` parametresi ekleyin (debug için)

## 🐛 Debug

### Detaylı çıktı için
```bash
curl -v -X GET http://localhost:8080/api/companies/$COMPANY_ID/customers \
  -H "Authorization: Bearer $TOKEN"
```

### Sadece HTTP status code
```bash
curl -s -o /dev/null -w "%{http_code}" \
  http://localhost:8080/api/companies/$COMPANY_ID/customers \
  -H "Authorization: Bearer $TOKEN"
```

### Response header'larını göster
```bash
curl -i -X GET http://localhost:8080/api/companies/$COMPANY_ID/customers \
  -H "Authorization: Bearer $TOKEN"
```
