-- toán tử truy vấn

-- from - lấy dữ liệu từ bảng nào để truy vấn
select * from sinhvien;
select HoTen, GioiTinh from sinhvien;

-- where - điều kiện lọc dữ liệu
select * from sinhvien where GioiTinh = 'Nam';
select * from sinhvien where NgaySinh > '2000-01-01';
select * from ketqua where Diem >= 8.0;

-- and - kết hợp nhiều điều kiện 
select * from sinhvien where GioiTinh = 'Nam' and NgaySinh > '2000-01-01';
select * from ketqua where Diem >= 7.0 and Diem <= 9.0;

-- or - một trong các điều kiện đúng
select * from sinhvien where GioiTinh = 'Nam' or NgaySinh < '1990-01-01';
select * from ketqua where Diem < 5.0 or Diem >= 9.0;

-- not - phủ định điều kiện
select * from sinhvien where not GioiTinh = 'Nam';
select * from ketqua where not Diem < 5.0;

-- in - kiểm tra giá trị trong danh sách
select * from sinhvien where MaSV in ('SV001', 'SV002', 'SV003');
select * from ketqua where Diem in (8.0, 8.5, 9.0);

-- between - kiểm tra giá trị trong khoảng
select * from ketqua where Diem between 7.0 and 9.0;
select * from sinhvien where NgaySinh between '1995-01-01' and '2000-12-31';

-- like - tìm kiếm
select * from sinhvien where HoTen like 'Nguyen%';  -- bắt đầu bằng "Nguyen"
select * from sinhvien where HoTen like '%Van%';    -- chứa "Van"


-- order by - sort
select * from sinhvien order by HoTen;              -- tăng dần (mặc định)
select * from sinhvien order by NgaySinh desc;      -- giảm dần

-- group by - nhóm dữ liệu
select GioiTinh, count(*) as SoLuong from sinhvien group by GioiTinh;
select MaMonHoc, avg(Diem) as DiemTB from ketqua group by MaMonHoc;

-- having - điều kiện cho group by
select GioiTinh, count(*) as SoLuong 
from sinhvien 
group by GioiTinh 
having count(*) > 5;

select MaMonHoc, avg(Diem) as DiemTB 
from ketqua 
group by MaMonHoc 
having avg(Diem) >= 7.0;

-- limit - giới hạn số dòng kết quả
select * from sinhvien limit 10;                    -- lấy 10 dòng đầu

-- distinct - loại bỏ trùng lặp
select distinct MaMonHoc from ketqua;

-- kết hợp nhiều toán tử
select sv.HoTen, sv.GioiTinh, kq.Diem
from sinhvien sv, ketqua kq
where sv.MaSV = kq.MaSV 
  and sv.GioiTinh = 'Nam' 
  and kq.Diem >= 8.0
order by kq.Diem desc
limit 10;