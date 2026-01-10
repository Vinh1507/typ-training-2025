# 1. Ansible là gì?
* Ansible là một công cụ tự động hóa mã nguồn mở được sử dụng để quản lý cấu hình, triển khai ứng dụng và từ động hóa.
* Ansible được thiết kế theo một số nguyên tắc:
  * Agent-less architect: Ansible không yêu cầu cài đặt bất kỳ phần mềm nào trên các nút được quản lý. Nó sử dụng SSH để giao tiếp với các hệ thống từ xa.
  * Simplicity: Ansible sử dụng cú pháp đơn giản và dễ đọc, thường được viết bằng YAML trong các tệp gọi là **playbook**.
  * Scalability and flexibility: Ansible có thể mở rộng để quản lý từ vài hệ thống đến hàng ngàn hệ thống.
  * Idempotence and predictability: Ansible đảm bảo rằng các tác vụ có thể được chạy nhiều lần mà không gây ra các thay đổi không mong muốn.

![architect.png](Image/architect.png)

* Ansible gồm 3 thành phần:
  * Control node: Một hệ thống được cài đặt Ansible. Dev chạy các lệnh Ansible như ansible hoặc ansible-inventory trên nút điều khiển.
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

3. **`./ansible.cfg` (Thư mục hiện tại):** Nằm ngay trong thư mục dự án đang đứng. Đây là cấu hình đặc thù cho dự án (project-specific) và sẽ ghi đè hai loại trên.

![alt text](Image/conf3.png)

4. **Biến môi trường `ANSIBLE_CONFIG` (Mạnh nhất):** Đây là thẩm quyền cao nhất. Nếu định nghĩa biến môi trường này trỏ đến một file bất kỳ, Ansible sẽ sử dụng file đó và bỏ qua các nguồn khác.

## 3.3. Các lệnh kiểm tra và xác minh

* **Kiểm tra file cấu hình đang dùng:** Sử dụng lệnh `ansible --version`. Kết quả trả về sẽ hiển thị dòng "config file", cho biết chính xác file nào đang được Ansible sử dụng dựa trên thư mục đang đứng.
* **Xem toàn bộ cấu hình hiện tại:** Sử dụng lệnh `ansible-config dump`. Lệnh này sẽ in ra tất cả các thiết lập đang có hiệu lực sau khi đã gộp và xử lý ưu tiên từ các nguồn khác nhau.
* **Tạo file cấu hình mẫu:** Có thể tạo một file chứa các thiết lập mặc định (đã bị comment) để tham khảo bằng cách chạy lệnh sinh file mẫu (ví dụ: `ansible-config init --disabled > default-ansible.cfg`).

## 3.4. Tạo file cấu hình ansible.cfg cơ bản
### 3.4.1. Nhóm `[defaults]` (Cấu hình chung)
Đây là nhóm quan trọng nhất, quy định các hành vi mặc định của Ansible.

| Từ khóa | Ý nghĩa & Tác dụng | Ví dụ / Mặc định |
| --- | --- | --- |
| **`inventory`** | Đường dẫn đến file hoặc thư mục chứa danh sách host. | `./inventory` hoặc `/etc/ansible/hosts` |
| **`remote_user`** | User mặc định dùng để SSH vào máy đích. | `root` hoặc `ubuntu` |
| **`private_key_file`** | Đường dẫn đến file SSH private key (nếu không dùng ssh-agent). | `~/.ssh/id_rsa` |
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
-----
# 4. Inventory trong Ansible
## 4.1. Tổng quan về Inventory
* **Inventory** là một thành phần quan trọng trong Ansible, nó là nơi lưu trữ danh sách các máy chủ (hosts) mà Ansible sẽ quản lý và thực thi các tác vụ trên đó.
* Inventory có thể được định nghĩa dưới dạng:
  * **File tĩnh (Static Inventory):** Là các file văn bản (thường là định dạng INI hoặc YAML) chứa danh sách các host và nhóm host.
  * **File động (Dynamic Inventory):** Là các script hoặc chương trình tạo ra danh sách host một cách tự động từ các nguồn bên ngoài như dịch vụ đám mây (AWS, GCP, Azure).
* Inventory giúp tổ chức các máy chủ thành các nhóm (groups) để dễ dàng quản lý và thực thi các tác vụ trên các nhóm máy chủ cụ thể.
## 4.2. Cấu hình đường dẫn Inventory

Trong file cấu hình `ansible.cfg`:

* Chỉ thị `inventory` có thể trỏ đến một **thư mục** (ví dụ: `./my_inventory`) thay vì một file đơn lẻ.
* Khi trỏ vào thư mục, Ansible sẽ tự động xử lý và gộp tất cả các file nằm trong thư mục đó để tạo thành inventory hoàn chỉnh.

## 4.3. Cách viết file Inventory

### 4.3.1. Quản lý Nhóm (Groups)

Ansible cho phép gom các máy chủ vào các nhóm để dễ quản lý.
* **Nhóm mặc định:**
  * `all`: Chứa tất cả các host.
  * `ungrouped`: Chứa các host không thuộc nhóm nào (trừ nhóm `all`).
* **Một host thuộc nhiều nhóm:** Có thể phân loại host theo tiêu chí:
  * **What (Cái gì):** Ứng dụng, Database, Web...
  * **Where (Ở đâu):** Region (East, West), Datacenter...
  * **When (Khi nào):** Môi trường (Prod, Test, Dev).
* **Nhóm lồng nhau (Parent/Child Groups):** Tạo nhóm cha chứa các nhóm con.
  * *INI:* Dùng hậu tố `:children`.
  * *YAML:* Dùng mục `children:`.
  * *Lợi ích:* Giúp quản lý biến chung cho cả một tập hợp lớn (ví dụ: nhóm `prod` chứa `east` và `west`).

### 4.3.2. Khai báo Host hàng loạt (Ranges)
Nếu tên host tuân theo quy tắc số hoặc chữ cái, Ansible cho phép khai báo theo dải thay vì liệt kê từng cái.
* Ví dụ: `www[01:50].example.com` (từ www01 đến www50).
* Có thể quy định bước nhảy (stride): `www[01:50:2]` (chỉ lấy số lẻ: 01, 03, 05...).

### 4.3.3. Nguồn Inventory (Inventory Sources)
* **Thứ tự ưu tiên:** Ansible sẽ load inventory theo thứ tự:
  1. Biến môi trường `ANSIBLE_INVENTORY`.
  2. File cấu hình `ansible.cfg` (tham số `inventory`).
  3. File mặc định `/etc/ansible/hosts`.
* **Nhiều nguồn:** Có thể dùng nhiều file inventory cùng lúc bằng cách dùng tham số `-i` nhiều lần hoặc trỏ vào một thư mục chứa nhiều file.
* **Thứ tự load:** Ansible load file theo thứ tự bảng chữ cái. File load sau có thể ghi đè thông tin của file trước.

### 4.3.4. Biến trong Inventory (Inventory Variables)
Có thể gán biến (variables) cho từng host hoặc cả nhóm.
* **Host Variables:** Gán riêng cho 1 máy.
  * *INI:* Viết cùng dòng với host (`host1 http_port=80`).
  * *YAML:* Khai báo dưới mục host đó.

* **Group Variables:** Gán cho cả nhóm (tất cả máy trong nhóm đều nhận biến này).
  * *INI:* Dùng section `[ten_nhom:vars]`.
  * *YAML:* Dùng mục `vars:`.


* **Tổ chức biến (Khuyên dùng):** Thay vì viết hết vào file inventory, nên tách ra thư mục riêng:
  * `/etc/ansible/host_vars/`: Chứa file biến cho từng host.
  * `/etc/ansible/group_vars/`: Chứa file biến cho từng nhóm.


* **Thứ tự ưu tiên (Precedence):** Biến cụ thể sẽ ghi đè biến chung.
* Thấp nhất: Nhóm `all` -> Nhóm cha -> Nhóm con -> Cao nhất: Host.

### 4.3.5. Các tham số kết nối (Behavioral Inventory Parameters)
Đây là các biến đặc biệt để điều khiển cách Ansible kết nối SSH tới máy đích:

**Kết nối chung:**
  * `ansible_host`: IP hoặc Hostname thực tế để kết nối (nếu khác với tên alias trong inventory).
  * `ansible_port`: Cổng SSH (mặc định 22).
  * `ansible_user`: Username để đăng nhập SSH.
  * `ansible_password`: Mật khẩu đăng nhập (Khuyên dùng Ansible Vault để bảo mật, không nên lưu text rõ).

**Kết nối SSH:**
  * `ansible_ssh_private_key_file`: Đường dẫn tới file private key (nếu không dùng ssh-agent).
  * `ansible_connection`: Loại kết nối (mặc định là `ssh`, có thể là `local`, `winrm`...).

**Leo thang đặc quyền (Privilege Escalation - Sudo):**
  * `ansible_become`: Đặt là `yes` để bật chế độ sudo.
  * `ansible_become_user`: User muốn trở thành (thường là `root`).
  * `ansible_become_password`: Mật khẩu sudo.

**Môi trường Python:**
  * `ansible_python_interpreter`: Đường dẫn tới Python trên máy đích (Hữu ích nếu máy đích cài Python ở vị trí lạ hoặc dùng Python 2/3 lẫn lộn).

### 4.3.6. Các ví dụ tổ chức Inventory (Inventory Setup Examples)
* **Theo môi trường:** Tạo file riêng cho mỗi môi trường (`inventory_test`, `inventory_staging`, `inventory_prod`) để tránh chạy nhầm lệnh lên Production.
* **Theo chức năng:** Gom nhóm dbserver, appserver để chạy các task cài đặt firewall hoặc phần mềm đặc thù.
* **Theo vị trí:** Gom nhóm theo Datacenter (DC1, DC2) để xử lý các vấn đề hạ tầng cục bộ.

## 4.4. Cách kiểm tra Inventory
* **`ansible-inventory --graph`** (Khuyên dùng):
  * Hiển thị cấu trúc dạng cây (tree layout).
  * Dễ đọc, trực quan.
  * Các nhóm sẽ có tiền tố `@` (ví dụ: `@all`, `@webservers`).
  * Hiển thị rõ nhóm mặc định `all` và `ungrouped`.
* **`ansible-inventory --list`**:
  * Hiển thị dữ liệu dạng **JSON**.
  * Chứa đầy đủ thông tin chi tiết nhưng khó đọc hơn đối với con người.
-----
# 5. Ad-Hoc commands trong Ansible.
## 5.1. Giới thiệu về Ad-hoc Commands
**Ad-hoc command** là các lệnh chạy nhanh, thực hiện **một nhiệm vụ duy nhất** trên một hoặc nhiều máy chủ.
* **Đặc điểm:** Nhanh, dễ dùng, nhưng không lưu lại để tái sử dụng (khác với Playbook).
* **Khi nào dùng:** Cho các việc hiếm khi lặp lại (ví dụ: tắt toàn bộ máy trong phòng Lab để nghỉ lễ, check nhanh thông tin hệ thống).
* **Cấu trúc lệnh:**
  ```bash
  ansible [nhóm_máy] -m [tên_module] -a "[tham_số]"
  ```
## 5.2. Các trường hợp sử dụng phổ biến
**Ad-hoc** cũng hoạt động dựa trên mô hình **khai báo (declarative)** và **tính bất biến (idempotence)**: Nó kiểm tra trạng thái hiện tại, nếu máy đích đã ở đúng trạng thái mong muốn rồi thì nó sẽ không làm gì cả.
### 5.2.1. Khởi động lại Server (Reboot)
Module mặc định của Ansible là `command` (không cần gõ `-m command`).
* **Reboot toàn bộ nhóm [chatbot]:**
`ansible chatbot -a "/sbin/reboot"`
* **Chạy song song (Forks):** Mặc định Ansible chạy 5 luồng cùng lúc. Muốn reboot 10 máy cùng lúc để nhanh hơn:
`ansible chatbot -a "/sbin/reboot" -f 10`
* **Quyền admin (Sudo):** Dùng `--become` để chạy với quyền root (nếu cần nhập pass sudo thì thêm `-K`):
`ansible chatbot -a "/sbin/reboot" --become -K`
* **Lưu ý quan trọng:** Module `command` không hỗ trợ các ký tự đặc biệt của shell như `|` (pipe), `>` (redirect). Nếu cần dùng chúng, hãy đổi sang module **`shell`** (`-m shell`).
### 5.2.2. Quản lý File (File Transfer)
Tận dụng sức mạnh của SCP để copy file hàng loạt.
* **Copy file:**
`ansible chatbot -m copy -a "src=/etc/hosts dest=/tmp/hosts"`
* **Thay đổi quyền/tạo thư mục (chmod/chown/mkdir):** Dùng module `file`.
`ansible webservers -m file -a "dest=/srv/foo/a.txt mode=600 owner=mdehaan"`
* **Xóa file/thư mục:**
`ansible webservers -m file -a "dest=/path/to/c state=absent"`
### 5.2.3. Quản lý Gói phần mềm (Packages)
Dùng module tương ứng với OS (ví dụ `yum` cho CentOS/RHEL, `apt` cho Ubuntu).
* **Cài đặt (không update):** `state=present`
* **Cài bản mới nhất:** `state=latest`
* **Gỡ bỏ phần mềm:** `state=absent`
*Ví dụ:* `ansible webservers -m yum -a "name=acme state=latest"`
### 5.2.4. Quản lý Người dùng (Users)
Tạo hoặc xóa user nhanh chóng.
* **Tạo user:** `ansible all -m user -a "name=foo password=<pass_đã_mã_hóa>"`
* **Xóa user:** `ansible all -m user -a "name=foo state=absent"`
### 5.2.5. Quản lý Dịch vụ (Services)
Bật/tắt/restart các service như Apache, Nginx...
* **Start:** `ansible webservers -m service -a "name=httpd state=started"`
* **Restart:** `state=restarted`
* **Stop:** `state=stopped`
## 5.3. Thu thập thông tin (Gathering Facts)
Ansible có thể lấy toàn bộ thông tin phần cứng/phần mềm của máy đích (IP, OS, RAM, CPU...).
* Lệnh: `ansible all -m setup`
## 5.4. Chế độ kiểm tra (Check Mode)
Đây là chế độ **"Chạy thử" (Dry Run)**. Ansible sẽ báo cáo những gì nó *dự định* làm nhưng **không thực sự thay đổi** bất cứ thứ gì trên máy đích.
* Dùng cờ `-C` hoặc `--check`.
* *Ví dụ:* `ansible all -m copy -a "..." -C` (Chỉ hiện ra là sẽ copy, nhưng không copy thật).

-----

# 6. Playbook trong Ansible
## 6.1. Giới thiệu về Ansible Playbooks

**Ansible Playbooks** là hệ thống quản lý cấu hình và triển khai (deployment) có thể tái sử dụng, lặp lại và đơn giản. Chúng rất phù hợp để triển khai các ứng dụng phức tạp.

* **Chức năng:** Khai báo cấu hình, điều phối các quy trình thủ công trên nhiều nhóm máy theo thứ tự xác định, và chạy các tác vụ đồng bộ hoặc không đồng bộ.

* **Lợi ích:** Nên viết Playbook và lưu vào source control (như Git) để quản lý phiên bản và tái sử dụng nếu một tác vụ cần thực hiện nhiều lần.

## 6.2. Cú pháp và Cấu trúc của Playbook

* **Định dạng:** Playbook được viết bằng định dạng **YAML**.

* **Cấu trúc phân cấp:** 
  * **Playbook:** Chứa danh sách các **Plays**.
  * **Play:** Mỗi Play thực hiện một phần của mục tiêu tổng thể, nhắm vào một nhóm máy cụ thể (ví dụ: Web servers hoặc DB servers).
  * **Task (Tác vụ):** Mỗi Play chứa nhiều Task. Mỗi Task gọi một **Ansible Module** để thực thi lệnh.

## 6.3. Cơ chế hoạt động (Execution)

* **Thứ tự:** Playbook chạy tuần tự từ trên xuống dưới. Trong mỗi Play, các Task cũng chạy theo thứ tự từ trên xuống.
* **Quy trình:**
  1. Xác định các máy (managed nodes) cần nhắm tới.
  2. Thực thi ít nhất một tác vụ.


* **FQCN (Fully-Qualified Collection Name):** Từ Ansible 2.10 trở đi, nên dùng tên đầy đủ của collection (ví dụ: `ansible.builtin.yum` thay vì chỉ `yum`) để đảm bảo chọn đúng module.

* **Xử lý lỗi:** Nếu một Task bị lỗi trên một máy chủ, máy chủ đó sẽ bị loại khỏi danh sách thực thi cho các phần còn lại của Playbook.

## 6.4. Trạng thái mong muốn và Tính Idempotency

Đây là khái niệm cốt lõi của Ansible:

* **Desired State (Trạng thái mong muốn):** Hầu hết các module sẽ kiểm tra xem hệ thống đã ở trạng thái mong muốn chưa. Nếu rồi, chúng sẽ **không làm gì cả**.
* **Idempotency (Tính bất biến/lũy đẳng):** Dù chạy Playbook 1 lần hay nhiều lần, kết quả cuối cùng của hệ thống vẫn giống nhau. Điều này đảm bảo an toàn khi chạy lại Playbook.

## 6.5. Các chế độ chạy Playbook

Để chạy Playbook, sử dụng lệnh: `ansible-playbook playbook.yml`

* **Chạy thông thường:** Áp dụng thay đổi trực tiếp lên hệ thống.
* **Check Mode (Chạy kiểm tra):** Sử dụng cờ `--check` hoặc `-C`.
* Chế độ này chạy "giả lập", báo cáo các thay đổi *sẽ* diễn ra nhưng **không thực hiện thay đổi thực tế**.
* Rất hữu ích để kiểm tra an toàn trước khi chạy trên môi trường production (sản xuất).


* **Ansible-Pull:**
* Đảo ngược kiến trúc: Thay vì cần "đẩy" (push) cấu hình từ máy điều khiển xuống, các máy con sẽ tự động "kéo" (pull) cấu hình từ một kho lưu trữ trung tâm (như Git) và tự chạy.
* Giúp mở rộng quy mô (scale) dễ dàng.



## 6.6. Xác minh và Kiểm tra (Verification & Linting)

Trước khi chạy, nên kiểm tra Playbook để tránh lỗi cú pháp hoặc logic:

* **Các lệnh kiểm tra:** `ansible-playbook --syntax-check` (kiểm tra cú pháp), `--list-hosts` (liệt kê máy), `--list-tasks` (liệt kê tác vụ).

* **Ansible-Lint:** Một công cụ phân tích code (linter) cung cấp phản hồi chi tiết về các lỗi best-practice. Ví dụ: Cảnh báo việc sử dụng `state: latest` (cài bản mới nhất) thay vì một phiên bản cụ thể, điều này có thể gây rủi ro không lường trước.


## 6.7. Ví dụ Playbook đơn giản
```yaml
- name: DBServer setup
  hosts: db-server # Chỉ định nhóm máy đích
  become: yes # Sử dụng quyền sudo
  tasks:
  - name: Install MySQL
    community.mysql.mysql_db:
      name: mysql-server
      state: present # Đảm bảo MySQL được cài đặt 

  - name: Start MySQL service
    ansible.builtin.service:
      name: mysqld
      state: started # Đảm bảo dịch vụ MySQL đang chạy

- name: Backend Server setup
  hosts: be-server
  become: yes
  tasks:
  - name: Install Apache
    ansible.builtin.apt:
      name: apache2
      state: present

  - name: Start Apache service
    ansible.builtin.service:
      name: apache2
      state: started 

- name: Frontend Server setup
  hosts: fe-server
  become: yes
  tasks:
  - name: Install Nginx
    ansible.builtin.apt:
      name: nginx
      state: present
  - name: Start Nginx service
    ansible.builtin.service:
      name: nginx
      state: started
```
-----

# 7. Module trong Ansible
## 7.1. Ansible Modules là gì?
* **Định nghĩa:** Là các đoạn mã nhỏ (script) độc lập, được Ansible đẩy xuống các máy con (managed nodes) để thực thi một tác vụ cụ thể.

* **Cơ chế:** Mỗi khi bạn viết một `task` trong Playbook, bạn thực chất đang gọi một **Module**.

* **Thư viện khổng lồ:** Ansible có sẵn hàng nghìn modules để làm đủ mọi việc: từ cài đặt phần mềm, sửa file, tạo user, đến cấu hình thiết bị mạng Cisco, hay thao tác với Cloud (AWS, Azure).

* **Định dạng output chuẩn:** Mỗi module trả về kết quả dưới dạng JSON, giúp Ansible dễ dàng phân tích và xử lý.

* **Tính bất biến (Idempotency)**: Hầu hết các module được thiết kế để đảm bảo tính bất biến, nghĩa là chạy nhiều lần sẽ không gây ra thay đổi không mong muốn nếu hệ thống đã ở trạng thái mong muốn.

## 7.2. Các nhóm Module phổ biến nhất
Ansible có rất nhiều module, nhưng dưới đây là một số nhóm module thường dùng nhất:
### 7.2.1. Quản lý Gói phần mềm (Package Management)

Dùng để cài đặt, gỡ bỏ, cập nhật phần mềm.

* **`ansible.builtin.yum`** (hoặc `dnf`): Dùng cho họ RHEL/CentOS.
* **`ansible.builtin.apt`**: Dùng cho họ Ubuntu/Debian.
* **`community.general.pip`**: Cài thư viện Python.

### 7.2.2. Quản lý File và Thư mục (Files)

* **`ansible.builtin.copy`**: Copy file từ máy bạn (control node) sang máy đích.
* **`ansible.builtin.file`**: Tạo thư mục, xóa file, phân quyền (chmod, chown).
* **`ansible.builtin.template`**: Giống `copy` nhưng cho phép chèn biến động vào nội dung file (rất mạnh mẽ).
* **`ansible.builtin.lineinfile`**: Tìm và sửa/thêm một dòng cụ thể trong file cấu hình.

### 7.2.3. Quản lý Hệ thống (System)

* **`ansible.builtin.service`** (hoặc `systemd`): Bật/tắt, khởi động lại dịch vụ (như Apache, MySQL).
* **`ansible.builtin.user`**: Tạo, xóa, sửa user hệ thống.
* **`ansible.builtin.group`**: Quản lý nhóm user.

### 7.2.4. Chạy lệnh thô (Commands)

Dùng khi không có module chuyên dụng nào làm được việc bạn muốn.

* **`ansible.builtin.command`**: Chạy lệnh Linux cơ bản (an toàn hơn, không dùng được biến môi trường hay pipe `|`).
* **`ansible.builtin.shell`**: Chạy lệnh Linux đầy đủ (dùng được pipe, redirect `>`, `>>`), nhưng rủi ro bảo mật cao hơn.

> Ngoài ra, còn rất nhiều [modules](https://docs.ansible.com/projects/ansible/2.9/modules/modules_by_category.html) cho các mục đích khác như quản lý mạng, tương tác với Cloud, quản lý Docker/Kubernetes, v.v.

## 7.3. Làm sao để biết Module nào có tham số gì?

Ansible có công cụ tra cứu tích hợp sẵn ngay trên dòng lệnh, đó là `ansible-doc`.

* **Liệt kê tất cả modules:**
  ```bash
  ansible-doc -l
  ```


* **Xem hướng dẫn sử dụng module cụ thể (Ví dụ module `apt`):**
  ```bash
  ansible-doc apt
  ```

-----
# 8. Variables trong Ansible
## 8.1. Giới thiệu về Variables trong Ansible
* **Variables (Biến)** trong Ansible là các đại diện cho giá trị có thể thay đổi, giúp bạn tái sử dụng và quản lý cấu hình một cách linh hoạt hơn.
* **Mục đích:** Giúp tránh lặp lại giá trị cứng (hard-coded values) trong Playbook, làm cho chúng dễ bảo trì và tái sử dụng.
* **Cú pháp:** Biến được khai báo và sử dụng trong Playbook, templates, và các thành phần khác của Ansible.
* **Cách sử dụng:** Biến được tham chiếu bằng cách sử dụng dấu ngoặc nhọn đôi `{{ variable_name }}`.
## 8.2. Các loại Variables trong Ansible
Ansible hỗ trợ nhiều loại biến khác nhau, mỗi loại có mục đích và phạm vi sử dụng riêng:
### 8.2.1. Playbook Variables
* Được khai báo trực tiếp trong Playbook dưới mục `vars:`.
* Phạm vi: Chỉ trong Play đó.
* Ví dụ:
```yaml
- name: Example Playbook
  hosts: all
  vars:
    app_port: 8080
  tasks:
    - name: Print app port
      ansible.builtin.debug:
        msg: "Application will run on port {{ app_port }}"
```
### 8.2.2. Inventory Variables
* Gán biến cho từng host hoặc nhóm trong file inventory.
* Phạm vi: Trong host hoặc nhóm đó.
* Ví dụ (INI):
```ini
[webservers]
web1 ansible_host=192.168.1.10 ansible_user=ubuntu app_port=80
```
### 8.2.3. Host and Group Variables
* Lưu trữ biến trong thư mục `host_vars/` và `group_vars/`.
* Phạm vi: Tương ứng với từng host hoặc nhóm.
* Ví dụ:
```host_vars/web1.yml
app_port: 80
```
### 8.2.4. Registered Variables
* Lưu trữ kết quả của một task để sử dụng trong các task sau.
* Phạm vi: Trong Playbook hoặc Role đó.
* Ví dụ:
```yaml
- name: Get disk usage
  ansible.builtin.command: df -h
  register: disk_usage  
- name: Print disk usage
  ansible.builtin.debug:
    var: disk_usage.stdout
```
### 8.2.5. Facts
* Thông tin thu thập tự động từ các máy đích khi sử dụng module `setup`.
* Phạm vi: Trong toàn bộ Playbook.
* Ví dụ:
```yaml
- name: Print OS information
  ansible.builtin.debug:
    var: ansible_facts['os_family']
```
## 8.3. Thứ tự ưu tiên của Variables (Variable Precedence)
Khi có nhiều biến cùng tên từ các nguồn khác nhau, Ansible sẽ áp dụng một thứ tự ưu tiên để xác định biến nào sẽ được sử dụng. Thứ tự ưu tiên từ thấp đến cao như sau:
1. **Default variables** (biến mặc định trong module).
2. **Inventory variables** (biến trong file inventory).
3. **Host and Group variables** (biến trong `host_vars/` và `group_vars/`).
4. **Playbook variables** (biến trong Playbook).
5. **Registered variables** (biến đã đăng ký từ task trước).
6. **Extra variables** (biến truyền từ dòng lệnh với `-e`).
* Biến có độ ưu tiên cao hơn sẽ ghi đè biến có độ ưu tiên thấp hơn.
## 8.4. Ví dụ sử dụng Variables trong Ansible
```yaml
- name: DBServer setup
  hosts: db-server
  become: yes
  
  vars:
    db_user: "admin"
    db_password: "securepassword"
    db_name: "mydatabase"
    mysql_service: mysql 
  tasks:
  - name: Install MySQL Server
    ansible.builtin.apt:
      name: mysql-server
      state: present

  - name: Install Python MySQL library
    ansible.builtin.apt:
      name: python3-pymysql
      state: present

  - name: Start MySQL service
    ansible.builtin.service:
      name: "{{ mysql_service }}"
      state: started
      enabled: yes

  - name: Create database
    community.mysql.mysql_db:
      name: "{{ db_name }}"
      state: present
      login_unix_socket: /var/run/mysqld/mysqld.sock # Giúp login quyền root không cần pass

  - name: Create user for database
    community.mysql.mysql_user:
      name: "{{ db_user }}"
      password: "{{ db_password }}"
      priv: "{{ db_name }}.*:ALL" 
      state: present
      login_unix_socket: /var/run/mysqld/mysqld.sock
```
-----
# 9. Một số lệnh thường dùng trong Ansible Playbook
## 9.1. Decision making (Điều kiện)
* **`when`**: Chạy task chỉ khi điều kiện đúng.
  ```yaml
  - name: Check HTTP status
    ansible.builtin.uri:
      url: "http://{{ backend_server }}:{{ backend_port }}/health"
      return_content: yes
      status_code: 200 
    register: result # Lưu kết quả vào biến 'result'
    ignore_errors: yes # Quan trọng: Để nếu lỗi (404/500) thì playbook không dừng ngay

  - name: Notify if Backend is Healthy
    ansible.builtin.debug:
      msg: "Backend is running perfectly!"
    when: result.status == 200
  ```
* **`failed_when`**: Báo lỗi nếu điều kiện đúng.
  ```yaml
  - name: Ensure backend is healthy
    ansible.builtin.uri:
      url: "http://{{ backend_server }}:{{ backend_port }}/health"
      return_content: yes
      status_code: 200 
    register: result
    failed_when: result.status != 200 # Báo lỗi nếu status không phải 200
  ```
## 9.2. Debug & fail
* **`fail`**: Báo lỗi và dừng playbook ngay lập tức.
  ```yaml
  - name: Ensure backend is healthy
    ansible.builtin.uri:
      url: "http://{{ backend_server }}:{{ backend_port }}/health"
      return_content: yes
      status_code: 200 
    register: result

  - name: Fail if Backend is Unhealthy
    ansible.builtin.fail:
      msg: "Backend server is down!"
    when: result.status != 200
  ```
* **`debug`**: In thông tin ra màn hình để kiểm tra.
  ```yaml
  - name: Print backend status
    ansible.builtin.debug:
      msg: "Backend status is {{ result.status }}"
  ```
## 9.3. Handlers (Xử lý sự kiện)
* **`handlers`**: Chạy khi được "gọi" bởi một task (thường dùng để restart dịch vụ).
  ```yaml
  handlers:
    - name: restart apache
      ansible.builtin.service:
        name: httpd
        state: restarted  
  ```
* **Gọi handler trong task:**
  ```yaml  - name: Update Apache config
    ansible.builtin.template:
      src: apache.conf.j2
      dest: /etc/httpd/conf/httpd.conf
    notify: restart apache
  ```
## 9.3. Loops (Vòng lặp)
* **`loop`**: Lặp qua danh sách các mục.
  ```yaml
  - name: Install MySQL Server and MySQL library
    ansible.builtin.apt:
      name: {{ item }}
      loop:
        - mysql-server
        - python3-mysql.connector
      state: present
      update_cache: yes # Cập nhật cache trước khi cài đặt

  ```

## 9.4. Includes và Imports (Chia nhỏ Playbook)
* **`include_tasks`**: Chèn các task từ file khác tại thời điểm chạy  
  ```yaml
  - name: Include additional tasks
    include_tasks: additional_tasks.yml
  ```
* **`import_tasks`**: Chèn các task từ file khác tại thời điểm phân tích Playbook  
  ```yaml
  - name: Import additional tasks 
    import_tasks: additional_tasks.yml
  ```
## 9.5. Tags (Gắn thẻ)
* **`tags`**: Gắn thẻ cho task hoặc play để chạy một phần cụ thể.
  ```yaml
  - name: Restore Database
  hosts: db-server
  become: yes
  gather_facts: yes # Lấy ngày giờ hệ thống

  vars:
    restore_file: "{{ restore_file }}"
    backup_dir: /backups
    db_name: "mydatabase"
    db_user: "admin"
    db_password: "securepassword"

  tasks:
  - name: Backup MySQL database
    tags: backup
    community.mysql.mysql_db:
      name: "{{ db_name }}"
      state: backup
      target: "{{ backup_dir }}/{{ ansible_date_time.date }}_{{ ansible_date_time.time }}.sql"
      login_user: "{{ db_user }}"
      login_password: "{{ db_password }}"

  - name: Restore MySQL database
    tags: restore
    community.mysql.mysql_db:
      name: "{{ db_name }}"
      state: import
      target: "{{ restore_file }}"
      login_user: "{{ db_user }}"
      login_password: "{{ db_password }}"
  ```
* **Chạy với tag cụ thể:**
  ```bash
  ansible-playbook backup.yml --tags "backup"
  ```
-----
# 10. Roles trong Ansible
## 10.1. Khái niệm và Mục đích

Roles (Vai trò) cho phép bạn tự động tải các biến, tệp, tác vụ (tasks), trình xử lý (handlers) và các thành phần khác của Ansible dựa trên một cấu trúc tệp đã biết. Sau khi nhóm nội dung vào các roles, bạn có thể dễ dàng tái sử dụng và chia sẻ chúng với người dùng khác.

## 10.2. Cấu trúc thư mục của Role

Một Ansible role có cấu trúc thư mục tiêu chuẩn với 7 thư mục chính. Bạn phải bao gồm ít nhất một trong số này, nhưng có thể bỏ qua những thư mục không dùng đến.

Các thư mục và tệp tin quan trọng bao gồm:

* **`tasks/main.yml`**: Danh sách các tác vụ mà role sẽ thực thi.
* **`handlers/main.yml`**: Các handlers được import để role hoặc playbook sử dụng.
* **`vars/main.yml`**: Các biến có độ ưu tiên cao dành cho role.
* **`defaults/main.yml`**: Các biến có độ ưu tiên rất thấp (có thể bị ghi đè dễ dàng).
* **`files/`**: Chứa các tệp tĩnh (dùng cho module `copy`, `script`).
* **`templates/`**: Chứa các mẫu template `.j2`.
* **`meta/main.yml`**: Chứa metadata của role, bao gồm các role phụ thuộc (dependencies).
* **`library/`** và **`module_utils/`**: Chứa các custom module hoặc plugin (đối với role độc lập - standalone roles).

## 10.3. Cách sử dụng Roles

Ansible tìm kiếm roles ở các vị trí mặc định như thư mục `roles/` nằm cùng cấp với playbook, đường dẫn cấu hình `roles_path`, hoặc thư mục hiện tại.

Có 3 cách chính để sử dụng roles:

### 10.3.1. Ở cấp độ Play (Play level)
Đây là cách cổ điển, sử dụng tùy chọn `roles:` trong playbook.

* Role được xử lý như một import tĩnh (static import) trong quá trình phân tích playbook.
* Ví dụ:
```yaml
- hosts: webservers
  roles:
    - common
    - webservers

```
### 10.3.2. Ở cấp độ Task: Tái sử dụng động (`include_role`)

* Dùng `include_role` trong phần `tasks`.
* Role được chạy theo thứ tự định nghĩa, cho phép sử dụng linh hoạt các biến và điều kiện.
* Nếu gán `tag` cho `include_role`, tag đó chỉ áp dụng cho lệnh include, không áp dụng cho tất cả task bên trong trừ khi chúng cũng có tag đó.

### 10.3.3. Ở cấp độ Task: Tái sử dụng tĩnh (`import_role`)

* Dùng `import_role` trong phần `tasks`.
* Hành vi tương tự như dùng từ khóa `roles:` ở cấp độ Play.
* Nếu gán `tag` cho `import_role`, tag đó sẽ áp dụng cho **tất cả** các task bên trong role.

### 10.4. Thứ tự thực thi (Execution Order)

Khi sử dụng tùy chọn `roles` ở cấp độ Play, Ansible thực thi theo thứ tự sau:

1. **`pre_tasks`** được định nghĩa trong play.
2. Các handlers được kích hoạt bởi pre_tasks.
3. **Các Role** được liệt kê trong `roles:`, bao gồm cả các role phụ thuộc (dependencies).
4. **`tasks`** được định nghĩa trong play.
5. Các handlers được kích hoạt bởi role hoặc task.
6. **`post_tasks`** được định nghĩa trong play.
7. Các handlers được kích hoạt bởi post_tasks.

## 10.5. Kiểm tra tham số đầu vào (Argument Validation)

Bạn có thể xác định thông số kỹ thuật (specification) cho các tham số của role trong tệp `meta/argument_specs.yml`.

* Việc này giúp xác thực các tham số được cung cấp cho role.
* Nếu tham số không hợp lệ, role sẽ báo lỗi và ngừng chạy.
* Các thông số bao gồm: loại dữ liệu (`type`), bắt buộc (`required`), mặc định (`default`), mô tả (`description`), v.v..

## 10.6. Chạy Role nhiều lần và Trùng lặp

Theo mặc định, Ansible chỉ thực thi mỗi role **một lần** trong một play, ngay cả khi bạn liệt kê nó nhiều lần.

Để chạy một role nhiều lần, bạn có 2 cách:

1. **Truyền tham số khác nhau:** Nếu định nghĩa role với các tham số khác nhau, Ansible sẽ chạy role đó nhiều lần.
2. **Sử dụng `allow_duplicates: true`:** Cấu hình trong tệp `meta/main.yml` của role để cho phép chạy lặp lại dù tham số giống nhau.

## 10.7. Role phụ thuộc (Dependencies)

Role phụ thuộc cho phép tự động kéo các role khác vào khi sử dụng một role chính.

* Được định nghĩa trong `meta/main.yml`.
* Ansible sẽ chạy các role phụ thuộc **trước** role chính.
* Role phụ thuộc cũng tuân theo quy tắc chống trùng lặp (chỉ chạy một lần trừ khi tham số khác nhau hoặc được cho phép trùng lặp).

## 10.8. Chia sẻ Roles

* **Ansible Galaxy:** Là trang web miễn phí để tìm kiếm, tải xuống và chia sẻ các roles do cộng đồng phát triển.
* Bạn có thể dùng lệnh `ansible-galaxy` đi kèm với Ansible để tải hoặc tạo cấu trúc role mới.
