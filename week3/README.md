## Phần 1: Tổng quan về ảo hoá

1. **Khái niệm ảo hoá(Virtualization)**
- Khái niệm ảo hoá
- Mục đích ảo hoá

2. **Các loại ảo hoá và các công nghệ ảo hóa thông dụng**
- Khái niệm, phân loại ảo hoá
- Liệt kê các công nghệ ảo hóa thông dụng, đang dùng công nghệ nào, áp dụng loại ảo hoá nào

3. **Virtual Machine**
- Khái niệm
- Trong 1 VM, các thành phần nào có thể ảo hoá? Ảo hóa được hỗ trợ bởi Phần cứng? Ảo hoá bán phần?
- Vòng đời và quản lý VM

## Phần 2: Container

1. **Container là gì?**
- Khái niệm
- So sánh Container và VM

2. **Docker**

    2.1. Tổng quan & Cài đặt Docker
    * Overview về docker container
    * Cài đặt docker (trên linux)
    * Cấu hình proxy
        * Cho docker daemon
        * Cho docker client
    * Cấu hình trusted registry trong daemon.json tham khảo Local Repos
    * Hello world với Docker

    2.2. Các câu lệnh cơ bản
    * Docker tag
    * Docker run
    * Docker build
    * Docker push
    * Docker pull

    2.3. Kiến trúc Docker
    * Docker CE/Docker EE
    * Docker Engine
    * Docker Compose
    * Docker Registry
    * Docker Daemon
    * Docker network: Host, Bridge, Overlay, ...
    * Docker volume, mount

    2.4. Docker-compose
    * Định nghĩa
    * Thực hành chạy file-config