# Muhasebe App - Postman Test Koleksiyonu

Bu klasör, Muhasebe uygulaması için hazırlanmış Postman test koleksiyonunu içerir.

## 📥 Import Etme

1. Postman'i açın
2. `Import` butonuna tıklayın
3. `Muhasebe-App-Tests.json` dosyasını seçin
4. Import işlemi tamamlandığında koleksiyon kullanıma hazır olacaktır

## 🔧 Değişkenler (Variables)

Koleksiyon aşağıdaki değişkenleri kullanır ve otomatik olarak günceller:

- `baseUrl`: API base URL (varsayılan: `http://localhost:8080`)
- `accessToken`: Login sonrası otomatik doldurulur
- `refreshToken`: Login sonrası otomatik doldurulur
- `companyId`: Register/Login sonrası otomatik doldurulur
- `customerId`: Customer oluşturulunca otomatik doldurulur
- `supplierId`: Supplier oluşturulunca otomatik doldurulur

## 📋 Test Sırası

### 1️⃣ Authentication (Kimlik Doğrulama)

**İlk Adım - Kayıt:**
```
POST /api/auth/register
```
- Admin veya normal kullanıcı kaydı yapın
- Token ve companyId otomatik kaydedilir

**Alternatif - Giriş:**
```
POST /api/auth/login
```
- Mevcut kullanıcı ile giriş yapın
- Token ve companyId otomatik kaydedilir

### 2️⃣ Customers (Müşteriler)

**Müşteri Oluştur:**
```
POST /api/companies/{companyId}/customers
```
✅ İki örnek hazır:
- Kurumsal müşteri (vergi numarası ile)
- Bireysel müşteri (TC kimlik no ile)

**Tüm Müşterileri Listele:**
```
GET /api/companies/{companyId}/customers?page=0&size=10
```

**Müşteri Detayı:**
```
GET /api/companies/{companyId}/customers/{customerId}
```

**Müşteri Güncelle:**
```
PUT /api/companies/{companyId}/customers/{customerId}
```

**Müşteri Ara:**
```
GET /api/companies/{companyId}/customers/search?keyword=ABC
```

**Aktif Müşteriler:**
```
GET /api/companies/{companyId}/customers/active
```

**Borçlu Müşteriler:**
```
GET /api/companies/{companyId}/customers/with-debt
```

**Müşteri Sil:**
```
DELETE /api/companies/{companyId}/customers/{customerId}
```

### 3️⃣ Suppliers (Tedarikçiler)

**Tedarikçi Oluştur:**
```
POST /api/companies/{companyId}/suppliers
```
✅ Banka bilgileri ve IBAN ile örnek hazır

**Tüm Tedarikçileri Listele:**
```
GET /api/companies/{companyId}/suppliers?page=0&size=10
```

**Tedarikçi Detayı:**
```
GET /api/companies/{companyId}/suppliers/{supplierId}
```

**Tedarikçi Güncelle:**
```
PUT /api/companies/{companyId}/suppliers/{supplierId}
```

**Tedarikçi Ara:**
```
GET /api/companies/{companyId}/suppliers/search?keyword=XYZ
```

**Aktif Tedarikçiler:**
```
GET /api/companies/{companyId}/suppliers/active
```

**Tedarikçi Sil:**
```
DELETE /api/companies/{companyId}/suppliers/{supplierId}
```

## 🎯 Hızlı Test Senaryosu

1. **Register - Admin User** çalıştırın (token alır)
2. **Create Customer - Corporate** çalıştırın (customerId alır)
3. **Create Supplier** çalıştırın (supplierId alır)
4. **Get All Customers** ile müşterileri listeleyin
5. **Search Customers** ile arama yapın
6. **Update Customer** ile müşteriyi güncelleyin

## ✅ Otomatik Test Scripts

Bazı request'lerde otomatik test script'leri vardır:

- **Login/Register**: Token ve ID'leri otomatik kaydeder
- **Create Customer/Supplier**: ID'leri otomatik kaydeder
- **Status Code**: 200, 201 gibi başarı kodlarını kontrol eder

## 🔐 Authentication

Tüm API request'leri (register ve login hariç) aşağıdaki header'ı gerektirir:

```
Authorization: Bearer {{accessToken}}
```

Bu header otomatik olarak eklenir, manuel işlem gerekmez.

## 📊 Response Format

### Başarılı Response (200/201):
```json
{
  "id": 1,
  "name": "ABC Ticaret Ltd.",
  "taxNumber": "1234567891",
  ...
}
```

### Hata Response (400/404/500):
```json
{
  "message": "Hata mesajı"
}
```

## 🐛 Sorun Giderme

### Token Geçersiz Hatası:
- **Login** request'ini tekrar çalıştırın
- Token süresi dolmuş olabilir (24 saat)

### 404 Not Found:
- `companyId`, `customerId` veya `supplierId` değişkenlerini kontrol edin
- İlgili create endpoint'ini çalıştırın

### 400 Bad Request:
- Request body'de zorunlu alanları kontrol edin
- JSON formatının doğru olduğundan emin olun

### 403 Forbidden:
- Kullanıcı yetkisini kontrol edin
- ADMIN, ACCOUNTANT gibi roller gerekebilir

## 📝 Örnek Veriler

### Kurumsal Müşteri:
```json
{
  "name": "ABC Ticaret Ltd. Şti.",
  "taxNumber": "1234567891",
  "customerType": "Kurumsal",
  "paymentTerms": 30,
  "creditLimit": 50000.00
}
```

### Bireysel Müşteri:
```json
{
  "name": "Mehmet Demir",
  "identityNumber": "12345678901",
  "customerType": "Bireysel",
  "paymentTerms": 15,
  "creditLimit": 10000.00
}
```

### Tedarikçi:
```json
{
  "name": "XYZ Tedarik A.Ş.",
  "taxNumber": "9876543210",
  "supplierType": "Distribütör",
  "paymentTerms": 60,
  "iban": "TR330006100519786457841326"
}
```

## 🎨 İpuçları

1. **Environment kullanın**: Production/Development için farklı environment'lar oluşturun
2. **Collection Runner**: Tüm testleri otomatik çalıştırmak için Runner'ı kullanın
3. **Save Response**: Response'ları kaydedin, karşılaştırma yaparken işe yarar
4. **Pre-request Scripts**: Dinamik veri üretmek için kullanabilirsiniz

## 📞 Destek

Sorun yaşarsanız:
1. Application loglarını kontrol edin: `logs/muhasebe-app.log`
2. Postman Console'u açın: `View > Show Postman Console`
3. Request/Response detaylarını inceleyin
