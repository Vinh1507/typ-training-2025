# 1. Ansible là gì?
* Ansible là một công cụ tự động hóa mã nguồn mở được sử dụng để quản lý cấu hình, triển khai ứng dụng và từ động hóa.
* Ansible được thiết kế theo một số nguyên tắc:
  * Agent-less architect: Ansible không yêu cầu cài đặt bất kỳ phần mềm nào trên các nút được quản lý. Nó sử dụng SSH để giao tiếp với các hệ thống từ xa.
  * Simplicity: Ansible sử dụng cú pháp đơn giản và dễ đọc, thường được viết bằng YAML trong các tệp gọi là **playbook**.
  * Scalability and flexibility: Ansible có thể mở rộng để quản lý từ vài hệ thống đến hàng ngàn hệ thống.
  * Idempotence and predictability: Ansible đảm bảo rằng các tác vụ có thể được chạy nhiều lần mà không gây ra các thay đổi không mong muốn.
![architect.png](Image/architect.png)
* Ansible gồm 3 thành phần:
  * Control node: Một hệ thống được cài đặt Ansible. Bạn chạy các lệnh Ansible như ansible hoặc ansible-inventory trên nút điều khiển.
  * Inventory: Danh sách các nút được quản lý.
  * Managed node: Các hệ thống từ xa được quản lý bởi Ansible.
-----
# 2. Cài đặt Ansible trên Control Node (Ubuntu 24.04)
- Có một số cách để cài đặt Assible, trong đó có 2 cách phổ biến nhất:
  * Cài đặt qua apt (dành cho Ubuntu/Debian).
  * Cài đặt qua pip (Python package manager).
  * Cài đặt qua repo chính thức của Ansible.
## Bước 1: Cập nhật hệ thống
```bash
sudo apt update
sudo apt upgrade -y
```
## Bước 2: Cài đặt Ansible (sử dụng apt)
```bash
sudo apt install ansible -y
```
## Bước 3: Kiểm tra cài đặt
```bash
typ@master:~$ ansible --version
ansible [core 2.16.3]
  config file = None
  configured module search path = ['/home/typ/.ansible/plugins/modules', '/usr/share/ansible/plugins/modules']
  ansible python module location = /usr/lib/python3/dist-packages/ansible
  ansible collection location = /home/typ/.ansible/collections:/usr/share/ansible/collections
  executable location = /usr/bin/ansible
  python version = 3.12.3 (main, Nov  6 2025, 13:44:16) [GCC 13.3.0] (/usr/bin/python3)
  jinja version = 3.1.2
  libyaml = True
```
-----
# 3. Ansible Config (Ansile.cfg)
## 3.1. Ansible.cfg là gì?
* `ansible.cfg` là tập tin cấu hình chính của Ansible, nơi có thể định nghĩa các thiết lập và tùy chọn để điều chỉnh hành vi của Ansible khi nó chạy.
* Mục đích chính của `ansible.cfg` bao gồm:
  * **Tổ chức Inventory:** Tập tin cấu hình cần thiết để tham chiếu đến inventory (danh sách các host), giúp tổ chức các máy chủ được quản lý (managed hosts).
  * **Thiết lập kết nối:** Nó định nghĩa cách thức kết nối đến các host trong inventory. Đối với hệ thống Linux, kết nối được thực hiện qua giao thức SSH.
  * **Xác định User:** Cấu hình quy định user nào sẽ được sử dụng để kết nối (ví dụ: user `DevOps`).
  * *Lưu ý:* Để tự động hóa hiệu quả, user này cần được tạo trước trên các managed hosts và đã được cài đặt SSH Public Key (trong khi Private Key nằm trên Ansible controller).
## 3.2. Các mức độ ưu tiên của tập tin cấu hình
Ansible có nhiều nguồn cấu hình khác nhau và chúng tuân theo một thứ tự ưu tiên (precedence) cụ thể, từ yếu nhất đến mạnh nhất. Ansible sẽ gộp (merge) các cài đặt lại, và nếu có xung đột, nguồn có độ ưu tiên cao hơn sẽ ghi đè nguồn thấp hơn.

Thứ tự ưu tiên như sau:

1. **`/etc/ansible/ansible.cfg` (Yếu nhất):** Đây là nơi chứa các thiết lập mặc định toàn cục cho hệ thống. Người dùng thông thường không cần quyền root để chạy Ansible, nên file này thường chỉ để tham khảo các giá trị mặc định.

![alt text](Image/conf1.png)

2. **`~/.ansible.cfg` (Thư mục Home):** Đây là file ẩn nằm trong thư mục Home của user. Nó chứa các cấu hình riêng cho user đó và sẽ ghi đè các thiết lập toàn cục.

![alt text](Image/conf2.png)

3. **`./ansible.cfg` (Thư mục hiện tại):** Nằm ngay trong thư mục dự án bạn đang đứng. Đây là cấu hình đặc thù cho dự án (project-specific) và sẽ ghi đè hai loại trên.

![alt text](Image/conf3.png)

4. **Biến môi trường `ANSIBLE_CONFIG` (Mạnh nhất):** Đây là thẩm quyền cao nhất. Nếu bạn định nghĩa biến môi trường này trỏ đến một file bất kỳ, Ansible sẽ sử dụng file đó và bỏ qua các nguồn khác.

## 3.3. Các lệnh kiểm tra và xác minh

* **Kiểm tra file cấu hình đang dùng:** Sử dụng lệnh `ansible --version`. Kết quả trả về sẽ hiển thị dòng "config file", cho biết chính xác file nào đang được Ansible sử dụng dựa trên thư mục bạn đang đứng.
* **Xem toàn bộ cấu hình hiện tại:** Sử dụng lệnh `ansible-config dump`. Lệnh này sẽ in ra tất cả các thiết lập đang có hiệu lực sau khi đã gộp và xử lý ưu tiên từ các nguồn khác nhau.
* **Tạo file cấu hình mẫu:** Bạn có thể tạo một file chứa các thiết lập mặc định (đã bị comment) để tham khảo bằng cách chạy lệnh sinh file mẫu (ví dụ: `ansible-config init --disabled > default-ansible.cfg`).

## 3.4. Tạo file cấu hình ansible.cfg cơ bản
### 3.4.1. Nhóm `[defaults]` (Cấu hình chung)
Đây là nhóm quan trọng nhất, quy định các hành vi mặc định của Ansible.

| Từ khóa | Ý nghĩa & Tác dụng | Ví dụ / Mặc định |
| --- | --- | --- |
| **`inventory`** | Đường dẫn đến file hoặc thư mục chứa danh sách host. | `./inventory` hoặc `/etc/ansible/hosts` |
| **`remote_user`** | User mặc định dùng để SSH vào máy đích. | `root` hoặc `ubuntu` |
| **`private_key_file`** | Đường dẫn đến file SSH private key (nếu bạn không dùng ssh-agent). | `~/.ssh/id_rsa` |
| **`host_key_checking`** | Kiểm tra SSH key fingerprint. Đặt `False` để tránh lỗi khi kết nối server mới lần đầu. | `False` (Rất hay dùng) |
| **`forks`** | Số lượng host được xử lý song song cùng lúc. Tăng lên giúp chạy nhanh hơn nếu máy mạnh. | `5` (Mặc định). Có thể tăng lên `20` hoặc `50`. |
| **`roles_path`** | Đường dẫn nơi Ansible tìm kiếm các Roles. | `./roles` |
| **`log_path`** | Đường dẫn file log để lưu lại lịch sử chạy lệnh (mặc định Ansible không lưu log). | `./ansible.log` |
| **`interpreter_python`** | Chỉ định phiên bản Python trên máy đích. Đặt `auto_silent` để nó tự tìm và tắt cảnh báo. | `auto_silent` |
| **`timeout`** | Thời gian chờ (giây) khi cố kết nối SSH trước khi báo lỗi. | `10` (Mặc định) |

### 3.4.2. Nhóm `[privilege_escalation]` (Cấu hình leo quyền/Sudo)

Nhóm này quản lý việc chuyển đổi quyền hạn (tương tự lệnh `sudo`).

| Từ khóa | Ý nghĩa & Tác dụng | Ví dụ |
| --- | --- | --- |
| **`become`** | Kích hoạt leo quyền (tương đương gõ sudo trước câu lệnh). | `True` hoặc `False` |
| **`become_method`** | Phương thức leo quyền. | `sudo` (phổ biến nhất), `su`, `pbrun` |
| **`become_user`** | User bạn muốn trở thành sau khi leo quyền. | `root` (Mặc định) |
| **`become_ask_pass`** | Có hỏi mật khẩu khi sudo không? Nếu server cấu hình NOPASSWD thì để False. | `False` |

### 3.4.3. Nhóm `[ssh_connection]` (Tối ưu kết nối)

Nhóm này dành cho người dùng nâng cao muốn tối ưu tốc độ và sự ổn định của kết nối SSH.

| Từ khóa | Ý nghĩa & Tác dụng | Ví dụ |
| --- | --- | --- |
| **`pipelining`** | Giảm số lượng kết nối SSH cần thiết để thực thi module. Giúp Ansible chạy **nhanh hơn rất nhiều**. | `True` (Nên bật nếu không dùng `requiretty` trong sudoers) |
| **`ssh_args`** | Các tham số truyền thêm vào lệnh SSH. Thường dùng để giữ kết nối sống (KeepAlive). | `-o ControlMaster=auto -o ControlPersist=60s` |
| **`retries`** | Số lần thử lại nếu kết nối SSH thất bại. | `3` |

# 4. Invectory trong Ansible
### 4.1. Tổng quan về Inventory
* **Inventory** là một thành phần quan trọng trong Ansible, nó là nơi lưu trữ danh sách các máy chủ (hosts) mà Ansible sẽ quản lý và thực thi các tác vụ trên đó.
* Inventory có thể được định nghĩa dưới dạng:
  * **File tĩnh (Static Inventory):** Là các file văn bản (thường là định dạng INI hoặc YAML) chứa danh sách các host và nhóm host.
  * **File động (Dynamic Inventory):** Là các script hoặc chương trình tạo ra danh sách host một cách tự động từ các nguồn bên ngoài như dịch vụ đám mây (AWS, GCP, Azure).
* Inventory giúp tổ chức các máy chủ thành các nhóm (groups) để dễ dàng quản lý và thực thi các tác vụ trên các nhóm máy chủ cụ thể.
### 4.2. Cấu hình đường dẫn Inventory

Trong file cấu hình `ansible.cfg`:

* Chỉ thị `inventory` có thể trỏ đến một **thư mục** (ví dụ: `./my_inventory`) thay vì một file đơn lẻ.
* Khi trỏ vào thư mục, Ansible sẽ tự động xử lý và gộp tất cả các file nằm trong thư mục đó để tạo thành inventory hoàn chỉnh.

### 3. Cách viết file Inventory (Định dạng INI)

Dưới đây là các kỹ thuật gom nhóm (grouping) được đề cập trong video:

#### a. Gom nhóm cơ bản (Basic Grouping)

Sử dụng dấu ngoặc vuông `[]` để đặt tên nhóm. Một máy chủ có thể thuộc về nhiều nhóm khác nhau (ví dụ: vừa là web server, vừa là rhel9 server).

```ini
[webservers]
servera
serverc

[dbservers]
serverb
serverd

```

#### b. Sử dụng dải số (Ranges)

Thay vì liệt kê từng máy, bạn có thể dùng dải ký tự/số để khai báo nhanh.

```ini
[rhel9]
# Bao gồm servera, serverb, serverc, serverd
server[a:d]

```

#### c. Nhóm lồng nhau (Nested Groups / Children)

Để tạo một "nhóm cha" chứa các "nhóm con", bạn **bắt buộc** phải sử dụng từ khóa `:children`.

* **Sai:** Nếu chỉ viết tên nhóm con dưới tên nhóm cha, Ansible sẽ hiểu lầm đó là tên của một máy chủ (host).
* **Đúng:** Phải thêm hậu tố `:children`.

```ini
# Nhóm cha là 'rhel'
[rhel:children]
# Nhóm con
rhel9
rhel8

```

*(Lúc này, Ansible hiểu `rhel9` là một nhóm các máy chủ, chứ không phải là một máy chủ tên là rhel9).*

### 4. Cách kiểm tra Inventory

Tác giả video khuyên dùng các lệnh sau để kiểm tra xem Ansible đã hiểu đúng cấu trúc file inventory hay chưa:

* **`ansible-inventory --graph`** (Khuyên dùng):
* Hiển thị cấu trúc dạng cây (tree layout).
* Dễ đọc, trực quan.
* Các nhóm sẽ có tiền tố `@` (ví dụ: `@all`, `@webservers`).
* Hiển thị rõ nhóm mặc định `all` và `ungrouped`.


* **`ansible-inventory --list`**:
* Hiển thị dữ liệu dạng **JSON**.
* Chứa đầy đủ thông tin chi tiết nhưng khó đọc hơn đối với con người.



