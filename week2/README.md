## Phần 1: Giới thiệu tổng quan về Linux

1. **Linux là gì?**

   - Khái niệm hệ điều hành mã nguồn mở.
   - Lịch sử phát triển của Linux (Linus Torvalds, 1991).
   - Phân biệt giữa Linux kernel và các bản phân phối (Ubuntu, CentOS, Debian...).

2. **Tại sao nên học Linux**

   - Ứng dụng rộng rãi trong server, cloud, DevOps, AI, lập trình hệ thống.
   - Sự khác biệt giữa Linux và Windows/macOS.
   - Ưu điểm: bảo mật, miễn phí, tùy biến, hiệu suất cao.

3. **Kiến trúc hệ thống Linux**
   - Kernel, Shell, Application layer.
   - Quá trình khởi động (boot process) cơ bản.
   - Các thư mục hệ thống chính (`/bin`, `/etc`, `/home`, `/usr`, `/var`...).

---

## Phần 2: Làm quen với Terminal và Shell

4. **Terminal & Shell là gì**

   - Terminal: giao diện dòng lệnh (CLI).
   - Shell: chương trình trung gian giữa người dùng và kernel (bash, zsh...).
   - Mở terminal, chạy lệnh cơ bản.

5. **Lệnh cơ bản trong Linux**

   - `pwd`, `ls`, `cd`, `clear`, `history`.
   - Sử dụng phím tắt: `Tab`, `Ctrl + C`, `Ctrl + D`.

6. **Hiểu cấu trúc đường dẫn**
   - Đường dẫn tuyệt đối vs tương đối.
   - Dấu `~`, `.`, `..` ý nghĩa và cách dùng.

---

## Phần 3: Làm việc với file và thư mục

7. **Tạo, xem, xóa và di chuyển file**

   - `touch`, `cat`, `less`, `head`, `tail`.
   - `cp`, `mv`, `rm`, `mkdir`, `rmdir`.

8. **Sao chép và nén file**

   - `tar`, `gzip`, `zip`, `unzip`, `scp`.
   - Giải thích khái niệm stream (`stdin`, `stdout`, `stderr`).

9. **Tìm kiếm file**
   - `find`, `locate`, `grep`.
   - Kết hợp `grep` với `pipe (|)`.

---

## Phần 4: Quyền truy cập và người dùng

10. **Người dùng và nhóm (User & Group)**

    - `whoami`, `id`, `adduser`, `deluser`.
    - `su`, `sudo`, `/etc/passwd`.

11. **Phân quyền file**

    - `ls -l`, quyền đọc/ghi/thực thi (`rwx`).
    - `chmod`, `chown`, `chgrp`.

12. **Quyền root và an toàn**
    - Tại sao không nên chạy mọi thứ bằng root.
    - Phân biệt `sudo` và `su`.

---

## Phần 5: Quản lý tiến trình và hệ thống

13. **Quản lý tiến trình (process)**

    - `ps`, `top`, `htop`, `kill`, `killall`.
    - Foreground và background process (`&`, `fg`, `bg`).

14. **Kiểm tra tài nguyên hệ thống**

    - `df`, `du`, `free`, `uptime`, `uname`, `lscpu`, `lsblk`.

15. **Dịch vụ và tiến trình nền (daemon)**
    - `systemctl`, `service`, `journalctl`.
    - Start/stop/restart dịch vụ.

---

## Phần 6: Quản lý gói phần mềm

16. **Trình quản lý gói (Package Manager)**

    - Debian/Ubuntu: `apt`, `apt-get`.
    - RedHat/CentOS: `yum`, `dnf`.

17. **Cài đặt và gỡ bỏ phần mềm**

    - `sudo apt install`, `sudo apt remove`, `apt update`, `apt upgrade`.
    - Kiểm tra package đang cài đặt: `dpkg -l`.

18. **Tạo và sử dụng alias**
    - Tạo lệnh tắt trong file `~/.bashrc`.
    - `alias ll='ls -alF'`.

---

## Phần 7: Làm việc với mạng (Networking)

19. **Lệnh kiểm tra mạng**

    - `ping`, `ifconfig`, `ip addr`, `netstat`, `ss`, `curl`, `wget`.

20. **Kết nối SSH và truyền file**

    - `ssh user@ip`, `scp`, `rsync`.
    - Thiết lập SSH key (`ssh-keygen`, `ssh-copy-id`).

21. **Kiểm tra cổng và firewall**
    - `ufw`, `iptables`, `ss -tuln`.

---

## Phần 8: Script & Automation cơ bản

22. **Shell Script là gì**

    - File `.sh`, cú pháp `#!/bin/bash`.
    - Cách chạy: `bash script.sh` hoặc `chmod +x script.sh`.

23. **Biến, vòng lặp và điều kiện**

    - `if`, `for`, `while`, biến môi trường (`$USER`, `$HOME`).

24. **Tự động hóa tác vụ**
    - `cron`, `crontab -e`.
    - Lên lịch chạy script.

---

## Phần 9: Thực hành tổng hợp

25. **Bài tập thực tế**
    - Tạo user mới, cấp quyền hạn chế.
    - Tạo và nén backup thư mục.
    - Viết script tự động sao lưu log hệ thống hàng ngày.
    - Dò tìm file lớn nhất trong thư mục home.
    - Cấu hình SSH và kiểm tra kết nối.

---
