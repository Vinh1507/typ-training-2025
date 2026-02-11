# Module 1: the core architectural components of Azure
## 1. What is Microsoft Azure?
**Azure** là **một tập hợp các các dịch vụ điện toán đám mây** giúp bạn tự do **xây dụng, quản lý và triển khai** các ứng dụng lên Internet.

Azure cung cấp **khả năng sáng tạo không giới hạn** với các công nghệ mới giúp:
- **Biến ý tưởng thành hiện thực**
- **Thống nhất các dịch vụ**
- **Đảm bảo an toàn và tuân thủ**

---

## 2. Getting started with Azure account
<div style="text-align: center;">
    <img src="https://learn.microsoft.com/en-us/training/wwl-azure/describe-core-architectural-components-of-azure/media/account-scope-levels-9ceb3abd-a2d45a13.png" alt="Azure Portal" width="600"/>
</div>

Để tạo dịch vụ trên Azure, bạn cần có **Azure subscriptions**. Mỗi tài khoản Azure có thể có **nhiều subscriptions khác nhau** để quản lý các dịch vụ và tài nguyên một cách hiệu quả.

Nếu bạn chưa có tài khoản, có 2 hướng giúp **tạo tài khoản Azure miễn phí**:
- **Tạo tài khoản cá nhân**: Truy cập [Azure Free Account](https://azure.microsoft.com/en-us/free/) và làm theo hướng dẫn để đăng ký tài khoản miễn phí với $200 tín dụng sử dụng trong 30 ngày.
- **Tạo tài khoản sinh viên**: Truy cập [Azure for Students](https://azure.microsoft.com/en-us/free/students/) để đăng ký tài khoản sinh viên miễn phí mà không cần thẻ tín dụng với $100 tín dụng sử dụng trong 12 tháng.

---

## 3. Physical infrastructure
### 3.1. Data Centers
- **Data Centers** là hạ tầng vật lý của Azure, bao gồm các máy chủ, lưu trữ và thiết bị mạng được đặt trong các trung tâm dữ liệu trên toàn cầu.

- Có nhiều datacenter của Azure được đặt tại các vị trí chiến lược để đảm bảo hiệu suất và độ tin cậy cao.

### 3.2. Regions
- **Regions** là các khu vực địa lý nơi đặt các datacenter của Azure. Mỗi region bao gồm một hoặc nhiều datacenter gần nhau để cung cấp dịch vụ với độ trễ thấp và tính sẵn sàng cao.

- Khi triển khai dịch vụ trên Azure, bạn có thể chọn region phù hợp với nhu cầu về hiệu suất, tuân thủ và chi phí.
> **Note:**
>> Một số **dịch vụ hoặc máy áo** chỉ khả dụng ở một số region nhất định, vì vậy hãy kiểm tra trước khi triển khai.
>
>> Có một số dịch vụ toàn cầu không bị ràng buộc bởi region cụ thể, ví dụ như Microsoft Entra ID, Azure Traffic Manager hay Azure DNS.

### 3.3. Availability Zones
<div style="text-align: center;">
    <img src="https://learn.microsoft.com/en-us/training/wwl-azure/describe-core-architectural-components-of-azure/media/availability-zones-c22f95a3-14cd8677.png" alt="Availability Zones" width="600"/>
</div>

- **Availability Zones** là các khu vực vật lý riêng biệt trong một region, được thiết kế để cung cấp khả năng chịu lỗi và tính sẵn sàng cao cho các dịch vụ và ứng dụng.

> **Important:** 
>> Để đảm bảo khả năng chịu lỗi, thường có **tối thiểu 3 Availability Zones** trong mỗi region. Tuy nhiên **không phải** tất cả các region đều có Availability Zones.

#### Các dùng của Availability Zones:
- Để đảm bảo dịch vụ và dữ liệu luôn sẵn sàng ngay cả khi một zone gặp sự cố, Azure hỗ trợ triển khai dịch vụ và dữ liệu trên **nhiều Availability Zones** khác nhau trong cùng một region.
- Azure hỗ trợ availability zones với 3 tiêu chí:
    - **Zonal services**: Dịch vụ được triển khai và gắn với một zone cụ thể.
    - **Zone-redundant services**: Dịch vụ được tự động phân phối và sao lưu trên nhiều zone để đảm bảo tính sẵn sàng cao.
    - **Non-regional services**: Dịch vụ không bị ràng buộc bởi một region cụ thể và có thể hoạt động trên toàn cầu.
### 3.4. Regional Pairs
- **Regional Pairs** là các cặp region được Azure thiết kế để cung cấp khả năng phục hồi thảm họa và bảo vệ dữ liệu. Mỗi region trong một cặp được đặt cách nhau ít nhất 300 dặm để giảm thiểu rủi ro từ các sự cố địa phương.

<div style="text-align: center;">
    <img src="https://learn.microsoft.com/en-us/training/wwl-azure/describe-core-architectural-components-of-azure/media/region-pairs-7c495a33-85c0fa20.png" alt="Regional Pairs" width="600"/>
</div>

#### Lợi ích của Regional Pairs:
- **Khôi phục thảm họa**: Trong trường hợp một region gặp sự cố nghiêm trọng, dịch vụ và dữ liệu có thể được khôi phục từ region cặp.
- **Bảo vệ dữ liệu**: Azure tự động sao lưu dữ liệu giữa các region trong một cặp để đảm bảo tính toàn vẹn và sẵn sàng của dữ liệu.
- **Cập nhật bảo trì**: Azure thường xuyên thực hiện các bản cập nhật bảo trì trên một region trong cặp, giúp giảm thiểu thời gian gián đoạn dịch vụ.

> **Important:**
>> Không phải tất cả các dịch vụ đều hỗ trợ tự động sao lưu giữa các region trong một cặp. Hãy kiểm tra và cấu hình sao lưu phù hợp cho từng dịch vụ cụ thể.
>
>> Hầu hết các cặp regions là 2 chiều (tức là cả hai region đều có thể phục hồi lẫn nhau), nhưng có một số cặp là 1 chiều (chỉ một region có thể phục hồi từ region kia). Hãy kiểm tra tài liệu Azure để biết chi tiết về từng cặp region.

### 3.5. Sovereign Regions
**Sovereign Regions** là các region đặc biệt được thiết kế để đáp ứng các yêu cầu về tuân thủ và bảo mật dữ liệu của các quốc gia hoặc khu vực cụ thể, tách biệt hoàn toàn với hạ tầng Azure toàn cầu.

Ví dụ: 
- US DoD Central, US Gov Virginia dành cho các cơ quan chính phủ Hoa Kỳ.
- China East, China North dành cho thị trường Trung Quốc.

---

## 4. Management infrastructure
### 4.1. Azure resources and resource groups
<div style="text-align: center;">
    <img src="https://learn.microsoft.com/en-us/training/wwl-azure/describe-core-architectural-components-of-azure/media/resource-group-eb2d7177-ff67d816.png" alt="Resource Groups" width="600"/>
</div>

- **Azure resources** là các dịch vụ và thành phần mà bạn tạo và quản lý trên Azure, chẳng hạn như máy ảo, cơ sở dữ liệu, mạng ảo, v.v.
- **Resource groups** là các nhóm logic dùng để tổ chức và quản lý các tài nguyên Azure. Mỗi resource group có thể chứa nhiều tài nguyên khác nhau và giúp bạn dễ dàng quản lý, giám sát và kiểm soát truy cập cho các tài nguyên đó.
#### Lợi ích của Resource Groups:
- **Quản lý dễ dàng**: Giúp bạn tổ chức và quản lý các tài nguyên liên quan đến nhau trong cùng một nhóm.
- **Kiểm soát truy cập**: Cho phép bạn áp dụng các chính sách bảo mật và quyền truy cập cho toàn bộ nhóm tài nguyên.
- **Giám sát và báo cáo**: Giúp bạn theo dõi hiệu suất và chi phí của các tài nguyên trong nhóm một cách hiệu quả.
> **Note:**
>> Một tài nguyên chỉ có thể thuộc về một resource group tại một thời điểm. Bạn có thể di chuyển tài nguyên giữa các resource group khác nhau nếu cần thiết.

### 4.2. Azure subscriptions
<div style="text-align: center;">
    <img src="https://learn.microsoft.com/en-us/training/wwl-azure/describe-core-architectural-components-of-azure/media/subscriptions-d415577b-04961c4b.png" alt="Azure Subscriptions" width="600"/>
</div>

- **Azure subscriptions** là các gói dịch vụ mà bạn đăng ký để sử dụng các dịch vụ và tài nguyên trên Azure. Mỗi subscription có thể chứa nhiều resource groups và tài nguyên khác nhau.
- Azure subscriptions cung cấp khả năng **xác thực và ủy quyền**, giúp bạn **kiểm soát ai có thể truy cập và quản lý các tài nguyên** trong subscription đó.
- Một tài khoản Azure có thể **có nhiều subscriptions khác nhau** để phục vụ các mục đích khác nhau với chi phí và quản lý riêng biệt.
- Có 2 kiểu subscription phổ biến:
    - **Billing boundary**: Subscription được sử dụng để quản lý chi phí và thanh toán cho các dịch vụ Azure.
    - **Access control boundary**: Subscription được sử dụng để kiểm soát quyền truy cập và quản lý các tài nguyên Azure.
- Khi tạo Azure subcription, bạn cần lựa chọn:
    - **Environment**: Chọn môi trường phù hợp như Production, Development, Testing, v.v.
    - **Organizational structure**: Xác định cấu trúc tổ chức để quản lý subscription hiệu quả.
    - **Billing structure**: Thiết lập cấu trúc thanh toán phù hợp với nhu cầu sử dụng dịch vụ.
- **Lợi ích của Azure subscriptions**:
    - **Quản lý chi phí**: Giúp bạn theo dõi và kiểm soát chi phí sử dụng dịch vụ Azure.
    - **Phân quyền truy cập**: Cho phép bạn kiểm soát ai có thể truy cập và quản lý các tài nguyên trong subscription.
    - **Tách biệt môi trường**: Giúp bạn tách biệt các môi trường khác nhau như phát triển, thử nghiệm và sản xuất để đảm bảo an toàn và hiệu quả.

### 4.3. Management groups
- **Management groups** là các nhóm logic dùng để tổ chức và quản lý nhiều Azure subscriptions trong một tổ chức. Chúng giúp bạn áp dụng các chính sách và kiểm soát truy cập trên toàn bộ subscriptions một cách hiệu quả.
- Mỗi tổ chức có thể có một **cây phân cấp management groups** để tổ chức các subscriptions theo cấu trúc tổ chức của mình.
- **Lợi ích của Management Groups**:
    - **Quản lý tập trung**: Giúp bạn tổ chức và quản lý nhiều subscriptions trong một cấu trúc hợp lý.
    - **Áp dụng chính sách**: Cho phép bạn áp dụng các chính sách bảo mật và quyền truy cập trên toàn bộ subscriptions trong một management group.
    - **Giám sát và báo cáo**: Giúp bạn theo dõi hiệu suất và chi phí của các subscriptions trong nhóm một cách hiệu quả.
<div style="text-align: center;">
    <img src="https://learn.microsoft.com/en-us/training/wwl-azure/describe-core-architectural-components-of-azure/media/management-groups-subscriptions-dfd5a108-60f31f5a.png" alt="Management Groups" width="600"/>
</div>  

> **Note:**
>> Mỗi tổ chức có thể có tối đa 10,000 management groups và mỗi management group có thể chứa tối đa 10,000 subscriptions.
>> Mỗi subscription chỉ có thể thuộc về một management group tại một thời điểm, nhưng bạn có thể di chuyển subscription giữa các management groups khác nhau nếu cần thiết.

---

# Module 2: Computing and networking services in Azure
## 1. Virtual Machines
