SELECT * FROM KetQua WHERE ma_sv = '0212003';

-- 1. Xóa nhầm dữ liệu, muốn hủy bỏ toàn bộ thay đổi <transaction bị ROLLBACK>
BEGIN;
DELETE FROM KetQua
WHERE ma_sv = '0212003' AND ma_mon_hoc = 'THCS02';
ROLLBACK;

-- 2. <transaction được COMMIT> sau khi COMMIT dữ liệu mất vĩnh viễn không ROLLBACK lại được
BEGIN;
DELETE FROM KetQua
WHERE ma_sv = '0212003' AND ma_mon_hoc = 'THCS02';
COMMIT;

-- 3. <transaction có SAVEPOINT>
INSERT INTO KetQua VALUES ('0212003', 'THCS02', 2, 10);
INSERT INTO KetQua VALUES ('0212003', 'THT01', 2, 10);

BEGIN;
SAVEPOINT GiaiDoan1;
DELETE FROM KetQua WHERE ma_sv = '0212003' AND ma_mon_hoc = 'THCS02';

SAVEPOINT GiaiDoan2;
DELETE FROM KetQua WHERE ma_sv = '0212003' AND ma_mon_hoc = 'THT01';

ROLLBACK TO SAVEPOINT GiaiDoan1; -- khôi phục phần sau GiaiDoan1
COMMIT;

-- 4. <transaction xử lý lỗi TRY-CATCH>
DO $$
BEGIN
	BEGIN
		PERFORM pg_advisory_xact_lock(1); -- đảm bảo khóa tạm thời
		INSERT INTO KetQua(ma_sv, ma_mon_hoc, lan_thi, diem)
		VALUES ('0212003', 'THCS02', 2, 10);
	
		INSERT INTO KetQua(ma_sv, ma_mon_hoc, lan_thi, diem)
	    VALUES ('0212003', 'JAVA', 1, NULL);
	
		COMMIT;
	EXCEPTION WHEN OTHERS THEN -- bắt mọi lỗi
		RAISE NOTICE 'Lỗi khi thực hiện Transaction: %', SQLERRM; -- thông báo lỗi hệ thống
        ROLLBACK;
	END;
END;
$$;

-- 5. <transaction thực tế> UPDATE + INSERT + SAVEPOINT
BEGIN;
SAVEPOINT before_insert;
INSERT INTO KetQua(ma_sv, ma_mon_hoc, lan_thi, diem)
VALUES ('0212003', 'THCS02', 2, 10);

SAVEPOINT before_update;
UPDATE KetQua
SET diem = diem - 0.5
WHERE ma_sv = '0212003' AND ma_mon_hoc = 'THCS02';

ROLLBACK TO SAVEPOINT before_update;

COMMIT;

-- 6. <transaction có điều kiện> rollback nếu điểm > 9
DO $$
DECLARE
    v_ma_sv VARCHAR(10) := '0212003';
    v_ma_mon_hoc VARCHAR(10) := 'THCS02';
    v_lan_thi INT := 3;
    v_diem FLOAT := 9.5;
BEGIN
    INSERT INTO KetQua(ma_sv, ma_mon_hoc, lan_thi, diem)
    VALUES (v_ma_sv, v_ma_mon_hoc, v_lan_thi, v_diem);

    IF v_diem > 9 THEN
        RAISE EXCEPTION 'Điểm % lớn hơn 9 → rollback tự động do exception.', v_diem;
    ELSE
        RAISE NOTICE 'Điểm % hợp lệ → giữ transaction.', v_diem;
		-- Tự động COMMIT khi Done
    END IF;

EXCEPTION WHEN OTHERS THEN
    RAISE NOTICE 'Lỗi hoặc rollback tự động: %', SQLERRM;
END;
$$;
