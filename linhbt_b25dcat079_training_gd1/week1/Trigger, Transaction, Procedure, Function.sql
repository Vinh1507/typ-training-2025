
CREATE TRIGGER trg_InsertThamGia
ON ThamGia
AFTER INSERT
AS
BEGIN
    PRINT N'Vận động viên mới đã tham gia giải đấu!';
END;

BEGIN TRANSACTION
    UPDATE VDV SET QuocGia = N'Mỹ' WHERE MaVDV = 1;
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
CREATE PROCEDURE sp_LayVDVTheoQuocGia
    @QuocGia NVARCHAR(50)
AS
BEGIN
    SELECT * FROM VDV WHERE QuocGia = @QuocGia;
END;
CREATE FUNCTION fn_TinhTuoi (@NgaySinh DATE)
RETURNS INT
AS
BEGIN
    RETURN DATEDIFF(YEAR, @NgaySinh, GETDATE());
END;