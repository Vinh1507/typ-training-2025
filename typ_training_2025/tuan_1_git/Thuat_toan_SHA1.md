## Phần 3.2 : Thuật toán SHA-1
* SHA-1 hoạt động giống như một chiếc **"máy xay sinh tố" kỹ thuật số** 🌪️. Bạn đưa bất kỳ dữ liệu nào vào (dù là một chữ cái hay cả một bộ phim), nó sẽ "xay" dữ liệu đó qua một quy trình toán học phức tạp và cho ra một "ly sinh tố" có kích thước cố định, gọi là **hash** (hoặc dấu vân tay kỹ thuật số).
Dấu vân tay này dài 160 bit, thường được biểu diễn dưới dạng 40 ký tự thập lục phân (hexadecimal).
---
* SHA-1 được thiết kế với những đặc tính quan trọng sau:
   - **Một chiều (One-way):** Từ dữ liệu gốc, bạn rất dễ dàng tạo ra hash. Nhưng từ hash, việc tìm lại dữ liệu gốc là gần như không thể. Giống như bạn không thể nào lấy lại hoa quả ban đầu từ một ly sinh tố đã xay nhuyễn.
   - **Kích thước cố định (Fixed-size):** Dù đầu vào của bạn lớn hay nhỏ, dấu vân tay SHA-1 tạo ra luôn có độ dài 160 bit.
   - **Cực kỳ nhạy cảm (Avalanche Effect):** Chỉ cần thay đổi một chi tiết cực nhỏ trong dữ liệu đầu vào (ví dụ, thay đổi một chữ cái), dấu vân tay SHA-1 tạo ra sẽ **hoàn toàn khác biệt**.
   - **Ít đụng độ (Collision Resistant):** Về lý thuyết, rất khó để tìm ra hai khối dữ liệu khác nhau mà lại tạo ra cùng một dấu vân tay SHA-1. *(Xem lưu ý quan trọng bên dưới)*.
---
## Cách hoạt động (phiên bản đơn giản)
Quá trình "xay" dữ liệu của SHA-1 trải qua các bước chính sau:
1.  **Bước 1: Chuẩn bị dữ liệu (Padding):** Đầu tiên, thuật toán thêm các bit vào cuối dữ liệu gốc để đảm bảo tổng độ dài của nó là một bội số của 512 bit.
2.  **Bước 2: Chia thành các khối (Chunking):** Dữ liệu sau khi được chuẩn bị sẽ được chia thành các khối (chunk) bằng nhau, mỗi khối dài 512 bit.
3.  **Bước 3: Xử lý lặp đi lặp lại (Processing Rounds):** ⛓️
    * Thuật toán bắt đầu với 5 biến số nội bộ ban đầu (gọi là H0 đến H4).
    * Nó lấy khối dữ liệu đầu tiên và "xay" nó cùng với 5 biến số đó qua 80 vòng lặp của các phép toán logic và toán học phức tạp.
    * Kết quả của quá trình này sẽ cập nhật giá trị của 5 biến số.
    * Quá trình lặp lại: Lấy kết quả mới, kết hợp với khối dữ liệu tiếp theo và lại "xay" qua 80 vòng. Cứ như vậy cho đến khi xử lý hết tất cả các khối.
4.  **Bước 4: Ra kết quả cuối cùng:** Sau khi xử lý khối dữ liệu cuối cùng, 5 biến số nội bộ được kết hợp lại với nhau để tạo ra dấu vân tay SHA-1 cuối cùng dài 160 bit.
---
## ⚠️ Lưu ý quan trọng: Tại sao SHA-1 không còn an toàn tuyệt đối?
Mặc dù rất phức tạp, SHA-1 hiện đã bị coi là **không an toàn** cho các ứng dụng bảo mật như chứng chỉ SSL. Lý do là vì các nhà nghiên cứu đã tìm ra cách tạo ra **"đụng độ" (collision)** một cách có chủ đích.
> **Đụng độ là gì?** Là hiện tượng hai tập tin hoàn toàn khác nhau (ví dụ, một hợp đồng hợp lệ và một hợp đồng giả mạo) lại tạo ra **cùng một dấu vân tay SHA-1**.
Tuy nhiên, đối với Git, mục đích chính của SHA-1 là để **đảm bảo tính toàn vẹn dữ liệu** (chống lại sự hư hỏng do vô tình) chứ không phải để chống lại các cuộc tấn công tinh vi. Vì vậy, nó vẫn được coi là "đủ an toàn" cho Git ở thời điểm hiện tại, nhưng cộng đồng Git cũng đang trong quá trình chuyển dịch sang thuật toán mạnh hơn là **SHA-256**.

## Tài liệu tham khảo
* [SHA-1 trên Wikipedia](https://en.wikipedia.org/wiki/SHA-1)
* [How SHA-1 Works](https://www.movable-type.co.uk/scripts/sha1.html)
* [Git và SHA-1](https://git-scm.com/book/en/v2/Git-Internals-Git-Objects#_sha_1)
