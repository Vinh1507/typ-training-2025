# Triển khai bằng Linux
1. Clone dự án và tạo user và cấp các quyền cho project
![Minh hoa](../images/lab1_linux.png) 

2. Cài đặt môi trường, thư viện chạy

 ![Minh hoa](../images/linux2.png) 

3. Thiết lập Database
    ![Minh hoa](../images/linux3.png) 

    ![Minh hoa](../images/linux4.png) 

    ![Minh hoa](../images/linux5.png) 

    ![Minh hoa](../images/linux6.png) 

4. Khởi chạy java Spring bôt
    ![Minh hoa](../images/linux7.png) 

    ![Minh hoa](../images/linux8.png) 

5. Triển khai Front end
   - Sửa file cấu hình phía service `http` khi gọi tới port BackendEnd
   
    ![Minh hoa](../images/linuxend.png) 


# Triển khai Docker
Bước 1: Viết dockerfile cho frontend và chạy lệnh `build` để kiểm tra.

![Minh hoa](../images/FE.png) 
![Minh hoa](../images/runFE.png) 

Bước 2: Viết dockerfile cho backend và chạy lệnh `build` để kiểm tra.

![Minh hoa](../images/BE.png)



### Bài LAB Nexus 
Bước 1: 
![Minh hoa](../images/setupnexus.png) 

File cấu hình:
```
  version: '3'
  services:
    nexus:
      image: sonatype/nexus3:latest
      ports:
        - "8081:8081"
        - "5000:5000"
      volumes:
        - nexus-data:/nexus-data
      environment:
        - NEXUS_SECURITY_RANDOMPASSWORD=false
        - NEXUS_CONTEXT=/  # Context path
      restart: always
  volumes:
    nexus-data:
```

Chạy lệnh `  docker-compose up -d`

Truy cập `192.168.126.99:8081`
Đăng nhập: admin
Pass: admin123

![Minh hoa](../images/login.png) 






