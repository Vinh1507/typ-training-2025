# CI/CD with Kubernetes
"Cảm ơn Toàn. Bạn Toàn đã giúp chúng ta tìm hiểu xong về Kubernetes - nó giống như việc chúng ta đã xây dựng xong một nhà bếp công nghiệp hiện đại với đầy đủ bếp lò, dụng cụ và hệ thống phân chia khu vực (Namespace, Node, Pod...).

Tuy nhiên, có nhà bếp xịn mà đầu bếp cứ phải chạy đi chạy lại làm mọi thứ thủ công thì món ăn ra bàn vẫn chậm. Chúng ta cần một dây chuyền chế biến tự động để đưa nguyên liệu từ kho ra đến bàn ăn một cách nhanh nhất và chuẩn xác nhất. Dây chuyền đó chính là CI/CD."

---
## 1. CI/CD overview
### Why CI/CD is important in Kubernetes-based systems
Vì K8s thường làm việc trong môi trường Microservice. Nếu coi các service là các nguyên liệu để tạo thành món ăn, người đầu bếp (developer) sẽ không thể nào tự mình nhận order, chuẩn bị nguyên liệu, chế biến và phục vụ món ăn (build, test, deploy) cho từng món ăn (service) một cách thủ công được.

Nếu thuê thêm nhân viên (devops) để làm việc này thì chi phí sẽ rất cao mà chưa chắc có thể đáp ứng kịp thời nhu cầu thay đổi món ăn (feature) của khách hàng (user) cũng như là có rủi ro sai sót trong quá trình chế biến (deploy) do con người làm.

Vì vậy, chúng ta cần một hệ thống CI/CD tự động để giúp các đầu bếp (developer) có thể nhanh chóng đưa món ăn (feature) mới ra bàn (production) một cách nhanh nhất, an toàn nhất và ít tốn kém chi phí nhất.
### CI/CD concepts
* **CI (Continuous Integration)**: Quá trình tự động hóa việc tích hợp mã nguồn từ một hoặc nhiều developer. Khi mà nhận biết được có thay đổi trong mã nguồn, hệ thống CI sẽ tự động thu thập, xây dựng (build) và kiểm thử (test) mã nguồn đó để đảm bảo rằng các thay đổi không phá vỡ hệ thống hiện tại.

    $\Rightarrow$ Nhờ có CI, các developer có thể làm việc cùng nhau một cách hiệu quả hơn, giảm thiểu xung đột mã nguồn và phát hiện lỗi sớm trong quá trình phát triển. 
* **CD (Continuous Delivery/Continuous Deployment)**: Quá trình tự động hóa việc triển khai (deploy) mã nguồn đã được kiểm thử vào môi trường sản xuất (production) hoặc các môi trường khác như staging, testing, kiểm tra (health check) xem mã nguồn có hoạt động ổn đinh và hỗ trợ rollback nếu có sự cố xảy ra.
   * **Continuous Deployment:** Sau khi mã nguồn đã qua kiểm thử, nó sẽ tự động được triển khai vào môi trường sản xuất mà không cần sự can thiệp của con người.
   * **Continuous Delivery:** Mã nguồn đã qua kiểm thử sẽ được chuẩn bị sẵn sàng để triển khai vào môi trường sản xuất, nhưng việc triển khai này vẫn cần sự xác nhận từ con người.
---
## 2. CI/CD tools for Kubernetes
### CI tools commonly used with Kubernetes
#### Jenkins
* **The power of controller:** 
    * Jenkins hoạt động với mô hình master-slave, trong đó master (controller) quản lý các job và phân phối công việc cho các slave (agent) để thực hiện. Điều này giúp tối ưu hóa việc sử dụng tài nguyên và tăng khả năng mở rộng. 
    * Pipeline as Code: Jenkins hỗ trợ việc định nghĩa pipeline dưới dạng mã (Jenkinsfile), giúp quản lý và version hóa quy trình CI/CD một cách dễ dàng.
    * Tính tùy biến cao với hàng ngàn plugin có sẵn để tích hợp với các công cụ khác nhau như Git, Docker, Kubernetes, v.v.
    * Phù hợp với các dự án lớn và phức tạp nhờ khả năng mở rộng và tùy biến cao.
* **Real-world Weaknesses:**
    * Cấu hình phức tạp: Việc thiết lập và cấu hình Jenkins có thể khá phức tạp, đặc biệt đối với người mới bắt đầu.
    * Bảo trì cao: Do tính tùy biến cao và số lượng plugin lớn, việc bảo trì Jenkins có thể trở nên khó khăn và tốn thời gian.
    * Giao diện người dùng lỗi thời: Giao diện của Jenkins có thể không thân thiện và khó sử dụng so với các công cụ CI/CD hiện đại khác.
#### GitLab CI
* **Advantage:**
    * Kiến trúc Server-Runner: GitLab CI sử dụng mô hình Server-Runner, trong đó GitLab Server quản lý các pipeline và phân phối công việc cho các Runner để thực hiện. Điều này giúp tối ưu hóa việc sử dụng tài nguyên và tăng khả năng mở rộng.
    * Pipeline as Code: GitLab CI cho phép định nghĩa pipeline dưới dạng mã (GitLab CI/CD YAML file), giúp quản lý và version hóa quy trình CI/CD một cách dễ dàng.
    * Cấu hình đơn giản: GitLab CI có giao diện người dùng thân thiện và dễ sử dụng, giúp người dùng nhanh chóng thiết lập và quản lý các pipeline.
    * Tích hợp sẵn với GitLab: GitLab CI tích hợp chặt chẽ với Gitlab, ngoài ra còn hỗ trợ tích hợp với các công cụ khác như Docker, Kubernetes, v.v.
* **Drawbacks:**
    * Phụ thuộc vào GitLab: GitLab CI hoạt động tốt nhất khi sử dụng cùng với GitLab, điều này có thể hạn chế sự linh hoạt trong việc lựa chọn công cụ quản lý mã nguồn.
    * Tính năng hạn chế: So với các công cụ CI/CD khác như Jenkins, GitLab CI có thể thiếu một số tính năng nâng cao và tùy biến.
    * Hạn chế về tài nguyên: Một số tài nguyên SaaS của GitLab CI có giới hạn về tài nguyên và thời gian chạy, điều này có thể ảnh hưởng đến hiệu suất của các pipeline lớn.
### CD approachs commonly used with Kubernetes
#### Push-based deployments
* **Concept:** Trong phương pháp triển khai dựa trên đẩy (push-based deployments), quá trình triển khai ứng dụng được kích hoạt từ bên ngoài hệ thống Kubernetes. Các công cụ CI/CD sẽ "đẩy" các thay đổi mã nguồn hoặc cấu hình mới vào cụm Kubernetes để triển khai.
* **Advantage:**
    * Đơn giản, trực quan, dễ hiểu và dễ triển khai.
    * Kiểm soát tập trung từ bên ngoài, giúp dễ dàng tích hợp với các công cụ CI/CD hiện có.
    * Triển khai nhanh chóng các thay đổi nhỏ.
* **Drawbacks:**
    * Rủi ro bảo mật: Việc mở cổng truy cập từ bên ngoài có thể tạo ra các lỗ hổng bảo mật nếu không được cấu hình đúng cách.
    * Trạng thái không đồng bộ: Mã nguồn đang chạy có thể chưa được cập nhật theo trạng thái của kho lưu trữ mã nguồn.
    * Khó ghi lại lịch sử triển khai và khó rollback nếu không có cơ chế quản lý phiên bản tốt.
    * Phụ thuộc vào công cụ CI/CD bên ngoài để quản lý quá trình triển khai.
#### Pull-based deployments
* **Concept:** Trong phương pháp triển khai dựa trên kéo (pull-based deployments), các cụm Kubernetes tự "kéo" các thay đổi mã nguồn hoặc cấu hình mới từ một kho lưu trữ trung tâm hoặc hệ thống CI/CD. Các agent hoặc controller chạy bên trong cụm Kubernetes sẽ kiểm tra định kỳ các thay đổi và tự động áp dụng chúng.
* **Advantage:**
    * Tăng cường bảo mật: Giảm thiểu rủi ro bảo mật do không cần mở cổng truy cập từ bên ngoài.
    * Đồng bộ trạng thái: Coi git như bản thiết kế duy nhất, CD tools (ArgoCD) sẽ đảm bảo trạng thái cụm K8s luôn đồng bộ với trạng thái trong kho lưu trữ mã nguồn.
    * Dễ dàng ghi lại lịch sử triển khai và hỗ trợ rollback nhờ vào việc quản lý phiên bản trong kho lưu trữ mã nguồn.
    * Tách biệt rõ ràng giữa quá trình xây dựng (build) và triển khai (deploy), giúp tăng tính linh hoạt và khả năng mở rộng.
* **Drawbacks:**
    * Phức tạp hơn trong việc thiết lập và cấu hình so với phương pháp đẩy (push-based).
    * Yêu cầu quản lý thêm các agent hoặc controller bên trong cụm Kubernetes.
    * Có thể có độ trễ trong việc áp dụng các thay đổi do quá trình kiểm tra định kỳ.
### Other tools commonly used with Kubernetes:
#### Docker:
* Docker được sử dụng trong CI/CD pipeline để đóng gói ứng dụng cùng toàn bộ môi trường chạy (code, thư viện, cấu hình) thành Docker image.
* Trong pipeline, Docker giúp build image, chạy test trong container, và đẩy image lên registry, đảm bảo ứng dụng chạy nhất quán giữa các môi trường (dev, test, staging, production).
* Việc triển khai chỉ cần pull đúng image version, giúp tăng tính tự động hóa, giảm lỗi môi trường và hỗ trợ rollback nhanh khi cần thiết.
#### Helm chart:
* Helm chart là một công cụ quản lý gói trong K8s.
* Trong CI/CD pipeline, Helm Chart được sử dụng để chuẩn hóa và tự động hóa việc triển khai ứng dụng lên Kubernetes thông qua các manifest dạng template.
* Pipeline cập nhật giá trị cấu hình (như image version), đóng gói chart và triển khai lên cluster, giúp đảm bảo tính nhất quán, dễ cấu hình và hỗ trợ rollback hiệu quả giữa các môi trường.
---
## 3. CI/CD workflow
**1. Commit & Push:**
- Developer thực hiện các thay đổi mã nguồn trên máy tính cá nhân và sử dụng hệ thống quản lý mã nguồn (như Git) để commit và push các thay đổi lên kho lưu trữu mã nguồn trung tâm (như GitHub, GitLab).

**2. CI Trigger:**
- Jenkins phát hiện thay đổi trong kho lưu trữ mã nguồn và tự động kích hoạt quá trình CI.

**3. Build & Test:**
- Jenkins thực hiện quá trình build mã nguồn thành các artifact (như Docker images) và chạy các bài kiểm thử tự động để đảm bảo mã nguồn hoạt động đúng.

**4. Tag & Push**
- Nếu quá trình build và test thành công, Jenkins sẽ gán thẻ (tag) cho phiên bản mới của mã nguồn và đẩy các artifact đã xây dựng lên registry (như Docker Hub, GitLab Container Registry).

**5. Update Manifest**
- Jenkins clone config repo chứa file value.yaml và cập nhật phiên bản image mới trong file này.
- Jenkins commit và push thay đổi này lên config repo.

**6. Detect Changes**
- ArgoCD theo dõi config repo và phát hiện thay đổi trong file value.yaml.

**7. Sync Changes**
- ArgoCD tự động đồng bộ các thay đổi từ config repo vào cụm Kubernetes, triển khai phiên bản mới của ứng dụng.

**8. Deploy**
- Ứng dụng mới được triển khai và chạy trên cụm Kubernetes.

**9. Monitor & Rollback**
- ArgoCD và các công cụ giám sát khác theo dõi trạng thái của ứng dụng. Nếu phát hiện sự cố, ArgoCD có thể tự động rollback về phiên bản trước đó để đảm bảo tính ổn định của hệ thống.
---
## 4. CI/CD strategy
## Recreate 
**Concept:** Chiến lược tái tạo (Recreate) là một phương pháp triển khai trong Kubernetes, trong đó tất cả các phiên bản hiện tại của một ứng dụng sẽ bị xóa bỏ trước khi triển khai phiên bản mới. Vì không có sự chồng chéo giữa các phiên bản, chiến lược này gây ra thời gian gián đoạn dịch vụ (downtime) trong quá trình triển khai.
## Rolling Update
**Concept:** Chiến lược cập nhật cuộn (Rolling Update) là một phương pháp triển khai trong Kubernetes, trong đó các phiên bản mới của ứng dụng được triển khai dần dần, thay thế từng phần các phiên bản cũ mà không gây ra thời gian gián đoạn dịch vụ (downtime). Điều này giúp đảm bảo rằng ứng dụng luôn sẵn sàng phục vụ người dùng trong suốt quá trình triển khai.

Một số tham số quan trọng trong chiến lược Rolling Update:
* **maxUnavailable:** Xác định số lượng hoặc tỷ lệ phần trăm của các Pod hiện tại có thể không sẵn sàng trong quá trình cập nhật. 
* **maxSurge:** Xác định số lượng hoặc tỷ lệ phần trăm của các Pod mới có thể được tạo ra vượt quá số lượng mong muốn trong quá trình cập nhật.

Ví dụ: Nếu chúng ta có một Deployment với 3 pod và thiết lập `maxUnavailable` là 1 và `maxSurge` là 2, quá trình cập nhật sẽ diễn ra như sau:
* Khi bắt đầu deploy, chỉ 2 trong số 3 pod ban đầu sẽ vẫn hoạt động (1 pod sẽ bị dừng để cập nhật).
* Đồng thời 3 pod mới sẽ bắt đầu được tạo ra (vì `maxSurge` là 2, nên có thể tạo thêm 2 pod mới vượt quá số lượng mong muốn).
* Khi lần lượt các pod mới sẵn sàng, các pod cũ sẽ tiếp tục được dừng lại cho đến khi tất cả các pod cũ được thay thế hoàn toàn bởi các pod mới.
## Rollback
Có 2 cách rollback phổ biến trong CI/CD với Kubernetes:
* **Rollback bằng ArgoCD:** ArgoCD cung cấp tính năng rollback tích hợp, cho phép người dùng dễ dàng quay trở lại phiên bản trước đó của ứng dụng nếu phát hiện sự cố sau khi triển khai. Người dùng có thể chọn phiên bản mong muốn từ giao diện ArgoCD và thực hiện rollback chỉ với vài cú nhấp chuột. Tuy nhiên, do ArgoCD liên tục theo dõi trạng thái của ứng dụng, nên sau khi rollback, ArgoCD sẽ tự động đồng bộ lại trạng thái của ứng dụng với kho lưu trữ mã nguồn, điều này khiên rollback chỉ mang tính tạm thời nếu không thay đổi lại mã nguồn trong kho lưu trữ. Nếu muốn giữ trạng thái rollback, ta cần tắt tính năng tự động đồng bộ (auto-sync) hoặc cập nhật lại mã nguồn trong kho lưu trữ để phản ánh trạng thái hiện tại của ứng dụng nhưng không phải là cách làm chuẩn.
* **Rollback bằng Git:** Đây là phương pháp chuẩn hơn, trong đó người dùng sẽ quay trở lại phiên bản trước đó của mã nguồn trong kho lưu trữ (Git) và sau đó đẩy (push) thay đổi này lên kho lưu trữ. ArgoCD sẽ phát hiện thay đổi trong kho lưu trữ và tự động đồng bộ lại trạng thái của ứng dụng trên cụm Kubernetes, triển khai phiên bản trước đó của ứng dụng. Phương pháp này đảm bảo rằng trạng thái của ứng dụng luôn đồng bộ với trạng thái trong kho lưu trữ mã nguồn.
---
"Món ăn đã lên bàn thông qua CI/CD pipeline, nhưng nếu khách chê mặn (Bug), ta không thể chỉ đứng nhìn.

Ta cần lật lại 'nhật ký bếp' để tra soát chính xác: Ai nấu? Bỏ dư gia vị gì? Vào lúc mấy giờ? Để truy vết được nguyên nhân gốc rễ đó, ta cần đến Logging. Và phần tiếp theo sẽ là về Logging trong Kubernetes do bạn Thắng thực hiện.

Còn bây giờ, chúng ta sẽ timebreak ít phút trước khi quay lại nhé!"
