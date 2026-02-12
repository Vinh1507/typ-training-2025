-- Trigger: Tự động ghi log khi INSERT vào ThamGia
CREATE TRIGGER trg_InsertThamGia
ON ThamGia
AFTER INSERT
AS
BEGIN
    PRINT N'Vận động viên mới đã tham gia giải đấu!';
END;

-- Transaction
BEGIN TRANSACTION
    UPDATE VDV SET QuocGia = N'Mỹ' WHERE MaVDV = 1;
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

-- Procedure: Lấy danh sách VDV theo quốc gia
CREATE PROCEDURE sp_LayVDVTheoQuocGia
    @QuocGia NVARCHAR(50)
AS
BEGIN
    SELECT * FROM VDV WHERE QuocGia = @QuocGia;
END;

-- Function: Tính tuổi VDV
CREATE FUNCTION fn_TinhTuoi (@NgaySinh DATE)
RETURNS INT
AS
BEGIN
    RETURN DATEDIFF(YEAR, @NgaySinh, GETDATE());
END;