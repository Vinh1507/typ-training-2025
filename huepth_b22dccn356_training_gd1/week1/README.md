# <a name="_80ugtdqsm3f1"></a>**Phần 1: Giới thiệu tổng quan về Git**
1. ## <a name="_22k257p7nfdu"></a>**Git là gì?**
1. ### <a name="_bl30helbvrdj"></a>**Lịch sử Git, lý do Git được tạo ra.**
- Git là 1 loại trong hệ thống quản lý phiên bản phân tán, được tạo bởi Linus Torvalds, vào năm 2005, trong bối cảnh phát triển hạt nhân Linux. Trước khi Git ra đời, cộng đồng phát triển Linux sử dụng hệ thống quản lý phiên bản thương mại BitKeeper.
- Bối cảnh lịch sử:
+ Trước năm 2005: cộng đồng phát triển hạt nhân Linux đã sử dụng BitKeeper, một hệ thống quản lý phiên bản phân tán nhưng quyền kiểm soát vẫn thuộc về công ty phát triển.
+ Xung đột giữa cộng đồng mã nguồn mở và BitKeeper: vào năm 2005, một tranh cãi giữa nhà phát triển Linux Andrew Tridgell và công ty phát triển BitKeeper đã dẫn đến việc BitKeeper rút quyền sử dụng miễn phí cho cộng đồng Linux. Điều này tạo ra một khủng hoảng vì đội ngũ phát triển hạt nhân Linux không còn công phù hợp để quản lý mã nguồn.
- Sự ra đời của Git: Để giải quyết tình huống này, Linus Torvalds quyết định phát triển một hệ thống quản lý phiên bản mới, với những tiêu chí sau:
+ Phân tán hoàn toàn: Mỗi nhà phát triển có một bản sao đầy đủ của kho mã nguồn.
+ Tốc độ cao: Xử lý nhanh chóng, ngay cả với các dự án lớn như hạt nhân Linux
+ Tính toàn vẹn: Bảo đảm mọi thay đổi và lịch sử mã nguồn được lưu trữ một cách an toàn và không thể thay đổi.
+ Mã nguồn mở: Đảm bảo rằng hệ thống quản lý mã nguồn mới sẽ không gặp phải vấn đề giống như BitKeeper.

1. ### <a name="_qbevubcso4fc"></a>**So sánh Git với các VCS khác (SVN, Mercruial)**

   ||Git|SVN|Mercurial|
   | :- | :- | :- | :- |
   |Kiến trúc|Phân tán, tính linh hoạt và khả năng phục hồi cao hơn.|Tập trung, dựa vào 1 kho lưu trữ trung tâm duy nhất|Phân tán, mỗi người dùng có bản sao đầy đủ, cung cấp tính linh hoạt và mạnh mẽ.|
   |Hiệu suất|Rất nhanh (hầu hết các thao tác là cục bộ).|Chậm hơn (hầu hết các thao tác yêu cầu kết nối mạng)|Nhanh (tương đương Git, vì các thao tác là cục bộ).|
   |Phân nhánh và hợp nhát|Cực kỳ mạnh mẽ, nhanh chóng và dễ dàng (thao tác cục bộ, khuyến khích sử dụng).|Phức tạp hơn và chậm hơn (thao tác trên máy chủ trung tâm).|Hiệu quả và đơn giản (tương tự Git).|
   |Ứng dụng|tính linh hoạt, tốc độ, khả năng mở rộng và khả năng áp dụng rộng rãi.|cần sự kiểm soát tập trung và đơn giản.|để có DVCS nhẹ với các lệnh dễ sử dụng hơn.|

   ## <a name="_h2xq07k0b9m0"></a>**2. Khác biệt giữa Git và Github/GitLab/Bitbucket**
1. Git là công cuj version control

   ||Git|Github|GitLab|Bitbucket|
   | - | - | - | - | - |
   |Kiểu|Hệ thống kiểm soát phiên bản|Dịch vụ lưu trữ kho lưu trữ Git|Nền tảng DevOps tích hợp|Dịch vụ lưu trữ kho lưu trữ Git|
   |Sử dụng chính|Kiểm soát phiên bản cục bộ|Lưu trữ kho lưu trữ từ xa, cộng tác và chia sẻ mã|Phát triển phần mềm toàn diện, CI/CD và cộng tác|Lưu trữ kho lưu trữ từ xa, tập trung vào tích hợp Atlassian|
   |Lưu trữ|Địa phương và tự lưu trữ|Chủ yếu được lưu trữ trên nền tảng đám mây (máy chủ GitHub), một số tùy chọn tự lưu trữ|Tùy chọn lưu trữ trên đám mây (GitLab.com) và tự lưu trữ; hỗ trợ các mô hình kết hợp|Tùy chọn lưu trữ trên đám mây (Cloud) và tự lưu trữ (Server/Data Center)|
   |Tích hợp CI/CD|` `Không tích hợp sẵn; yêu cầu các công cụ của bên thứ ba|GitHub Actions (công cụ CI/CD mạnh mẽ)|Đường ống CI/CD toàn diện và tự động hóa|Bitbucket Pipelines (CI/CD tích hợp), mạnh về tích hợp với Bamboo (công cụ CI/CD của Atlassian)|
   |Kiểm soát truy cập|Cơ bản thông qua Git hooks và cấu hình máy chủ|Kiểm soát truy cập chi tiết với các nhóm, quyền dựa trên vai trò và các tính năng cộng tác|Kiểm soát truy cập chi tiết, bao gồm quản lý nhóm và nhóm con, quyền chi tiết và các nhánh được bảo vệ|Kiểm soát chi tiết, đặc biệt mạnh về cấp quyền theo từng nhánh (Branch Permissions)|
   |Giấy phép||Độc quyền với một số dự án nguồn mở|Phiên bản nguồn mở (Core) và phiên bản độc quyền (Premium)|Độc quyền (thuộc sở hữu của Atlassian)|
   |Tính năng cộng đồng|Không có|Theo dõi vấn đề, thảo luận, wiki, tính năng cộng tác (phân nhánh, yêu cầu kéo)|Tương tự như GitHub với các công cụ quản lý dự án DevOps bổ sung (bảng, cột mốc)|Theo dõi vấn đề (Issue Tracking), yêu cầu kéo, tích hợp sâu với Jira để quản lý vấn đề|
   |Tích hợp|Yêu cầu các công cụ bên ngoài để có thêm chức năng|Nhiều tích hợp thông qua GitHub Marketplace|Tích hợp toàn diện trong hệ sinh thái DevOps của mình, bao gồm các công cụ và dịch vụ của bên thứ ba|Tích hợp cốt lõi và mượt mà với hệ sinh thái Atlassian (Jira, Confluence, Trello)|
   |Mô hình định giá / chi phí|Miễn phí|Miễn phí cho kho lưu trữ công cộng, gói trả phí cho kho lưu trữ riêng tư và các tính năng bổ sung|Miễn phí (Cốt lõi), các gói trả phí cho các tính năng cao cấp, có giá tự lưu trữ|Miễn phí cho các nhóm nhỏ (thường giới hạn 5 người), gói trả phí cho các đội lớn hơn và các tính năng nâng cao|

   ## <a name="_kbjrtieei437"></a>**3. Cài đặt Git và cấu hình ban đầu**
- **git config --global [**user.name**](http://user.name)**
- **git config --global user.email**
- <img width="477" height="72" alt="image" src="https://github.com/user-attachments/assets/73076940-a2ca-46ff-bdf4-7a168b70cd29" />
- **git –version**
- <img width="386" height="81" alt="image" src="https://github.com/user-attachments/assets/c668e9b9-cbc7-414e-bf5c-0956ce3d1eaf" />
- **Thiết lập SSH**

- <img width="615" height="221" alt="image" src="https://github.com/user-attachments/assets/9057caac-dd67-4218-b812-3aa9a4e2d871" />

# <a name="_2nd8t5rt1k6q"></a>**Phần 2: Làm việc với repository**
1. ## <a name="_x83k4ognrhpm"></a>**Khởi tạo và clone repository**
<img width="239" height="52" alt="image" src="https://github.com/user-attachments/assets/e24be121-9cfe-4dea-a921-cdd9fb5bc682" />

- Khởi tạo một kho lưu trữ Git hoàn toàn mới và bắt đầu theo dõi một thư mục hiện có. Lệnh này sẽ thêm một thư mục ẩn .git trong thư mục hiện có, chứa cấu trúc dữ liệu nội bộ cần thiết cho việc kiểm soát phiên bản.
- <img width="523" height="34" alt="image" src="https://github.com/user-attachments/assets/522847d3-0e08-4a0d-88b0-f817ec897c69" />

- Tạo một bản sao cục bộ của một dự án đã tồn tại từ xa. Bản sao này bao gồm tất cả các tệp, lịch sử và nhánh của dự án.
- Cấu trúc thư mục .git: thư mục .git chứa thông tin chi tiết về từng thay đổi được thực hiện đối với repository.
+ HEAD: trỏ đến commit hiện tại.
+ config: chứa thông tin cấu hình của repo.
+ objects/: lưu trữ dữ liệu (commits, blobs, trees).
+ refs/: lưu thông tin về các nhánh và tag.

2. ## <a name="_ugz03dgiz8dq"></a>**Trạng thái file trong Git**
- untracked: File mới chưa được git theo dõi
- staged: file đã được đánh dấu commit
- committed: file đã được lưu trong lịch sử commit
- modified: file đã thay đổi nhưng chưa add lại
- git status: kiểm tra các trạng thái thay đổi

<img width="603" height="418" alt="image" src="https://github.com/user-attachments/assets/47f4dd52-6da8-4a50-9da2-eb33b5bee4cd" />
<img width="645" height="208" alt="image" src="https://github.com/user-attachments/assets/c3056c66-0b10-4ffb-bc96-ae08e7ba1a5c" />

3. ## <a name="_13r5o3eo5yti"></a>**Thêm và commit thay đổi**
- git add: git add <file> / git add .
  Chuyển các thay đổi từ modified sang staged
  Chuẩn bị 1 snapshot của các thay đổi muốn đưa vào commit tiếp theo.
- git commit: git commit -m "first commit"
  Lưu trữ các thay đổi đang ở trạng thái staged vào lịch sử Git (trạng thái committed) với một message mô tả.

- <img width="610" height="527" alt="image" src="https://github.com/user-attachments/assets/d1e83315-d7a0-4a90-aae4-f884f141925c" />

# <a name="_o7g66vkqoz6b"></a>**Phần 3: Làm việc với lịch sử và phiên bản**
1. Xem lịch sử commit
- git log, git show, git blame
+ git log: Hiển thị danh sách các commit theo thứ tự thời gian.
- <img width="460" height="247" alt="image" src="https://github.com/user-attachments/assets/c32acef3-e09a-46d4-b24a-f506d4a4831d" />

+ git show <commit-id>: Hiển thị chi tiết nội dung của một commit cụ thể.
- <img width="607" height="191" alt="image" src="https://github.com/user-attachments/assets/fb7e35c5-53ad-4326-a309-5ee56f859a11" />

+ git blame <file>: Hiển thị người tạo ra (author) và commit ID của từng dòng trong một file.
<img width="614" height="100" alt="image" src="https://github.com/user-attachments/assets/eb55dcda-41b1-4942-9f54-154461d13584" />

- Giải thích commit ID (SHA-1 hash).
+ Commit ID là một chuỗi ký tự duy nhất (thường là 40 ký tự hex, mặc dù Git thường chỉ hiển thị 7-10 ký tự đầu) được tạo bằng thuật toán SHA-1 hash cho mỗi commit.
+ Đóng vai trò là địa chỉ để định danh và tham chiếu duy nhất đến một phiên bản cụ thể của toàn bộ kho lưu trữ tại thời điểm commit đó. Mọi thứ trong Git đều được tham chiếu thông qua ID này.
2. Undo / Revert / Reset thay đổi
- git checkout, git restore, git reset, git revert
+ git checkout: Dùng để chuyển nhánh hoặc hoàn tác thay đổi trong file.
+ git restore: mới sửa file nhưng chưa commit
   ++ hoàn tác file chưa commit: git restore index.html
   ++ khôi phục file về commit cụ thể git restore --source=HEAD~1 index.html
   ++hủy bỏ file đã add . git restore --staged index.html
+ git reset: dùng để di chuyển con trỏ HEAD 
  ++ git reset –soft: bỏ commit, giữ thay đổi => dùng khi muốn gộp commit hoặc sửa message
  ++ git reset –hard : xóa luôn commit và thay đổi => quay lại hẳn commit cũ
  ++ git revert: không xóa commit cũ, mà tạo 1 commit mới đảo ngược thay đổi commit đã chon;

<img width="601" height="181" alt="image" src="https://github.com/user-attachments/assets/ac6632c5-28db-4e53-8576-b5978ecb7aa1" />

- Khi nào nên dùng revert thay vì reset.
+ git reset: commit chưa push lên git => dùng để sắp xếp, gộp hoặc xóa commit cục bộ trước khi chia sẻ
+ git revert: commit đã được push => tạo ra sự an toàn, không phá hủy
3. Làm việc với .gitignore
+ Ý nghĩa, cú pháp, ví dụ thực tế (node_modules, build/, v.v.)
+ File .gitignore dùng để bỏ qua các file/thư mục không cần Git theo dõi (build, cache, log, IDE...).

  <img width="410" height="189" alt="image" src="https://github.com/user-attachments/assets/b9073606-57f8-440f-99dd-0e588dbfc5fb" />


# <a name="_mex0dy3j45cp"></a>**Phần 4: Nhánh (Branching) và hợp nhất (Merging)**
1. Branch là gì và tại sao cần branch: 
- Tạo ra các phiên bản song song của một dự án phần mềm, cho phép phát triển tính năng mới, sửa lỗi hoặc thử nghiệm mà không ảnh hưởng đến nhánh chính (main branch hoặc master branch).
- Được sử dụng để tạo ra các bản sao tạm thời của mã nguồn, giúp lập trình viên làm việc độc lập mà không gây ảnh hưởng đến sự ổn định của dự án.
2. Tạo và chuyển nhánh
- git branch, git checkout, git switch
  **git branch <branch-name>**: Tạo một nhánh mới.
  **git checkout <branch-name>**: Chuyển sang làm việc trên nhánh đã tồn tại 
  **git switch <branch-name>**: Chuyển sang làm việc trên nhánh .
  **git checkout -b <new-branch> / git switch -c <new-branch>**: Tạo và chuyển sang nhánh mới ngay lập tức.

  <img width="475" height="215" alt="image" src="https://github.com/user-attachments/assets/24b5e508-74d0-40a2-b37c-5b88be2aa485" />

3. Merge branch
- git merge, xử lý xung đột (conflict)

  Khi nhánh phụ (ví dụ feature/login) đã hoàn thành, ta cần **gộp (merge)** các thay đổi vào nhánh chính (main) 
  => git merge feature/login

- Mở file conflict, tìm các xung đột được đánh dấu và sửa
- git add . => git commit => git push

4. Chiến lược branching phổ biến
- main, develop, feature/\*, hotfix/\* (Git Flow, GitHub Flow)
- Git Flow là mô hình phổ biến trong phát triển phần mềm, giúp tổ chức branch một cách hệ thống:
+ Master Branch: Chứa mã nguồn ổn định.
+ Develop Branch: Chứa mã đang được phát triển.
+ Feature Branch: Phát triển tính năng mới.
+ Release Branch: Chuẩn bị phát hành.
+ Hotfix Branch: Sửa lỗi khẩn cấp.
- GitHub Flow đơn giản hơn, thường được sử dụng cho các dự án nhỏ hoặc phát triển liên tục (Continuous Deployment):
+ Main Branch: Chứa mã ổn định.
+ Feature Branch: Phát triển tính năng mới.
+ Pull Request (PR): Được sử dụng để đánh giá và hợp nhất mã.

# <a name="_meadwf83f92n"></a>**Phân 5: Remote repository (Github/ GitLab)**

5. Thêm remote và đẩy code
- git remote add, git push, git pull, git fetch
- Lệnh *git remote add* cho phép bạn liên kết repository trên máy tính của mình với một repository từ xa, giúp bạn dễ dàng đẩy (push) mã nguồn lên hoặc kéo (pull) dữ liệu từ remote repository.
- *git remote add origin <url>:* Thêm remote mới
- *git push origin main:* đẩy code lên remote
- *git pull origin main:* lấy code mới từ remote
- *git fetch ;* lấy code về và chưa meger
6. Làm việc nhóm: fork, clone, pull request
- Mô hình cộng tác GitHub cơ bản
+ folk: sao chép dự án của người khác về tài khoản github của mình
+ clone: repository về máy
+ pull request: gộp các nhánh vào dự án gốc
7. Giải quyết conflict khi làm việc nhóm
- Mở file conflict, tìm các xung đột được đánh dấu và sửa
- git add . => git commit => git push

# <a name="_kw3yst4y3ney"></a>**Phần 6: Công cụ và kỹ năng nâng cao**
1. Tag và versioning
- git tag:dùng để tạo, liệt kê, xem thông tin các tag trong repository => gán nhãn cho commit, dùng cho đánh dấu phiên bản

  <img width="590" height="131" alt="image" src="https://github.com/user-attachments/assets/6bbd68b1-cdf8-4696-99e2-2ee13b047ab8" />

- git describe: mô tả commit hiện tại dựa trên tag gần nhất => biết commit hiện tại cách tag gần nhất bao nhiêu bước

<img width="400" height="73" alt="image" src="https://github.com/user-attachments/assets/d8040750-baad-4e9c-aa44-b41a91e55ed8" />

2. Stash
Lưu tạm thay đổi chưa commit (git stash)

<img width="604" height="99" alt="image" src="https://github.com/user-attachments/assets/60d75b43-b36e-4fbd-8084-29c3977b35ec" />

git stash list: kiểm tra danh sách stash
git stash apply: lấy lại các thay đôit
3. Rebase và squash
- git rebase: di chuyển các commit từ nhánh này sang nhánh khác để lịch sử thẳng và sách hơn => không tạo merge commit thừa
4. Git alias & log formatting
- Alias là tạo lệnh tắt cho các câu lệnh git dài.

<img width="582" height="327" alt="image" src="https://github.com/user-attachments/assets/e08bae23-79d4-493d-ae5e-50a8578b5462" />

5. Git reflog và khôi phục commit bị mất
git reflog: khôi phục lại commit bị mất

<img width="611" height="376" alt="image" src="https://github.com/user-attachments/assets/13e4de28-1679-4274-9799-3292466a2a18" />

# <a name="_3j59tmtnhhsz"></a>**Phần 7: Thực hành dự án thực tế**






