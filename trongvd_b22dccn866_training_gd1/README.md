# TYP Training 2025

## Vương Đức Trọng

### Week 1

#### [I. Create backend project](source)

```
- Dependencies:
    1. lombok
    2. spring-boot-web-test
    3. jdbc
    4. jpa
    5. postgres

- Techs:
    1. PotgreSQL
    2. Pgadmin

- Tools: 
    1. Docker-desktop:
    2. Intellij
    3. Vscode

- Guide:
    1. git clone my-repo
    2. cd source
    3. docker-compose up -d : to turn on containers
    4. into docker-desktop click link: 5050:80 to see UI db 
    5. Pgadmin UI => Enter username : admin@admin.com
                           Password : admin
    6. Enter name server : Postgres Server (custom)
       Host name : postgres_db
       port : 5432
       name db : management
       username : postgres
       password : postgres
    7. docker-compose down : to turn off containers
```
![alt text](source/images/docker.png)

#### [II. Create database: management (QuanLySinhVien)](source/database)
[1. Intital Tables](source/database/intitial_database.sql)
```
- Tables:
    1. Khoa
    2. KhoaHoc
    3. ChuongTrinhHoc
    4. Lop
    5. SinhVien
    6. MonHoc
    7. KetQua
    8. GiangKhoa
```
[2. ERD For Database](source/images/ERD.png)

![alt text](source/images/ERD.png)

[3. Insert Data](source/database/insert_data.sql)

[4. Query Data](source/database/query_data.sql)

[5. Query Function](source/database/function.sql)
```
    Function: Tổ chức và thực thi mã lệnh một cách hiệu quả, có cấu trúc, và có thể tái sử dụng
```
```
    Syntax: plpgsql
    CREATE OR REPLACE FUNCTION [NAME_FUNCTION](
        p_[NAME_ATTRIBUTE] [DATA_TYPE]
    )
    LANGUAGE plpgsql
    AS $$
    DECLARE
        [NAME_VALUE] [DATA_TYPE]
    BEGIN
        <Process Code>
    END;
    $$;
```

[6. Query Procerdure](source/database/procerdure.sql)
```
Store Procerdure: Stored procedures thường được sử dụng trong các hệ thống lớn, nơi mà việc quản lý hiệu suất, bảo mật và tính toàn vẹn dữ liệu là rất quan trọng.
    - Lưu trữ chương trình hay thủ tục để tái sử dụng đoạn code
    - Thực hiện lệnh Transact-SQL EXECUTE (EXEC) thực thi các stored procedure.  
    - Store procedure khác với các hàm xử lý là giá trị trả về của chúng
    - Không chứa trong tên và chúng không được sử dụng trực tiếp trong biểu thức
    
    + Động: Có thể chỉnh sửa khối lệnh, tái sử dụng nhiều lần
    + Nhanh hơn: Tự phân tích cú pháp cho tối ưu. Và tạo bản sao để lần chạy sau không cần thực thi lại từ đầu.  
    + Bảo mật: Giới hạn quyền cho user nào sử dụng user nào không 
    + Giảm bandwidth: với các gói transaction lớn. Vài ngàn dòng lệnh một lúc thì dùng store sẽ đảm bảo.  

```
```
    Syntax: plpgsql
    CREATE OR REPLACE PROCERDURE [NAME_PROCERDURE](
        p_[NAME_ATTRIBUTE] [DATA_TYPE]
    )
    LANGUAGE plpgsql
    AS $$
    DECLARE
        [NAME_VALUE] [DATA_TYPE]
    BEGIN
        <Process Code>
    END;
    $$;

```

[7. Query Trigger](source/database/trigger.sql)
```
Triggers giúp tự động hóa các công việc kiểm tra ràng buộc, duy trì tính toàn vẹn của dữ liệu, hoặc ghi lại các thay đổi trong cơ sở dữ liệu.
    - Trigger sẽ được gọi mỗi khi có thao tác thay đổi thông tin bảng
    - Inserted: Chứa những trường đã insert | update vào bảng
    - Deleted: Chứa những trường đã bị xóa khỏi bảng

```
```
    Syntax: plpgsql
    CREATE OR REPLACE FUNCTION [NAME_TRIGGER_FUNCTION]()
    RETURNS TRIGGER
    LANGUAGE plpgsql
    AS $$
    BEGIN
        <process code>
        RETURN NEW / OLD;
    END;
    $$;

    CREATE TRIGGER [NAME_RESULT_TRIGGER]
    BEFORE [INSERT OR UPDATE / DELETE] ON [Table]
    FOR EACH ROW
    EXECUTE FUNCTION [NAME_TRIGGER_FUNCTION]();
```

[8. Query Transaction](source/database/transaction.sql)
```
Transaction trong SQL là một nhóm các thao tác SQL được thực thi như một đơn vị duy nhất, đảm bảo tính toàn vẹn và nhất quán của dữ liệu trong cơ sở dữ liệu. Một transaction có thể bao gồm một hoặc nhiều câu lệnh SQL, và tất cả các câu lệnh này phải thành công hoặc thất bại cùng nhau.
```
```
    Syntax: plpgsql
    DO $$
    DECLARE
        [ATTRIBUTE] [DATA_TYPE] := [VALUE];
    BEGIN
        <process code>

    EXCEPTION WHEN OTHERS THEN
        RAISE NOTICE 'Lỗi khi thực hiện Transaction: %', SQLERRM;
    END;
    $$;
```

[9. Grant - Revoke](source/database/grant_revoke.sql)
```
GRANT : cấp quyền/vai trò cho 1 người dùng thực hiện các thao tác nhất định trên CSDL, bảng, cột, hay view 
Syntax:
    GRANT Permission [, permission2, ...]
    ON Object
    TO User [, user2, ...];
```
```
Revoke : thu hồi quyền mà trước đó đã được cấp bằng GRANT
Syntax:
    REVOKE Permission [, permission2, ...]
    ON Object
    FROM User [, user2, ...];
```

```
User login: (when have account)
    + Registry Server:
        Enter name server : User Server
        Host name : postgres_db
        port : 5432
        name db : management
        username : [username]
        password : [password]
    + Query with granted permission
```

[10. OOP](https://docs.google.com/document/d/1Sbtn1SWsS0AqPhbzMeRWOyVLrqoHfKNF7y4nZxsD9_Y/edit?tab=t.0#heading=h.d97h4lvay1o4)
```
4 Tính chất OOP:
    + Đóng gói
    + Kế thừa
    + Đa hình
    + Trừu tượng
```
[11. DI, IOC](https://viblo.asia/p/springgiai-thich-dependency-injection-di-va-ioc-bang-ngoc-trinh-naQZRW3Alvx)
```
DI: là việc các Object phụ thuộc vào các abstract Class chứ không
phụ thuộc vào cấp thấp
3 kiểu tạo DI:
    + constructor
    + setter
    + Interface()
=> Inject Dependency: giả sử class có hàng chục cái dependency 
và phải tự tay inject từng cái => khó code 
=> Inversion Of Control: gom các dependency này vào cái kho và giao cho 
1 (framework) quản lý. thì framework này tự tìm trong kho để inject vào đối tượng
=> dễ dàng hơn
```

### [III. Frontend](frontend)
#### [1. part1_basic.html](frontend/ui/part1_basic.html)

![alt text](frontend/images/part1.png)

#### [2. part2_tag.html](frontend/ui/part2_tags.html)

![alt text](frontend/images/part2.png)

#### [3. part3_semantic.html](frontend/ui/part3_semantic.html)

![alt text](frontend/images/part3.png)

#### [4. practice : part4_profile.html](frontend/ui/part4_profile.html)

![alt text](frontend/images/part4.png)