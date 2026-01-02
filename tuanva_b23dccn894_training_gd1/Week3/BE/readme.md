# Báo cáo: Authentication, Authorization, Session, Token và JWT

Khi nói về bảo mật trong ứng dụng web, có hai khái niệm cơ bản luôn song hành: Authentication (xác thực) và Authorization (phân quyền). Dù chúng thường bị nhầm lẫn, nhưng thực chất chúng là hai bước tách biệt trong quá trình kiểm soát truy cập.

## Authentication và Authorization

Authentication là quá trình xác định danh tính của người dùng. Khi bạn nhập tên đăng nhập và mật khẩu, hệ thống sẽ kiểm tra xem thông tin đó có đúng hay không. Nếu đúng, nghĩa là bạn đã được “xác thực” – hệ thống biết bạn là ai. Ngoài mật khẩu, còn có thể dùng các phương pháp khác như mã OTP, vân tay, khuôn mặt, hoặc khóa bảo mật.

Authorization diễn ra ngay sau khi xác thực. Đây là bước hệ thống kiểm tra xem người dùng đã xác thực đó được phép làm gì – ví dụ: xem dữ liệu, chỉnh sửa, hay xóa tài nguyên. Nếu xác thực trả lời “bạn là ai”, thì phân quyền trả lời “bạn được làm gì”.

Trong thực tế, hai khái niệm này luôn đi cùng nhau. Một người dùng có thể được xác thực nhưng không có quyền thực hiện hành động nhất định. Điều đó giúp hệ thống an toàn hơn và linh hoạt trong quản lý quyền truy cập.

## Cơ chế xác thực: Session-based và Token-based

### Session-based Authentication

Đây là phương pháp truyền thống, phổ biến trong các ứng dụng web đời đầu. Khi người dùng đăng nhập, máy chủ tạo một phiên làm việc (session) và lưu thông tin người dùng trong bộ nhớ hoặc cơ sở dữ liệu. Trình duyệt nhận một session ID, thường lưu trong cookie. Mỗi khi gửi request, cookie này tự động được gửi kèm, giúp máy chủ nhận diện người dùng.

Ưu điểm của cách này là dễ hiểu và dễ triển khai. Nó cũng cho phép máy chủ thu hồi quyền truy cập ngay lập tức bằng cách xóa session. Tuy nhiên, hạn chế lớn là tính mở rộng: nếu hệ thống có nhiều server, session phải được chia sẻ hoặc đồng bộ giữa chúng, gây tốn tài nguyên và phức tạp.

Cách này rất phù hợp cho các trang web truyền thống, chỉ chạy trên một server hoặc ít máy chủ.

### Token-based Authentication

Với sự phát triển của ứng dụng di động và API, token-based trở thành xu hướng mới. Thay vì lưu session ở server, sau khi người dùng đăng nhập thành công, máy chủ tạo ra một token (thường là JWT) và gửi lại cho client. Token này được client lưu lại, thường là trong localStorage hoặc sessionStorage, và gửi kèm trong mỗi request.

Điểm khác biệt lớn là server không cần lưu trạng thái người dùng, vì tất cả thông tin đã nằm trong token. Việc xác thực diễn ra thông qua việc kiểm tra tính hợp lệ của token – nó có bị thay đổi, hết hạn hay không.

Ưu điểm là hệ thống trở nên “stateless”, giúp mở rộng dễ dàng hơn trong mô hình microservices. Tuy nhiên, nhược điểm là việc thu hồi token khó hơn. Nếu token bị đánh cắp, kẻ tấn công có thể sử dụng nó cho đến khi token hết hạn.

## JWT – JSON Web Token

JWT là một định dạng phổ biến trong cơ chế xác thực bằng token. Đây là chuẩn mở (RFC 7519) dùng để truyền thông tin giữa các bên một cách an toàn, dưới dạng JSON.

Một JWT gồm ba phần:

1. Header – chứa thông tin về loại token và thuật toán mã hóa.
2. Payload – chứa dữ liệu (claims), ví dụ như user_id, vai trò, thời gian hết hạn.
3. Signature – chữ ký số dùng để xác minh token chưa bị thay đổi.

Ba phần này được mã hóa bằng Base64URL và nối lại với nhau bằng dấu chấm. Khi máy chủ nhận được JWT, nó sẽ dùng khóa bí mật để xác thực chữ ký. Nếu chữ ký hợp lệ, token được coi là đáng tin cậy.

Ví dụ cấu trúc một JWT:

header.payload.signature

### Lợi ích của JWT

- Không cần lưu session trên server, giúp hệ thống dễ mở rộng.
- Dễ dàng sử dụng trong nhiều dịch vụ khác nhau (API, microservices, mobile app).
- Nhanh và gọn nhẹ, dễ truyền qua HTTP header.
- Có thể xác minh tính toàn vẹn của dữ liệu nhờ chữ ký số.
- Linh hoạt, có thể chứa nhiều thông tin tuỳ theo mục đích sử dụng.

Dù vậy, JWT không mã hóa nội dung payload. Ai cũng có thể giải mã phần dữ liệu nếu có token, nên không nên lưu thông tin nhạy cảm trong đó. Để đảm bảo an toàn, cần kết hợp HTTPS hoặc mã hóa bổ sung nếu cần.

## [Video thực hành](https://drive.google.com/drive/folders/1XpR5k3sdLrMTeUGAa_NE_VLvmtyRq9Q7?hl=vi)
