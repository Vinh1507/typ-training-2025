# Hiểu các khái niệm
## 1. Entity-Relationship(E-R) Diagram
- ER-Diagram là sơ đồ mô tả các thực thể trong hệ thống và mối quan hệ giữa chúng.
**Tại sao lại cần đến ER Diagram ?**
- Nó có thể thể hiện cấu trúc logic tổng quan của 1 cơ sở dữ liệu.
- Nó có thể thể hiện bằng hình ảnh cho dễ hình dung.
- Nó đơn giản và rõ ràng.
- Nó được sử dụng rộng rãi.

## 2.Entity
> **Định nghĩa**: một đối tượng được xác định - như là con người, đồ vật, sự kiện,..có thể lưu trữ dữ liệu về bản thân. Ví dụ đơn giản hơn là: một khách hàng, học sinh, chiếc xe, sản phẩm,...  
- **Entity type:** Một nhóm các đối tượng có thể xác định được, chẳng hạn như sinh viên hoặc vận động viên, trong khi thực thể (entity) sẽ là một cá thể cụ thể — ví dụ một sinh viên cụ thể hoặc một vận động viên cụ thể.
- **Entity set**: Giống như entity type, nhưng được xác định tại một thời gian cụ thể, ví dụ các bạn học sinh tham gia lớp học vào buổi học đầu tiên, những khách hàng mua hàng tháng trước,...
- **Entity categories:** Entity được chia làm ba loại: Strong, Weak và Associative. Một "strong entity" có thể tự xác định bằng các thuộc tính riêng của nó, trong khi đó "weak entity" không thể tự phụ thuộc mà phải phụ thuộc vào các "strong entity" khác. Còn "associative entity" là thực thể dùng để liên kết cấc thực thể khác với nhau
<img width="379" height="199" alt="Screenshot 2025-11-02 003507" src="https://github.com/user-attachments/assets/820695ad-f046-4fde-906f-9259d898eccd" />
<img width="378" height="204" alt="Screenshot 2025-11-02 010950" src="https://github.com/user-attachments/assets/8ab8cb06-3dc5-4087-ab79-81451b317a94" />

- **Entity keys:** là thuộc tính giúp xác định duy nhất một đối tượng trong "entity set". Có bốn loại entity key: super key, candidate key,  primary key và foreign key. Super key: là tập hợp các thuộc tính cùng nhau xác định một entity trong entity set, ví dụ coi student_id là một key thì bây giờ có thêm những key khác để xác định học sinh ấy như email, số điện thoại,... Candidate key: là "super key" “tinh gọn”, chỉ giữ những thuộc tính cần thiết nhất để xác định entity. Primary key: là một "candidate key" được chọn bởi người thiết kế dữ liệu để chọn làm đặc trưng của "entity set". Foreign key dùng để xác nhận mối quan hệ giữa các thực thể.

## 3. Attribute
> **Định nghĩa**: là một đặc tính, tính chất của một "entity". Attribute thì được phân ra thành simple attribute, composite attribute, derived attribute, single-valued attribute và multi-valued attribute.
- **Single attribute**: giá trị thuộc tính không thể chia nhỏ thêm, ví dụ như số điện thoại.
- **Composite attribute**: thuộc tính tổng hợp, bao gồm các thuộc tính con xuất phát từ attribute chính, ví dụ họ tên thì có họ + tên.
- **Derived attribute**: thuộc tính dẫn xuất được suy ra từ một thuộc tính khác, ví dụ tuổi được suy ra từ ngày sinh.
- **Single-value attribute**: chỉ chứa một thuộc tính duy nhất.
- **Multi-valued attribute**: có thể chứa nhiều giá trị cho entity, ví dụ một người có thể có nhiều số điện thoại.
<img width="876" height="694" alt="Screenshot 2025-11-02 015931" src="https://github.com/user-attachments/assets/267e74d1-4774-44a7-a0c7-d4ddd0f0498d" />


## 4. Relationship
> **Định nghĩa**: là chỉ mối quan hệ giữa các entity với nhau. Ví dụ như có hai entity là sếp và nhân viên, relationship mô tả hành động quản lý của sếp đối với nhân viên.

<img width="785" height="347" alt="Screenshot 2025-11-02 015757" src="https://github.com/user-attachments/assets/c1fc2118-1b87-415b-b7fb-b2831803261d" />

- Một "strong relationship" được hình nếu như các entity tham gia relationship không phụ thuộc vào sự tồn tại của nhau, ví dụ 2 entity là giảng viên và học sinh, associative entity là "cho lời khuyên" thì là một strong relationship.
- Còn"weak relationship" xảy ra khi một entity không thể tồn tại nếu không có entity khác. Ví dụ nhân viên còn con của nhân viên, con của nhân viên không thể tồn tại nếu không có nhân viên.
- Recursive relationship: Các entity tham tham gia nhiều hơn một lần trong các mối quan hệ.

## 5. Cardinality
> **Định nghĩa**: dùng để xác định số đặc tính của mối quan hệ giữa hai entity hoặc "entity sets". Có ba loại cardinality chính: 1-1(One-to-One), 1-N(One-to-Many), Many-to-Many(M-N).

- One-to-One(1-1): Mỗi entity ở tập hợp A chỉ liên kết với một entity duy nhất ở tập hợp B, và ngược lại. Ví dụ: Một sinh viên (Student) liên kết với một địa chỉ gửi thư (Mailing Address).
<img width="787" height="129" alt="Screenshot 2025-11-02 023637" src="https://github.com/user-attachments/assets/bfd8a3a6-831b-47d0-aa9f-f82bba11444a" />

- One-to-Many(1-N): Một entity ở tập hợp A có thể liên kết với nhiều entity ở tập hợp B. Ví dụ One-to-Many: Một sinh viên  đăng ký nhiều khóa học, nhưng mỗi khóa học này chỉ liên kết trở lại với một sinh viên trong ví dụ này (theo chiều quan hệ).
<img width="797" height="161" alt="Screenshot 2025-11-02 023750" src="https://github.com/user-attachments/assets/cf0e8017-e7be-4ebb-9827-dc9602a3fa48" />

- Many-to-Many(M-N): Nhiều entity ở tập hợp A có thể liên kết với nhiều entity ở tập hợp B. Ví dụ: Một nhóm sinh viên liên kết với nhiều giảng viên, và các giảng viên này cũng liên kết với nhiều sinh viên.
<img width="777" height="171" alt="Screenshot 2025-11-02 024042" src="https://github.com/user-attachments/assets/1dd6afd3-6a91-41ff-89d7-e3fb4350b728" />





