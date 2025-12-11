## **Dưới đây là E-R của hệ thống Quản lý Cửa Hàng Bán Hoa Offline**

<img width="4512" height="1902" alt="erdplus (1)" src="https://github.com/user-attachments/assets/c09f323c-0a2d-4929-be21-39f566aa472a" />


### **Giải thích**
- Ta có tổng cộng 4 strong entity là : NHANVIEN, DonVi, Hoa, HoaDon và 1 weak entity là Con
- NHANVIEN thì có MaNV làm key attribute, GioiTinh, NgaySinh, DiaChi, Luong là những single attribute còn HoTen là conposite attribute khi có 2 sub-attribute là HoDem và Ten.
- DonVi thì có MaDV làm key attribute, TenDV và DiaDiem làm single attribute.
- Hoa thì có Hoa_id làm key attribute, TenHoa, GiaTien, Stock-Quantituy làm single attribute.
- HoaDon thì có MaHoaDon làm key attribute, GiaTien làm single attribute.
- Con thì có HoTen, GioiTinh, NgaySinh làm single attribute.
- Mỗi nhân viên phải làm việc cho 1 đơn vị, mỗi đơn vị phải có ít nhất 4 nhân viên và nhiều nhất N nhân viên, mỗi một nhân viên có thể tham gia quản lý hoặc không, mỗi đơn vị phải có 1 và chỉ 1 nhân viên quản lý.
- Liên kết quản lý giữa nhân viên và đơn vị có một thuộc tính đơn là ngày bắt đầu.
- Một hoặc nhiều nhân viên có thể tham gia bán hoa, còn một loại hoa có thể không được bán hoặc được bán bởi một nhân viên.
- Một hóa đơn bắt buộc phải chứa 1 hoặc nhiều hoa, một loại hoa bắt buộc phải chứa trong 1 hoặc nhiều hóa đơn.
- Một nhân viên bắt buộc phải xuất được 1 hoặc nhiều đơn, còn 1 hóa đơn chỉ được xuất bởi 1 nhân viên.
- Một nhân viên có thể không có hoặc có nhiều con, còn con thì bắt buộc phải là của 1 hoặc cả 2 nhân viên(cả bố và mẹ).

