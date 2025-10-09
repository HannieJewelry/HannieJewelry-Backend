# Hướng dẫn chạy Backend từ IntelliJ với Docker

## Bước 1: Khởi động Docker Services

Mở PowerShell trong thư mục `BackEnd-HannieJewelry` và chạy:

```powershell
# Khởi động chỉ các services cần thiết (DB, Redis, Vault)
docker compose up -d hannie-db redis vault

# Kiểm tra trạng thái
docker compose ps
```

## Bước 2: Cấu hình IntelliJ IDEA

### 2.1 Mở Project trong IntelliJ
- Mở IntelliJ IDEA
- File → Open → Chọn thư mục `BackEnd-HannieJewelry`
- IntelliJ sẽ tự động nhận diện Maven project

### 2.2 Tạo Run Configuration
1. **Mở Run/Debug Configurations:**
   - Run → Edit Configurations...
   - Hoặc click dropdown bên cạnh nút Run và chọn "Edit Configurations..."

2. **Tạo Spring Boot Configuration:**
   - Click "+" → Spring Boot
   - Đặt tên: `HannieJewelry-Local`

3. **Cấu hình chi tiết:**
   - **Main class:** `hanniejewelry.vn.HannieJewelryApplication`
   - **Program arguments:** (để trống)
   - **VM options:** 
     ```
     -Dspring.profiles.active=local
     -Xmx1024m
     -Xms512m
     ```
   - **Environment variables:** (click vào ... để mở)
     ```
     SPRING_PROFILES_ACTIVE=local
     ```
   - **Use classpath of module:** chọn module root
   - **JRE:** Java 21

4. **Apply và OK**

### 2.3 Cấu hình Debug Configuration (nếu cần debug)
1. Tạo thêm configuration mới tương tự trên
2. Đặt tên: `HannieJewelry-Debug`
3. Thêm VM options:
   ```
   -Dspring.profiles.active=local
   -Xmx1024m
   -Xms512m
   -Xdebug
   -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005
   ```

## Bước 3: Chạy Application

### 3.1 Chạy bình thường
1. Chọn configuration `HannieJewelry-Local`
2. Click nút Run (▶️) hoặc Shift+F10

### 3.2 Chạy Debug mode
1. Chọn configuration `HannieJewelry-Debug`
2. Click nút Debug (🐛) hoặc Shift+F9
3. Đặt breakpoint trong code nếu cần

## Bước 4: Kiểm tra kết quả

Sau khi chạy thành công, bạn sẽ thấy:
- Application khởi động trên port 8080 (HTTP)
- Swagger UI có thể truy cập tại: http://localhost:8080/swagger-ui.html
- Database connection thành công
- Redis connection thành công

## Troubleshooting

### Lỗi kết nối Database
```bash
# Kiểm tra container đang chạy
docker compose ps

# Xem logs database
docker compose logs hannie-db

# Restart services nếu cần
docker compose restart hannie-db
```

### Lỗi Port đã được sử dụng
- Kiểm tra application khác đang chạy trên port 8080
- Hoặc thay đổi port trong `application-local.yml`

### Lỗi SSL/Keystore
- File `application-local.yml` đã disable SSL
- Nếu vẫn gặp lỗi, kiểm tra lại configuration

### Lỗi Maven Dependencies
```bash
# Reload Maven project trong IntelliJ
# Hoặc chạy command line:
./mvnw clean install -DskipTests
```

## Dừng Services

Khi hoàn thành:
```powershell
# Dừng tất cả services
docker compose down

# Hoặc chỉ dừng không xóa data
docker compose stop
```