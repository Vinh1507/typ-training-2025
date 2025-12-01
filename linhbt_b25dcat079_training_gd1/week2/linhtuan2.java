//Khái niệm,Định nghĩa
Entity (Thực thể),"Một đối tượng, sự vật, hoặc khái niệm có ý nghĩa trong hệ thống (ví dụ: Khách hàng, Hoa, Đơn hàng)."
Attribute (Thuộc tính),"Đặc điểm mô tả cho một Thực thể (ví dụ: Thực thể Khách hàng có các thuộc tính: Mã KH, Tên KH, SĐT)."
Relationship (Mối quan hệ),Sự liên kết giữa hai hoặc nhiều Thực thể (ví dụ: Khách hàng Đặt Đơn hàng).
Cardinality (Bản số),"Số lượng các thực thể tối thiểu và tối đa có thể tham gia vào một mối quan hệ (ví dụ: 1-N, N-N)."
Loại Quan hệ,Ký hiệu,Ý nghĩa,Ví dụ trong Cửa hàng Hoa
One-to-Many,1 - N,"Một thực thể bên A liên kết với nhiều thực thể bên B, và ngược lại, mỗi thực thể bên B chỉ liên kết với một thực thể bên A.",Một Khách hàng có thể Đặt Nhiều đơn hàng.
One-to-One,1 - 1,"Một thực thể bên A chỉ liên kết với tối đa một thực thể bên B, và ngược lại.",Một Nhân viên chỉ Quản lý Một quầy hàng (trong mô hình nhỏ).
Many-to-Many,N - N,"Một thực thể bên A có thể liên kết với nhiều thực thể bên B, và một thực thể bên B cũng có thể liên kết với nhiều thực thể bên A.",Một Đơn hàng có thể chứa Nhiều loại Hoa khác nhau. Một loại Hoa có thể nằm trong Nhiều Đơn hàng.
Loại Quan hệ,Ký hiệu,Ý nghĩa,Ví dụ trong Cửa hàng Hoa
One-to-Many,1 - N,"Một thực thể bên A liên kết với nhiều thực thể bên B, và ngược lại, mỗi thực thể bên B chỉ liên kết với một thực thể bên A.",Một Khách hàng có thể Đặt Nhiều đơn hàng.
One-to-One,1 - 1,"Một thực thể bên A chỉ liên kết với tối đa một thực thể bên B, và ngược lại.",Một Nhân viên chỉ Quản lý Một quầy hàng (trong mô hình nhỏ).
Many-to-Many,N - N,"Một thực thể bên A có thể liên kết với nhiều thực thể bên B, và một thực thể bên B cũng có thể liên kết với nhiều thực thể bên A.",Một Đơn hàng có thể chứa Nhiều loại Hoa khác nhau. Một loại Hoa có thể nằm trong Nhiều Đơn hàng.
Thực thể,Thuộc tính (ví dụ),Khóa chính (Primary Key)
Khách hàng,"Mã_KH, Tên_KH, SĐT, Địa_chỉ",Mã_KH
Đơn hàng,"Mã_ĐH, Ngày_lập, Tổng_tiền, Trạng_thái",Mã_ĐH
Sản phẩm/Hoa,"Mã_Hoa, Tên_Hoa, Giá_bán, Mô_tả",Mã_Hoa
Nhân viên,"Mã_NV, Tên_NV, SĐT, Chức_vụ",Mã_NV
Chi tiết ĐH (Strong Entity for N-N),"Số_lượng, Đơn_giá","Mã_ĐH, Mã_Hoa (Khóa tổng hợp)"
## 2. Relationships

Ví dụ: Quan hệ người mua - Giao dịch - Trái cây

- Quan hệ 1 - N (1 người có thể mua được nhiều trái cây)
- Quan hệ 1 - 1 (1 người mua thì sẽ có 1 giao dịch)
- Quan hệ N - N (Nhiều người mua thì sẽ có nhiều trái cây để bán)

## 3. Sơ đồ E - R

+------------+           +-----------+           +------------+
| NGUOIMUA   |           | GIAODICH  |           | TRAICAY    |
|------------|           |-----------|           |------------|
| MaNM (PK)  |<--------->| MaNM (FK) |<--------->| MaTC (PK)  |
| HoTen      |           | MaTC (FK) |           | TenTC      |
| SDT        |           | SoLuong   |           | GiaBan     |
+------------+           | NgayMua   |           +------------+
                         +-----------+
