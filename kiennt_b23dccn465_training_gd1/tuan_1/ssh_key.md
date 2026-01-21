## Phần 1+ : SSH Key
* SSH key (Secure Shell key) trong Git là một cơ chế xác thực (authentication) cho phép bạn kết nối và tương tác với các kho lưu trữ (repository) Git từ xa (như GitHub, GitLab, Bitbucket) một cách an toàn mà không cần nhập tên người dùng và mật khẩu mỗi lần.
* Nó hoạt động dựa trên một cặp khóa mã hóa: một khóa riêng tư (private key) bạn giữ bí mật trên máy của mình và một khóa công khai (public key) bạn tải lên máy chủ Git.
* **Cơ chế hoạt động :**
  * Cơ chế này được gọi là "Mã hóa khóa công khai" (Public Key Cryptography). Hãy tưởng tượng nó như sau:
    1. Khóa Công khai (Public Key): Giống như một ổ khóa 🔒. 
       * Bạn có thể tạo ra bao nhiêu ổ khóa (khóa công khai) tùy thích từ chìa khóa của mình. 
       * Bạn có thể đưa ổ khóa này cho bất kỳ ai (ví dụ: bạn tải nó lên GitHub, GitLab). Họ có thể dùng nó để khóa một thông điệp hoặc xác minh bạn. 
    2. Khóa Riêng tư (Private Key): Giống như một chìa khóa 🔑. 
       * Đây là thứ duy nhất có thể mở được ổ khóa 🔒 tương ứng. 
       * Bạn phải giữ nó tuyệt đối bí mật. Không bao giờ chia sẻ khóa riêng tư của bạn.
* Ví dụ : Quy trình xác thực khi bạn git push: 
  * Máy của bạn nói: "Này GitHub, tôi muốn push code lên."
  * GitHub nói: "OK, cậu là ai? Hãy chứng minh đi. Tôi có cái 'ổ khóa' (public key) mà cậu đưa tôi đây. Tôi sẽ gửi cho cậu một thông điệp ngẫu nhiên, hãy 'ký' vào nó."
  * Máy của bạn: Dùng 'chìa khóa' (private key) của mình để "ký" (mã hóa) thông điệp đó và gửi lại cho GitHub. 
  * GitHub: Dùng 'ổ khóa' (public key) của bạn để kiểm tra chữ ký. 
  * Nếu khớp: "OK, đúng là cậu rồi. Cho phép push." ✅ 
  * Nếu không khớp: "Cậu không có chìa khóa. Kết nối bị từ chối." ❌ 
  * Điều quan trọng nhất là khóa riêng tư (chìa khóa) không bao giờ rời khỏi máy tính của bạn. Kẻ tấn công có nghe lén được mạng cũng không thể lấy được khóa của bạn.
## Phần 1+ : Personal Access Token
* Một Personal Access Token (PAT), hay "Token Truy cập Cá nhân", là một chuỗi ký tự dài, ngẫu nhiên, được sử dụng thay thế cho mật khẩu của bạn khi xác thực với các dịch vụ như GitHub, GitLab, hoặc các API khác.
* Hãy coi nó như một chiếc thẻ khóa 🔑 của khách sạn:
  * Mật khẩu của bạn giống như chìa khóa chủ của người quản lý. Nó có thể mở mọi phòng, mọi lúc (toàn quyền). Nếu bạn làm mất nó, rủi ro là cực kỳ lớn.
  * PAT giống như thẻ khóa phòng của khách. Nó chỉ được cấp cho bạn, chỉ có thể mở phòng của bạn (phạm vi giới hạn), và chỉ hoạt động trong 3 ngày (thời gian hết hạn).
* Bạn sử dụng PAT để cấp quyền cho một ứng dụng, một kịch bản (script), hoặc một công cụ (như `git` trên máy tính của bạn) để truy cập tài khoản của bạn mà không cần đưa cho nó mật khẩu thật.
* Các tính năng chính (Tại sao PAT tốt hơn Mật khẩu)
  * Phạm vi (Scope) giới hạn: Đây là lợi ích lớn nhất. Khi bạn tạo một PAT, bạn chọn chính xác những gì nó có thể làm.
    * Ví dụ: Bạn có thể tạo một token chỉ có quyền đọc (read) các kho lưu trữ (repository) của bạn, nhưng không có quyền xóa (delete) chúng. Mật khẩu của bạn có thể làm tất cả. 
  * Có thể thu hồi (Revocable): Nếu bạn vô tình làm lộ một PAT, bạn chỉ cần vào cài đặt tài khoản và xóa (revoke) token đó. Tài khoản và mật khẩu chính của bạn vẫn an toàn. Bạn không cần phải thay đổi mật khẩu và đăng nhập lại ở mọi nơi. 
  * Có thể hết hạn (Expirable): Bạn có thể đặt ngày hết hạn cho PAT (ví dụ: 7 ngày, 30 ngày). Sau ngày đó, token tự động ngừng hoạt động, giúp tăng cường bảo mật. 
  * Dễ nhận diện (Identifiable): Bạn có thể tạo nhiều PAT cho nhiều mục đích khác nhau và đặt tên cho chúng (ví dụ: "Token cho máy tính ở nhà", "Token cho script CI/CD"). Nếu một token bị lạm dụng, bạn biết chính xác ứng dụng nào đã bị xâm phạm.
## Phần 1+ : So sánh SSH Key và Personal Access Token
| Tính năng | SSH Key (Khóa SSH) | Personal Access Token (PAT) |
| :--- | :--- | :--- |
| **Giao thức** | `SSH` (ví dụ: `git@github.com:...`) | `HTTPS` (ví dụ: `https://github.com/...`) |
| **Cơ chế** | Mã hóa bất đối xứng (Cặp khóa Công khai/Riêng tư). | Một chuỗi bí mật (token) duy nhất. |
| **Cách xác thực** | Máy chủ xác minh "chữ ký" được tạo bởi **khóa riêng tư** (private key) của bạn. Khóa riêng tư không bao giờ rời khỏi máy bạn. | Bạn gửi **token** (thay cho mật khẩu) lên máy chủ qua HTTPS. Token được truyền qua mạng (đã mã hóa). |
| **Thiết lập** | Tạo cặp khóa trên máy tính của bạn (`ssh-keygen`), sau đó tải **khóa công khai** (public key) lên máy chủ (GitHub, GitLab, v.v.). | Tạo token trên giao diện web của dịch vụ (GitHub, GitLab, v.v.), sau đó sao chép và lưu trữ token trên máy của bạn. |
| **Bảo mật** | Rất an toàn. Khóa riêng tư không bao giờ bị lộ qua mạng. Có thể được bảo vệ thêm bằng passphrase. | An toàn, nhưng kém hơn SSH. Token được truyền qua mạng. Nếu bị lộ, kẻ tấn công có thể sử dụng nó ngay lập tức. |
| **Quản lý** | Quản lý các tệp khóa (`id_rsa`, `id_ed25519`) trên máy tính của bạn (thường trong `~/.ssh/`). | Quản lý token trên giao diện cài đặt của dịch vụ. Dễ dàng thu hồi (revoke) từng token. |
| **Phạm vi (Scopes)** | Thường cấp quyền truy cập **Đọc/Viết** (Read/Write) vào các kho lưu trữ bạn có quyền. Việc phân quyền chi tiết hơn (chỉ đọc) ít phổ biến. | **Rất chi tiết.** Bạn có thể chọn chính xác quyền (ví dụ: chỉ đọc repo, quản lý workflow, xóa package) cho từng token. |
| **Hết hạn** | Thường là **vĩnh viễn** (không tự động hết hạn) trừ khi bạn chủ động xóa khóa công khai khỏi máy chủ. | Có thể đặt **ngày hết hạn** (ví dụ: 7 ngày, 30 ngày, hoặc tùy chỉnh). Tăng cường bảo mật bằng cách tự động vô hiệu hóa. |
| **Trường hợp dùng chính** | Lập trình viên sử dụng trên **máy tính cá nhân** (local development), máy chủ build, để `git push/pull` từ terminal. | **Scripts**, **CI/CD pipelines** (như GitHub Actions), **ứng dụng bên thứ ba**, và các **lệnh gọi API** tự động. |