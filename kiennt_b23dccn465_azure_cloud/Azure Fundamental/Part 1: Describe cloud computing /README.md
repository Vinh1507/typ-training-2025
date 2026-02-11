# [Module 1: Describe cloud computing](https://learn.microsoft.com/en-us/training/modules/describe-cloud-compute/)
## 1. Cloud computing 
**Cloud computing** là việc cung cấp các dịch vụ điện toán **qua Internet**. Bao gồm:
* Hạ tầng IT truyền thống: **VM, storage, database, networking**
* Dịch vụ mở rộng hiện đại: **IoT, Machine Learning (ML), AI**

Vì cloud hoạt động qua Internet nên:

* Không bị giới hạn bởi hạ tầng vật lý như datacenter truyền thống.
* Khi cần mở rộng nhanh → **không cần xây thêm datacenter**
* Có thể **mở rộng tài nguyên cực nhanh** ngay trên cloud.

---
## 2. Mô hình chia sẻ trách nhiệm của Azure

Đây là mô hình mà **trách nhiệm được chia giữa Cloud Provider và người dùng (consumer)**, thay vì người dùng phải tự chịu hết như datacenter truyền thống.

**On-premises (datacenter truyền thống)**, công ty phải chịu **100% trách nhiệm**, gồm:

* bảo mật vật lý
* điện, làm mát
* máy chủ, mạng
* hệ điều hành, phần mềm
* patch/update
* dữ liệu

**Trong Cloud**, trách nhiệm được chia:

* **Cloud Provider luôn chịu trách nhiệm:**
    * Datacenter vật lý
    * Mạng vật lý
    * Máy chủ vật lý (hosts)
    * Điện, làm mát, kết nối mạng
* **Người dùng luôn chịu trách nhiệm:**
    * Dữ liệu và thông tin lưu trên cloud
    * Thiết bị được phép truy cập (PC, điện thoại…)
    * Tài khoản, danh tính và quyền truy cập (accounts/identities)

**Trách nghiệm thay đổi tùy theo mô hình dịch vụ:**
* **IaaS:** Người dùng chịu nhiều nhất (OS, app, patch…)
* **PaaS:** Chia đều hơn (provider lo nền tảng, bạn lo dữ liệu/app)
* **SaaS:** Provider chịu nhiều nhất (bạn chủ yếu quản lý dữ liệu và quyền truy cập)

<div style="text-align: center;">
    <img src="https://learn.microsoft.com/en-us/training/wwl-azure/describe-cloud-compute/media/shared-responsibility-b3829bfe.svg" alt="Shared Responsibility Model" style="max-width: 600px;">
</div>

---

## 3. Cloud models
### 3.1. Private Cloud
* Cloud dùng cho **một tổ chức duy nhất**
* **Kiểm soát cao** (tài nguyên + bảo mật)
* Nhưng **chi phí cao hơn** và ít lợi ích hơn public cloud
* Có thể đặt **on-premises** hoặc datacenter riêng bên ngoài
### 3.2. Public Cloud
* Do **nhà cung cấp (Azure, AWS, Google...)** xây dựng và quản lý
* **Ai cũng có thể thuê dùng**
* Ưu điểm: nhanh, linh hoạt, trả tiền theo mức dùng
### 3.3. Hybrid Cloud
* Kết hợp **public + private** và liên kết với nhau
* Dùng để:
  * mở rộng tạm thời khi tải tăng (surge)
  * tăng bảo mật (dữ liệu quan trọng để private)
### 3.4. Multi-cloud (mở rộng thêm)
* Dùng **nhiều public cloud provider** cùng lúc
* Ví dụ: vừa dùng Azure vừa dùng AWS
### 3.5. Azure Arc
* Giúp quản lý tài nguyên ở:
  * Azure
  * private cloud
  * hybrid
  * multi-cloud
### 3.6. Azure VMware Solution
* Cho phép chạy workload VMware trên Azure
* Hữu ích khi muốn migrate từ VMware private cloud lên Azure
---
## 4. The consumption-based model
Cloud hoạt động theo mô hình **pay-as-you-go** (dùng bao nhiêu trả bấy nhiêu), thuộc loại chi phí **OpEx**: 
* **CapEx**
    * Chi phí đầu tư ban đầu (mua hẳn)
    * Ví dụ: xây datacenter, mua server, mua xe công ty…
* **OpEx**
    * Chi phí vận hành theo thời gian (thuê/dùng dịch vụ)
    * Ví dụ: thuê địa điểm, thuê xe, dùng cloud…
$\Rightarrow$ Cloud = **OpEx** vì bạn **trả tiền theo mức sử dụng**.

**Lợi ích của consumption-based model**
* **Không cần trả trước** (no upfront cost)
* Không cần mua và tự quản lý hạ tầng đắt tiền
* **Cần nhiều thì tăng**, cần ít thì giảm
* Ngừng dùng thì **ngừng trả tiền**
* Tránh việc:
  * **overestimate** (mua dư, lãng phí)
  * **underestimate** (thiếu tài nguyên, hiệu năng kém)

---
# [Module 2: Describe the benefits of using cloud services](https://learn.microsoft.com/en-us/training/modules/describe-benefits-use-cloud-services/)
## 1. High-availability & Scaling
### 1.1. High Availability (Tính sẵn sàng cao)
**High Availability** tập trung vào việc đảm bảo hệ thống **luôn hoạt động và truy cập được** khi cần, kể cả khi có sự cố.
* Mục tiêu: **tối đa uptime** (giảm downtime).
* Cloud (như Azure) cung cấp mức uptime theo **SLA (Service-Level Agreement)**.
* SLA là **cam kết** của nhà cung cấp về độ sẵn sàng của dịch vụ.

$\Rightarrow$ Hệ thống **ít bị gián đoạn**, đáng tin cậy hơn cho người dùng.
### 1.2. Scalability (Khả năng mở rộng)
**Scalability** là khả năng **tăng hoặc giảm tài nguyên** để đáp ứng nhu cầu sử dụng.
* Khi traffic tăng đột biến → scale lên để chịu tải.
* Khi nhu cầu giảm → scale xuống để tiết kiệm tiền.
* Vì cloud tính phí theo **mức dùng thực tế**, nên bạn không bị **trả dư**.

Có 2 loại scaling chính:
* **Vertical Scaling (Scale up / down)**
    * Tăng/giảm sức mạnh của 1 tài nguyên
    * Ví dụ: tăng CPU/RAM cho máy ảo.
* **Horizontal Scaling (Scale out / in)**
    * Tăng/giảm số lượng tài nguyên
    * Ví dụ: thêm/bớt VM hoặc containers.

---

## 2. Reliability & Predictability
### 2.1. Reliability (Độ tin cậy)

**Reliability** là khả năng hệ thống **phục hồi sau sự cố** và **tiếp tục hoạt động bình thường** vì:
* Cloud có thiết kế **phân tán toàn cầu** (decentralized).
* Tài nguyên có thể triển khai ở **nhiều vùng (regions)** trên thế giới.
* Nếu **một vùng gặp sự cố lớn**, các vùng khác vẫn chạy.
* Ứng dụng có thể được thiết kế để **tự động chuyển vùng** khi có lỗi (failover).

$\Rightarrow$ Hệ thống **ổn định hơn**, **ít downtime hơn**, **chống chịu tốt hơn**.
### 2.2. Predictability (Tính dự đoán)
**Predictability** giúp bạn triển khai và vận hành hệ thống với **sự tự tin**, vì bạn có thể dự đoán trước:
#### 2.2.1. Predictability về hiệu năng (Performance)
* Dự đoán tài nguyên cần thiết để đảm bảo trải nghiệm người dùng tốt.
* Cloud hỗ trợ các cơ chế như:
      * **Autoscaling**: tự tăng/giảm tài nguyên theo nhu cầu
  * **Load balancing**: phân phối tải để tránh nghẽn
  * **High availability**: giảm nguy cơ hệ thống bị sập

$\Rightarrow$ Hệ thống chạy **mượt hơn**, ít bị quá tải.
#### 2.2.2. Predictability về chi phí (Cost)
* Cloud cho phép:
  * theo dõi tài nguyên **real-time**
  * tối ưu sử dụng tài nguyên
  * phân tích xu hướng để dự đoán chi phí tương lai
* Có thể dùng công cụ như:
  * **TCO Calculator**
  * **Pricing Calculator**

$\Rightarrow$ Bạn **dự toán được chi phí**, tránh “vỡ ngân sách”.

--- 

## 3. Security & Governance
## 3.1. Governance (Quản trị)
Cloud hỗ trợ quản trị và tuân thủ nhờ:
* **Template / chuẩn hóa cấu hình** → đảm bảo tài nguyên triển khai đúng tiêu chuẩn công ty và quy định nhà nước.
* **Dễ cập nhật theo tiêu chuẩn mới** khi quy định thay đổi.
* **Audit (kiểm tra) trên cloud** → phát hiện tài nguyên không tuân thủ và gợi ý cách khắc phục.
* Có thể **tự động cập nhật/patch** tùy mô hình triển khai.

$\Rightarrow$ Hệ thống được quản lý tốt, luôn đúng chuẩn và dễ kiểm soát.

### 3.2. Security (Bảo mật)

Cloud giúp bảo mật tốt hơn vì:

* Bạn có thể chọn mô hình phù hợp mức độ kiểm soát:

  * **IaaS**: kiểm soát nhiều (tự quản OS, phần mềm, patch)
  * **PaaS/SaaS**: nhà cung cấp tự lo patch và bảo trì nhiều hơn
* Cloud providers thường có khả năng chống các tấn công như **DDoS** tốt hơn.

$\Rightarrow$ Hệ thống **an toàn hơn**, giảm rủi ro bảo mật.

---

## 4. Managemeablity 
Cloud có lợi thế lớn vì cung cấp nhiều cách **quản lý và vận hành hệ thống dễ dàng, linh hoạt**.

### 4.1. Management of the cloud (Quản lý tài nguyên cloud)

Cloud giúp bạn quản lý tài nguyên hiệu quả hơn nhờ:

* **Tự động scale** tài nguyên theo nhu cầu.
* **Triển khai theo template** (chuẩn sẵn), giảm cấu hình thủ công.
* **Giám sát sức khỏe hệ thống**, tự thay thế tài nguyên lỗi.
* **Cảnh báo tự động theo metric**, theo dõi hiệu năng real-time.

$\Rightarrow$ Giảm công sức vận hành, hệ thống ổn định hơn.

### 4.2. Management in the cloud (Cách quản lý cloud)

Bạn có thể quản lý cloud bằng nhiều công cụ:

* **Web portal**
* **Command line (CLI)**
* **API**
* **PowerShell**

$\Rightarrow$ Quản lý linh hoạt theo nhu cầu và kỹ năng của bạn.

---

# [Module 3: Describe cloud service types](https://learn.microsoft.com/en-us/training/modules/describe-cloud-service-types/)
## 1. IaaS (Infrastructure as a Service)
**IaaS** là mô hình cloud **linh hoạt nhất**, cho bạn **quyền kiểm soát cao nhất** đối với tài nguyên.

$\Rightarrow$ Hiểu đơn giản: bạn **thuê phần cứng** (server) trong datacenter của cloud provider.

**Cloud provider chịu trách nhiệm:**

* Phần cứng (hardware)
* Kết nối mạng ra Internet
* Bảo mật vật lý (physical security)

**Bạn (người dùng) chịu trách nhiệm:**

* Cài đặt + quản lý hệ điều hành (OS)
* Cấu hình mạng
* Database, storage
* Patch/update
* Bảo mật hệ thống

$\Rightarrow$ IaaS là mô hình mà **người dùng phải chịu trách nhiệm nhiều nhất**.

**Nên dùng IaaS khi:**

* **Lift-and-shift migration**: chuyển hệ thống on-prem lên cloud gần như giữ nguyên.
* **Testing & Development**: tạo nhanh môi trường test/dev giống nhau, bật tắt linh hoạt.

---

## 2. PaaS (Platform as a Service)
**PaaS** là mô hình nằm **giữa IaaS và SaaS**.

$\Rightarrow$ Hiểu đơn giản: bạn không chỉ thuê hạ tầng, mà còn được cung cấp sẵn **nền tảng để phát triển và chạy ứng dụng**.

Trong PaaS, nhà cung cấp sẽ quản lý:

* Hạ tầng vật lý + bảo mật vật lý + kết nối Internet
* **Hệ điều hành (OS)**
* **Middleware**
* **Database**
* **Development tools**
* Các dịch vụ hỗ trợ như BI (business intelligence)

$\Rightarrow$ Bạn không phải lo licensing, patching OS và database.

Bạn chủ yếu tập trung vào:

  * code ứng dụng
  * dữ liệu
  * cấu hình cần thiết cho ứng dụng

$\Rightarrow$ PaaS là mô hình mà **trách nhiệm được chia đều hơn** giữa nhà cung cấp và người dùng.

**Nên dùng PaaS khi:**

* **Development framework**: môi trường phát triển ứng dụng nhanh, có sẵn tính năng scale, HA, multi-tenant.
* **Analytics / Business Intelligence**: dùng công cụ phân tích dữ liệu, khai thác insight và dự đoán xu hướng.

---

## 3. SaaS (Software as a Service)
**SaaS** là mô hình cloud **hoàn chỉnh nhất**.

$\Rightarrow$ Hiểu đơn giản: bạn **thuê/dùng một ứng dụng đã làm sẵn**, không cần tự xây hay tự vận hành.

Ví dụ phổ biến:
* Email
* Phần mềm tài chính
* Ứng dụng nhắn tin
* Công cụ làm việc (Office, Google Workspace…)

**Đặc điểm chính:**
* **Dễ dùng nhất**, triển khai nhanh nhất
* **Ít linh hoạt hơn** IaaS/PaaS
* Cần **ít kiến thức kỹ thuật** nhất

Bạn chịu trách nhiệm:
* Dữ liệu bạn đưa lên hệ thống
* Thiết bị được phép truy cập
* Người dùng và quyền truy cập

Cloud provider chịu trách nhiệm:
* Datacenter, điện, mạng
* Bảo mật vật lý
* Phát triển ứng dụng
* Patch và bảo trì hệ thống

$\Rightarrow$ SaaS là mô hình mà **cloud provider chịu trách nhiệm nhiều nhất**.

**Nên dùng SaaS khi:**

* Email & messaging
* Ứng dụng năng suất (productivity)
* Theo dõi tài chính / chi phí

---
