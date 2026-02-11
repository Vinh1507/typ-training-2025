-- 1. In danh sách các sinh viên của 1 lớp học
CREATE OR REPLACE PROCEDURE LIST_STUDENTS_FROM_CLASS(
    p_ma_lop VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
DECLARE
	r RECORD;
BEGIN
    FOR r IN
	    SELECT sv.* FROM SinhVien sv
		JOIN Lop l ON l.ma_lop = sv.ma_lop
	    WHERE l.ma_lop = p_ma_lop
	LOOP
		RAISE NOTICE 'ma_sv: %, ho_ten: %, nam_sinh: %, dan_toc: %, ma_lop: %', r.ma_sv, r.ho_ten, r.nam_sinh, r.dan_toc, r.ma_lop;
	END LOOP;
END;
$$;

CALL LIST_STUDENTS_FROM_CLASS('TH2002/01');

-- 2. Nhập vào 1 môn học và 1 mã sv, kiểm tra xem sinh viên có đậu môn này trong lần đầu tiên thi không, 
-- nếu đậu xuất ra 'Đậu' không thì xuất ra 'Trượt'
CREATE OR REPLACE PROCEDURE KIEM_TRA_SINH_VIEN_CO_DAU_MON_TRONG_LAN_THI_DAU(
	p_ma_mon_hoc VARCHAR(10),
	p_ma_sv VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
DECLARE
    diem FLOAT;
BEGIN
	SELECT kq.diem INTO diem FROM KetQua kq
	WHERE kq.ma_sv = p_ma_sv
	AND kq.ma_mon_hoc = p_ma_mon_hoc
	AND kq.lan_thi = 1;

	IF diem IS NULL THEN
		RAISE NOTICE 'Sinh viên % chưa thi môn %', p_ma_sv, p_ma_mon_hoc;
	ELSEIF diem >= 5 THEN
		RAISE NOTICE 'Đậu';
	ELSE
		RAISE NOTICE 'Trượt';
	END IF;
END;
$$;

CALL KIEM_TRA_SINH_VIEN_CO_DAU_MON_TRONG_LAN_THI_DAU('THT01', '0212003');

-- 3. Nhập vào 1 khoa, in danh sách các sinh viên(mã sinh viên, họ tên, ngày sinh) thuộc khoa này
CREATE OR REPLACE PROCEDURE DANH_SACH_SV_CUA_KHOA(
	p_ma_khoa varchar(10)
)
LANGUAGE plpgsql
AS $$
DECLARE 
	r RECORD;
BEGIN
	FOR r IN
		SELECT sv.ma_sv, sv.ho_ten, sv.nam_sinh FROM Khoa k
		JOIN Lop l ON l.ma_khoa = k.ma_khoa
		JOIN SinhVien sv ON sv.ma_lop = l.ma_lop
		WHERE k.ma_khoa = p_ma_khoa
	LOOP
		RAISE NOTICE 'ma_sv: %, ho_ten: %, nam_sinh: %', r.ma_sv, r.ho_ten, r.nam_sinh;
	END LOOP;
END;
$$;

CALL DANH_SACH_SV_CUA_KHOA('CNTT');

/*
	Thêm 1 quan hệ
	XepLoai:
	masv	diemtb	ketqua	hocluc
*/
CREATE TABLE XepLoai (
	ma_sv VARCHAR(10) NOT NULL,
	diem_tb FLOAT,
	ket_qua VARCHAR(10),
	hoc_luc VARCHAR(10),

	PRIMARY KEY (ma_sv),

	FOREIGN KEY (ma_sv) references SinhVien(ma_sv)
);

/*
	4. Quy định : ket_qua của sinh viên là ”Đạt‘ nếu diem_tb (chỉ tính các môn đã có điểm) của sinh viên đó lớn hơn hoặc bằng 5 
	và không quá 2 môn dưới 4 điểm, ngược lại thì kết quả là không đạt Đưa dữ liệu vào bảng xếp loại. Sử dụng function
	Đối với những sinh viên có ket_qua là ”Đạt‘ thì hocLuc được xếp loại như sau:

	 diem_tb >= 8 thì hoc_luc là ”Giỏi” 7 < = diem_tb < 8 thì hoc_luc là ”Khá” Còn lại là ”Trung bình”  
*/

CREATE OR REPLACE FUNCTION DIEM_TB(
    p_ma_sv VARCHAR(10)
)
RETURNS FLOAT
LANGUAGE plpgsql
AS $$
DECLARE 
    diem_tb FLOAT;
BEGIN
    SELECT AVG(list.diem) INTO diem_tb FROM (
        SELECT DISTINCT ON (kq.ma_mon_hoc)
            kq.diem
        FROM KetQua kq
        WHERE kq.ma_sv = p_ma_sv
        ORDER BY kq.ma_mon_hoc, kq.lan_thi DESC
    ) AS list;

    RETURN diem_tb;
END;
$$;

SELECT DIEM_TB('0212003');

CREATE OR REPLACE FUNCTION KET_QUA_SV(
	p_ma_sv VARCHAR(10)
)
RETURNS VARCHAR(10)
LANGUAGE plpgsql
AS $$
DECLARE 
	cnt INT;
	diem_tb FLOAT;
	ket_qua VARCHAR(10);
BEGIN
	SELECT DIEM_TB(p_ma_sv) INTO diem_tb;
	
	SELECT COUNT(*) INTO cnt FROM (
		SELECT DISTINCT ON (ma_mon_hoc) diem FROM KetQua kq
		WHERE kq.ma_sv = p_ma_sv
		ORDER BY ma_mon_hoc, lan_thi DESC
	) AS ds
	WHERE ds.diem < 4;
	
	IF diem_tb IS NULL THEN
		ket_qua := 'Chưa thi';
	ELSEIF diem_tb >= 5 AND cnt <= 2 THEN 
		ket_qua := 'Đạt';
	ELSE
		ket_qua := 'Trượt';
	END IF;

	RETURN ket_qua;
END;
$$;

SELECT KET_QUA_SV('0212005');

CREATE OR REPLACE FUNCTION NANG_LUC_SV(
	p_ma_sv VARCHAR(10)
)
RETURNS VARCHAR(10)
LANGUAGE plpgsql
AS $$
DECLARE 
	diem_tb FLOAT;
	ket_qua VARCHAR(10);
BEGIN
	SELECT DIEM_TB(p_ma_sv) INTO diem_tb;

	IF diem_tb >= 8 THEN ket_qua := 'Giỏi';
	ELSEIF diem_tb >= 7 THEN ket_qua := 'Khá';
	ELSE ket_qua := 'Trung bình';
	END IF;
	
	RETURN ket_qua;
END;
$$;

SELECT NANG_LUC_SV('0212004');

INSERT INTO XepLoai(ma_sv, diem_tb, ket_qua, hoc_luc)
SELECT ma_sv, DIEM_TB(ma_sv), KET_QUA_SV(ma_sv), NANG_LUC_SV(ma_sv) FROM SinhVien;

SELECT * FROM XepLoai;
