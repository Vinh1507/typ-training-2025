-- 1. Với mã sinh viên và 1 mã khoa, kiểm tra xem sinh viên có thuộc khoa này không (trả về đúng hay sai).
CREATE OR REPLACE FUNCTION KF_Kiem_Tra_Sv_Trong_Khoa(
	p_ma_sv varchar(10),
	p_ma_khoa varchar(10)
)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
DECLARE
	v_ket_qua BOOLEAN;
BEGIN
	IF EXISTS (
		SELECT * FROM SinhVien sv
		JOIN Lop l ON l.ma_lop = sv.ma_lop
		JOIN Khoa k ON k.ma_khoa = l.ma_khoa
		WHERE k.ma_khoa = p_ma_khoa
		AND sv.ma_sv = p_ma_sv
	) THEN
		v_ket_qua := TRUE;
	ELSE
		v_ket_qua := FALSE;
	END IF;

	RETURN v_ket_qua;
END;
$$

SELECT KF_Kiem_Tra_Sv_Trong_Khoa('0212003', 'VL');
SELECT KF_Kiem_Tra_Sv_Trong_Khoa('0212003', 'CNTT');

-- 2. Tính điểm thi sau cùng của một sinh viên trong một môn học cụ thể.
CREATE OR REPLACE FUNCTION KF_Diem_Thi_Cuoi_Cua_Sv(
	p_ma_sv varchar(10),
	p_ma_mon_hoc varchar(10)
)
RETURNS FLOAT
LANGUAGE plpgsql
AS $$
DECLARE
	diem FLOAT;
BEGIN
	SELECT kq.diem INTO diem FROM KetQua kq
	WHERE ma_sv = p_ma_sv
	AND ma_mon_hoc = p_ma_mon_hoc
	ORDER BY kq.lan_thi DESC
	LIMIT 1;
	
	RETURN diem;
END;
$$

SELECT KF_Diem_Thi_Cuoi_Cua_Sv('0212003', 'THT02');

-- 3. Tính điểm trung bình của sinh viên (chú ý: điểm trung bình được tính dựa trên lần thi sau cùng) sử dụng function 2 đã viết.
CREATE OR REPLACE FUNCTION KF_Diem_Tb_Cua_Sinh_Vien(
    p_ma_sv VARCHAR(10)
)
RETURNS FLOAT
LANGUAGE plpgsql
AS $$
DECLARE
    ket_qua FLOAT;
BEGIN
    SELECT AVG(KF_Diem_Thi_Cuoi_Cua_Sv(kq.ma_sv, kq.ma_mon_hoc))
    INTO ket_qua
    FROM KetQua kq
    WHERE kq.ma_sv = p_ma_sv;

    RETURN ket_qua;
END;
$$;

SELECT KF_Diem_Tb_Cua_Sinh_Vien('0212002');

-- 4. Nhập vào 1 sinh viên và 1 môn học, trả về các điểm thi của sinh viên này trong các lần thi của môn học đó.
CREATE OR REPLACE FUNCTION KF_Diem_Thi_Sv_Trong_Cac_Lan_Thi_Cua_Mh(
    p_ma_sv VARCHAR(10),
    p_ma_mon_hoc VARCHAR(10)
)
RETURNS TABLE(lan_thi INT, diem FLOAT)
LANGUAGE plpgsql
AS $$
BEGIN
	RETURN QUERY
    SELECT kq.lan_thi, kq.diem FROM KetQua kq
    WHERE kq.ma_sv = p_ma_sv
    AND kq.ma_mon_hoc = p_ma_mon_hoc;
END;
$$;


SELECT * FROM KF_Diem_Thi_Sv_Trong_Cac_Lan_Thi_Cua_Mh('0212001', 'THT01');

-- 5. Nhập vào 1 sinh viên, trả về danh sách các môn học mà sinh viên này phải học.
CREATE OR REPLACE FUNCTION KF_Danh_Sach_Mon_Hoc_Sv_Phai_Hoc(
	p_ma_sv varchar(10)
)
RETURNS TABLE(ma_mon_hoc varchar(10), ten_mon_hoc varchar(100))
LANGUAGE plpgsql
AS $$
BEGIN
	RETURN QUERY
	SELECT mh.ma_mon_hoc, mh.ten_mon_hoc FROM MonHoc mh
	JOIN Khoa k ON k.ma_khoa = mh.ma_khoa
	JOIN Lop l ON l.ma_khoa = k.ma_khoa
	JOIN SinhVien sv ON l.ma_lop = sv.ma_lop
	WHERE sv.ma_sv = p_ma_sv;
END;
$$;

SELECT * FROM KF_Danh_Sach_Mon_Hoc_Sv_Phai_Hoc('0212001')