-- 1. kiểm tra điểm đầu vào có nằm trong [0, 10]
CREATE OR REPLACE FUNCTION trg_check_diem()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
	-- Kiểm tra mã sinh viên có tồn tại không
	IF NOT EXISTS (
        SELECT 1 FROM SinhVien sv WHERE sv.ma_sv = NEW.ma_sv
    ) THEN
        RAISE EXCEPTION 'Không tìm thấy mã sinh viên: %', NEW.ma_sv;
    END IF;

	-- Kiểm tra mã môn học có tồn tại không
	IF NOT EXISTS (
        SELECT 1 FROM MonHoc mh WHERE mh.ma_mon_hoc = NEW.ma_mon_hoc
    ) THEN
        RAISE EXCEPTION 'Không tìm thấy mã môn học: %', NEW.ma_mon_hoc;
    END IF;
	
	-- Kiểm tra điểm hợp lệ
    IF NEW.diem < 0 OR NEW.diem > 10 THEN
        RAISE EXCEPTION 'Điểm phải nằm trong khoảng [0, 10], giá trị hiện tại: %', NEW.diem;
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_check_diem_ketqua
BEFORE INSERT OR UPDATE
ON KetQua
FOR EACH ROW
EXECUTE FUNCTION trg_check_diem();

INSERT INTO KetQua(ma_sv, ma_mon_hoc, lan_thi, diem)
VALUES ('0212003', 'THCS02', 1, 12);

INSERT INTO KetQua(ma_sv, ma_mon_hoc, lan_thi, diem)
VALUES ('0212003', 'THCS02', 1, 11);


-- 2. Không cho phép xóa xếp loại sinh viên đã có điểm_tb
CREATE OR REPLACE FUNCTION TRG_NO_DELETE_XEPLOAI()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
	IF NOT EXISTS (
        SELECT 1 FROM SinhVien sv
        WHERE sv.ma_sv = OLD.ma_sv
    ) THEN
        RAISE EXCEPTION 'Không tìm thấy mã sinh viên: %', OLD.ma_sv;
    END IF;
	
	IF OLD.diem_tb IS NOT NULL THEN
        RAISE EXCEPTION 'Không được xóa xếp loại của sinh viên đã có điểm trung bình: % % điểm', 
            OLD.ma_sv, OLD.diem_tb;
    END IF;
	
	RETURN OLD;
END;
$$;

CREATE TRIGGER NO_DELETE_XEPLOAI
BEFORE DELETE ON XepLoai
FOR EACH ROW
EXECUTE FUNCTION TRG_NO_DELETE_XEPLOAI();

DELETE FROM XepLoai WHERE ma_sv = '0311002';

SELECT * FROM XepLoai;