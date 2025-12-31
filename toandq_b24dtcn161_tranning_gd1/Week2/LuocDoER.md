# 1. Khái niệm

- **Entity - Thực thể**

    - **Đ/n**: Là một đối tượng hoặc một khái niệm có thật trong thế giới thực mà chúng ta muốn lưu trữ thông tin về nó. Nó là "danh từ" trong mô tả của bạn.

    - **VD:** `SINH VIÊN`, `MÔN HỌC`.

    - **Kí hiệu**: được biểu diễn bằng một **hình chữ nhật**.

- **Attribute - Thuộc tính**

    - **Đ/n**: Là các đặc điểm, tính chất mô tả cho một thực thể. Nếu Entity là "danh từ", thì Attribute là "tính từ" hoặc "dữ liệu" đi kèm.

    - **VD:** 
        - Thực thể `SINH VIÊN` có các thuộc tính: `Mã SV`, `Họ Tên`, `Ngày Sinh`, `Địa Chỉ`.

        - Thực thể `MÔN HỌC` có các thuộc tính: Mã Môn Học, `Tên Môn Học`, `Số Tín Chỉ`.

    - **Kí hiệu**: được biểu diễn bằng một **hình oval** (hình elip) nối với thực thể.

- **Relationship - Mối quan hệ**

    - **Đ/n**: Là sự liên kết, tương tác hoặc mối liên hệ giữa hai hoặc nhiều thực thể. Nó là "động từ" kết nối các "danh từ" (thực thể).

    - **VD:** `SINH VIÊN`... `MÔN HỌC`. Mối quan hệ giữa chúng là `Đăng ký`.

    - **Kí hiệu**: được biểu diễn bằng một hình thoi, nối giữa các thực thể mà nó liên kết.

- **Cardinality - Bản số / Lực lượng**

    - Cardinality mô tả số lượng các thể hiện (instance) của một thực thể có thể tham gia vào một mối quan hệ.

    - Nó trả lời hai câu hỏi:

        - Một thực thể A có thể liên quan đến tối thiểu và tối đa bao nhiêu thực thể B?

        - Một thực thể B có thể liên quan đến tối thiểu và tối đa bao nhiêu thực thể A?

    - Có 3 loại Cardinality chính: (1:1), (1:N), (N:M).

# 2. Relationship

- **a**. Một - Một (1:1)

    - **Ý nghĩa**: Một thể hiện của A chỉ liên quan đến tối đa một thể hiện của B, và ngược lại.
    - **Ví dụ**: `QUẢN LÝ KHOA` và `KHOA`.
        Một `KHOA` chỉ có một QUẢN LÝ `KHOA`.
        Một QUẢN LÝ `KHOA` cũng chỉ quản lý một `KHOA`.

- **b**. Một - Nhiều (1:N)
    - **Ý nghĩa**: Một thể hiện của A có thể liên quan đến nhiều thể hiện của B, nhưng một thểhiện của  B chỉ liên quan đến một thể hiện của A.
    - **Ví dụ**: `LỚP HỌC` và `SINH VIÊN`.
        Một `LỚP HỌC` có nhiều `SINH VIÊN`.
        Một `SINH VIÊN` chỉ thuộc về một `LỚP HỌC`.
- **c**. Nhiều - Nhiều (N:M)
    - **Ý nghĩa**: Một thể hiện của A có thể liên quan đến nhiều thể hiện của B, và một thểhiệncủa B    cũng       có thể liên quan đến nhiều thể hiện của A.
    - **Ví dụ** (quay lại ví dụ chính): `SINH VIÊN` và `MÔN HỌC` với mối quan hệ "Đăng ký".
        Một `SINH VIÊN` (Nguyễn Văn A) có thể đăng ký nhiều `MÔN HỌC` (Toán, Lý, Hóa).
        Một `MÔN HỌC` (ví dụ: Môn Toán) có nhiều `SINH VIÊN` đăng ký.
    - **Note**: Khi triển khai CSDL, mối quan hệ N:M thường được "bẻ gãy" thành một bảng trunggian  (còn         gọi là thực thể liên kết) chứa khóa chính của cả hai bảng. Ví dụ: Bảng `BẢNGĐIỂM`   hoặc `PHIẾU ĐĂNG KÝ.