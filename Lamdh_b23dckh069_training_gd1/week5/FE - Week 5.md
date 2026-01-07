## 1. Refresh Token (JWT)
- Khi đăng nhập thì server sẽ trả về Access Token, Refresh token.
- Access Token:
   * Chứa thông tin user
   * Có thời hạn
- Nếu thời hạn quá dài => Nguy hiểm
- Nếu thời hạn quá ngắn => User bị đăng xuất liên tục

=> Cần Refresh Token để giải quyết vấn đề trên

- Luồng hoạt động của refersh token:
1. User đăng nhập
2. Server trả về Access Token và Refresh Token
3. Client gọi API bằng Access Token
4. Access Token nếu hết hạn thì Client gọi lệnh refresh token để gửi Refresh Token
5. Nếu Refresh Token còn hạn thì cấp Access Token mới
6. Nếu Refresh Token hết hạn thì server trả lỗi và client log out user. Bắt buộc đăng nhập lại để tạo Refresh Token mới

- Refresh Token thường được lưu ở HttpOnly Cookie, Database (server) (dùng để revoke),...

---

## 2. Upload lên S3 (Cloudinary / MinIO)
- S3 (Object Storage) lưu ảnh, video, file, log. Không lưu file trực tiếp trong server

1. Cloudinary
- Là dịch vụ lưu ảnh, video, có CDN, resize, crop ảnh,...
- Luồng upload: Client -> Cloudinary -> Nhận URL -> Lưu URL vào database
- Dễ dùng, có CDN, xử lý ảnh mạnh

2. MinIO
- S3-Compatibl, Tự host (docker), phù hợp cho project nội bộ và học
- Kiến trúc MinIO:
   Client -> Backend -> MinIO -> Trả URL