CREATE TABLE Khoa (
	ma_khoa VARCHAR(10) PRIMARY KEY,
	ten_khoa VARCHAR(100),
	nam_thanh_lap INT
);

CREATE TABLE KhoaHoc(
	ma_khoa_hoc VARCHAR(10) PRIMARY KEY,
	nam_bat_dau INT,
	nam_ket_thuc INT
);

CREATE TABLE ChuongTrinhHoc (
	ma_chuong_trinh VARCHAR(10) PRIMARY KEY,
	ten_chuong_trinh VARCHAR(100)
);

CREATE TABLE Lop (
	ma_lop VARCHAR(10) PRIMARY KEY,
	ma_khoa VARCHAR(10) NOT NULL,
	ma_chuong_trinh VARCHAR(10) NOT NULL,
	ma_khoa_hoc VARCHAR(10) NOT NULL,
	stt INT,

	FOREIGN KEY(ma_khoa) REFERENCES Khoa(ma_khoa),
	FOREIGN KEY(ma_chuong_trinh) REFERENCES ChuongTrinhHoc(ma_chuong_trinh),
	FOREIGN KEY(ma_khoa_hoc) REFERENCES KhoaHoc(ma_khoa_hoc)
);

CREATE TABLE SinhVien (
	ma_sv VARCHAR(10) PRIMARY KEY,
	ho_ten VARCHAR(100),
	nam_sinh INT,
	dan_toc VARCHAR(10),
	ma_lop VARCHAR(10) NOT NULL,

	FOREIGN KEY (ma_lop) REFERENCES Lop(ma_lop)
);

CREATE TABLE MonHoc
(
	ma_mon_hoc VARCHAR(10) PRIMARY KEY,
	ma_khoa VARCHAR(10) NOT NULL,
	ten_mon_hoc VARCHAR(100),

	FOREIGN KEY (ma_khoa) REFERENCES Khoa(ma_khoa)
);

CREATE TABLE KetQua
(
	ma_sv VARCHAR(10) NOT NULL,
	ma_mon_hoc VARCHAR(10) NOT NULL,
	lan_thi INT,
	diem FLOAT,

	PRIMARY KEY (ma_sv, ma_mon_hoc, lan_thi),

	FOREIGN KEY (ma_sv) REFERENCES SinhVien(ma_sv),
	FOREIGN KEY (ma_mon_hoc) REFERENCES MonHoc(ma_mon_hoc)
);

CREATE TABLE GiangKhoa
(
	ma_chuong_trinh VARCHAR(10) NOT NULL,
	ma_khoa VARCHAR(10) NOT NULL,
	ma_mon_hoc VARCHAR(10) NOT NULL,
	nam_hoc INT NOT NULL,
	hoc_ky INT,
	so_tiet_ly_thuyet INT,
	so_tiet_thuc_hanh INT,
	so_tin_chi INT,

	PRIMARY KEY (ma_chuong_trinh, ma_khoa, ma_mon_hoc, nam_hoc),

	FOREIGN KEY (ma_chuong_trinh) REFERENCES ChuongTrinhHoc(ma_chuong_trinh),
	FOREIGN KEY (ma_khoa) REFERENCES Khoa(ma_khoa),
	FOREIGN KEY (ma_mon_hoc) REFERENCES MonHoc(ma_mon_hoc)
);
