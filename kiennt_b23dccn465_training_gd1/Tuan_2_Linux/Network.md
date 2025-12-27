# Một số câu lệnh cho mạng:
---
## Để kiểm tra kết nối mạng giữa client và máy khác:
```bash 
ping <địa_chỉ_IP_hoặc_tên_miền>
```
* Ví dụ : 
```bash
ngtukien@NgTuKien:~/Documents/typ-training-2025/kiennt_b23dccn465_training_gd1$ ping chatgpt.com
PING chatgpt.com (2a06:98c1:3100::6812:202f) 56 data bytes
64 bytes from 2a06:98c1:3100::6812:202f: icmp_seq=1 ttl=55 time=35.9 ms
64 bytes from 2a06:98c1:3100::6812:202f: icmp_seq=2 ttl=55 time=39.4 ms
64 bytes from 2a06:98c1:3100::6812:202f: icmp_seq=3 ttl=55 time=39.8 ms
^C
--- chatgpt.com ping statistics ---
3 packets transmitted, 3 received, 0% packet loss, time 2003ms
rtt min/avg/max/mdev = 35.945/38.366/39.801/1.721 ms
```
 * `ping -t 5 chatgpt.com`: Ping liên tục đến địa chỉ IP cho tới khi dừng (Ctrl + C), với `-t 5` là thời gian chờ 5 giây.