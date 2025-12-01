
CREATE TABLE VDV (
    MaVDV INT PRIMARY KEY,
    TenVDV NVARCHAR(100),
    NgaySinh DATE,
    QuocGia NVARCHAR(50)
);

CREATE TABLE GiaiDau (
    MaGiai INT PRIMARY KEY,
    TenGiai NVARCHAR(100),
    Nam INT,
    DiaDiem NVARCHAR(100)
);

CREATE TABLE ThamGia (
    MaVDV INT,
    MaGiai INT,
    ThanhTich NVARCHAR(100),
    PRIMARY KEY (MaVDV, MaGiai),
    FOREIGN KEY (MaVDV) REFERENCES VDV(MaVDV),
    FOREIGN KEY (MaGiai) REFERENCES GiaiDau(MaGiai)
);