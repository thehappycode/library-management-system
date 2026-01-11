# Hướng Dẫn Sử Dụng Flyway Database Migration

## 📚 Flyway là gì?

**Flyway** là công cụ quản lý database schema migration, giúp:

- ✅ Tự động tạo và cập nhật cấu trúc database
- ✅ Version control cho database schema
- ✅ Đồng bộ database giữa các môi trường (dev, test, prod)
- ✅ Rollback khi cần thiết
- ✅ Track lịch sử thay đổi database

## 🏗️ Cấu Trúc Migration Files

```
src/main/resources/
└── db/
    └── migration/
        ├── V1__Create_categories_table.sql
        ├── V2__Create_books_table.sql
        └── V3__Insert_sample_books.sql
```

### 📝 Quy Tắc Đặt Tên File

**Format:** `V{version}__{description}.sql`

- **V** (bắt buộc): Prefix cho versioned migration
- **{version}**: Số version (1, 2, 3, hoặc 1.0, 1.1, 2.0, ...)
- **\_\_** (hai dấu gạch dưới): Separator
- **{description}**: Mô tả ngắn gọn (dùng snake_case hoặc PascalCase)
- **.sql**: Extension

**Ví dụ:**

```
V1__Create_categories_table.sql       ✅ Đúng
V2__Create_books_table.sql            ✅ Đúng
V1.0__Initial_setup.sql               ✅ Đúng
V2.1__Add_email_column.sql            ✅ Đúng

V1_Create_table.sql                   ❌ Sai (chỉ 1 dấu gạch dưới)
create_table.sql                      ❌ Sai (thiếu version)
V1__create-table.sql                  ❌ Không nên (dùng dấu gạch ngang)
```

## ⚙️ Cấu Hình Flyway

Trong `application.yml`:

```yaml
spring:
  flyway:
    enabled: true # Bật Flyway
    locations: classpath:db/migration # Thư mục chứa migration files
    baseline-on-migrate: true # Tự động baseline nếu DB đã có dữ liệu
    baseline-version: 0 # Version của baseline
    validate-on-migrate: true # Validate trước khi migrate
    out-of-order: false # Không cho phép chạy migration không theo thứ tự
```

## 🚀 Cách Flyway Hoạt Động

### Lần Chạy Đầu Tiên:

1. Flyway tạo bảng `flyway_schema_history` để track migrations
2. Quét tất cả file trong `db/migration/`
3. Chạy các file theo thứ tự version (V1 → V2 → V3 → ...)
4. Lưu thông tin vào `flyway_schema_history`

### Các Lần Chạy Tiếp Theo:

1. Kiểm tra `flyway_schema_history` để xem migration nào đã chạy
2. Chỉ chạy các migration mới (chưa có trong history)
3. Cập nhật `flyway_schema_history`

## 📊 Bảng flyway_schema_history

Sau khi chạy, bạn sẽ thấy bảng này trong database:

```sql
SELECT * FROM flyway_schema_history;
```

| installed_rank | version | description             | type | script                            | checksum   | installed_on        | success |
| -------------- | ------- | ----------------------- | ---- | --------------------------------- | ---------- | ------------------- | ------- |
| 1              | 1       | Create categories table | SQL  | V1\_\_Create_categories_table.sql | 1234567890 | 2026-01-11 10:00:00 | 1       |
| 2              | 2       | Create books table      | SQL  | V2\_\_Create_books_table.sql      | 9876543210 | 2026-01-11 10:00:01 | 1       |
| 3              | 3       | Insert sample books     | SQL  | V3\_\_Insert_sample_books.sql     | 5555555555 | 2026-01-11 10:00:02 | 1       |

## 🎯 Cách Chạy Application

### 1. Đảm bảo MySQL đang chạy

```bash
# Kiểm tra MySQL
mysql -u root -p -e "SELECT VERSION();"

# Hoặc dùng Docker
docker ps | grep mysql
```

### 2. Chạy Application

**Môi trường DEV:**

```bash
cd services/book-service
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Môi trường PROD:**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

**Với biến môi trường:**

```bash
DB_USERNAME=root \
DB_PASSWORD=yourpassword \
SPRING_PROFILES_ACTIVE=dev \
mvn spring-boot:run
```

### 3. Kiểm tra Migration đã chạy

Trong log, bạn sẽ thấy:

```
INFO  o.f.c.i.d.DbMigrate - Successfully applied 3 migrations
INFO  o.f.c.i.d.DbMigrate - Schema history table created: bookdb.flyway_schema_history
```

### 4. Kiểm tra Database

```bash
# Kết nối MySQL
mysql -u root -p -h localhost -P 3307

# Chọn database
USE bookdb;

# Kiểm tra các bảng
SHOW TABLES;

# Xem dữ liệu
SELECT * FROM categories;
SELECT * FROM books;
SELECT * FROM flyway_schema_history;
```

## 📝 Thêm Migration Mới

### Ví dụ: Thêm cột `publisher` vào bảng `books`

**Bước 1:** Tạo file migration mới

`V4__Add_publisher_to_books.sql`

```sql
-- Add publisher column to books table
ALTER TABLE books
ADD COLUMN publisher VARCHAR(200) AFTER author_name;

-- Add index for publisher
CREATE INDEX idx_publisher ON books(publisher);

-- Update existing books with default publisher
UPDATE books
SET publisher = 'Unknown Publisher'
WHERE publisher IS NULL;
```

**Bước 2:** Restart application

Flyway sẽ tự động:

1. Phát hiện file V4
2. Chạy migration
3. Cập nhật `flyway_schema_history`

## 🔧 Các Loại Migration

### 1. Versioned Migration (V)

- File: `V{version}__{description}.sql`
- Chạy một lần duy nhất
- Không được sửa sau khi đã apply
- Dùng cho: CREATE, ALTER, DROP, INSERT

### 2. Repeatable Migration (R)

- File: `R__{description}.sql`
- Chạy lại mỗi khi checksum thay đổi
- Dùng cho: VIEW, PROCEDURE, FUNCTION

**Ví dụ:**

```sql
-- R__Create_book_stats_view.sql
CREATE OR REPLACE VIEW book_stats AS
SELECT
    category_id,
    COUNT(*) as total_books,
    SUM(available_quantity) as total_available
FROM books
GROUP BY category_id;
```

### 3. Undo Migration (U) - Rollback

- File: `U{version}__{description}.sql`
- Dùng để rollback migration
- Cần Flyway Teams (trả phí)

## ⚠️ Lưu Ý Quan Trọng

### ❌ KHÔNG BAO GIỜ:

1. **Sửa file migration đã chạy** - Flyway dùng checksum để verify
2. **Xóa file migration đã chạy** - Sẽ gây lỗi validation
3. **Thay đổi thứ tự version** - V5 không thể chạy trước V4
4. **Commit migration bị lỗi** - Phải fix và commit lại

### ✅ NÊN:

1. **Test migration trên local trước**
2. **Backup database trước khi migration production**
3. **Viết migration có thể rollback được** (nếu cần)
4. **Dùng transaction khi có thể**
5. **Commit migration vào Git**

## 🐛 Xử Lý Lỗi

### Lỗi: Migration checksum mismatch

**Nguyên nhân:** File migration đã được sửa sau khi apply

**Giải pháp 1:** Repair (khuyến nghị cho dev)

```bash
mvn flyway:repair
```

**Giải pháp 2:** Tạo migration mới để fix

```sql
-- V5__Fix_previous_migration.sql
-- Thêm các thay đổi mới thay vì sửa file cũ
```

### Lỗi: Migration failed

**Kiểm tra:**

1. Log để xem lỗi SQL cụ thể
2. Bảng `flyway_schema_history` - cột `success = 0`
3. Fix lỗi trong SQL
4. Xóa record lỗi trong `flyway_schema_history` hoặc dùng `flyway:repair`
5. Chạy lại

### Lỗi: Baseline needed

**Giải pháp:** Đã config `baseline-on-migrate: true` rồi, nhưng nếu vẫn lỗi:

```bash
mvn flyway:baseline
```

## 🔍 Flyway Maven Commands

```bash
# Xem thông tin migration
mvn flyway:info

# Validate migrations
mvn flyway:validate

# Migrate (apply pending migrations)
mvn flyway:migrate

# Clean database (⚠️ XÓA TẤT CẢ - chỉ dùng dev!)
mvn flyway:clean

# Repair metadata table
mvn flyway:repair

# Baseline existing database
mvn flyway:baseline
```

## 📚 Best Practices

### 1. Cấu Trúc Migration Tốt

```
V1__Create_schema.sql              # Tạo các bảng cơ bản
V2__Create_indexes.sql             # Tạo indexes
V3__Insert_reference_data.sql      # Insert dữ liệu tham chiếu
V4__Add_constraints.sql            # Thêm constraints
V5__Create_views.sql               # Tạo views
V10__Add_feature_X.sql             # Feature mới
V11__Add_feature_Y.sql             # Feature mới
```

### 2. Viết SQL An Toàn

```sql
-- ✅ Dùng IF NOT EXISTS
CREATE TABLE IF NOT EXISTS books (...);

-- ✅ Dùng IF EXISTS cho DROP
DROP TABLE IF EXISTS old_table;

-- ✅ Dùng transaction cho multiple operations
START TRANSACTION;
    ALTER TABLE ...;
    UPDATE ...;
COMMIT;

-- ✅ Backup trước khi DELETE/UPDATE lớn
CREATE TABLE books_backup AS SELECT * FROM books;
```

### 3. Naming Convention

```
V1__Create_{table_name}_table.sql
V2__Add_{column_name}_to_{table_name}.sql
V3__Modify_{column_name}_in_{table_name}.sql
V4__Drop_{table_name}_table.sql
V5__Insert_{data_type}_data.sql
```

## 🎓 Tài Liệu Tham Khảo

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Flyway Migrations](https://flywaydb.org/documentation/concepts/migrations)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)

## ✨ Checklist Khi Tạo Migration Mới

- [ ] Đặt tên file đúng format `V{n}__{description}.sql`
- [ ] Version number tăng dần so với migration gần nhất
- [ ] Viết SQL an toàn (IF NOT EXISTS, IF EXISTS)
- [ ] Test trên local database trước
- [ ] Kiểm tra không có lỗi syntax
- [ ] Commit vào Git
- [ ] Document trong code review (nếu có)
- [ ] Test trên staging trước khi deploy production

---

**Chúc bạn thành công! 🚀**
