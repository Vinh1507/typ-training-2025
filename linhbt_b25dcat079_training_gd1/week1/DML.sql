
INSERT INTO VDV VALUES (1, N'Nguyễn Văn A', '1995-06-20', N'Việt Nam');
INSERT INTO GiaiDau VALUES (101, N'Giải Vô Địch Quốc Gia', 2025, N'Hà Nội');
INSERT INTO ThamGia VALUES (1, 101, N'HCV');

SELECT TenVDV, TenGiai, ThanhTich
FROM VDV
JOIN ThamGia ON VDV.MaVDV = ThamGia.MaVDV
JOIN GiaiDau ON ThamGia.MaGiai = GiaiDau.MaGiai
WHERE QuocGia = N'Việt Nam'
ORDER BY TenVDV;

UPDATE ThamGia
SET ThanhTich = N'HCB'
WHERE MaVDV = 1 AND MaGiai = 101;

DELETE FROM ThamGia WHERE MaVDV = 1 AND MaGiai = 101;