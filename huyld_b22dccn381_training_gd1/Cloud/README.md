# Phần 1: Giới thiệu tổng quan về Git
## 1. Git là gì?
### Lịch sử Git, lý do Git được tạo ra?
**Git** là 1 hệ thống kiểm soát phiên bản phân tán (***DVCS***)  
Lý do chính là để thay thế BitKeeper (VCS cũ) và đáp ứng nhu cầu tốc độ cùng khả năng phân tán khi phát triển Linux.

Git cho phép nhiều nhà phát triển làm việc cùng lúc trên cùng một mã nguồn, lưu lại toàn bộ lịch sử thay đổi, tạo nhánh (branch) dễ dàng và hợp nhất (merge) sau khi hoàn thành tính năng. Một số tính năng nổi bật:
- Lưu trữ toàn bộ lịch sử theo dạng snapshot (nhanh, nhỏ gọn).
- Hoạt động offline: các thao tác commit, branch, diff, log đều thực hiện được mà không cần kết nối mạng.
- Hỗ trợ branching/merging mạnh mẽ và nhẹ (cheap branches).

Các khái niệm cơ bản cần nắm: repository (local vs remote), commit, branch, tag, working directory, staging area (index), object store (objects: blobs, trees, commits, tags).

### Khác biệt Git với các VCS khác (SVN, Mercurial)
|Tính năng|Git|SVN|
| :--- | :--- | :--- |
|Mô hình | Phân tán | Tập trung

Một vài điểm khác biệt chi tiết:
- Mô hình: Git là phân tán (mỗi clone là một repository đầy đủ), SVN là tập trung (có server trung tâm). Điều này giúp Git làm việc offline tốt hơn và cho phép workflow phân tán (forks, PRs).
- Branching: Branch trong Git rất nhẹ và thường dùng rộng rãi; trong SVN branching/merging tốn kém hơn.
- Hiệu suất: Git tối ưu cho các thao tác nhánh, merge và lịch sử cục bộ; SVN có thể chậm hơn khi thao tác với lịch sử lớn.
- Lưu trữ: Git lưu dưới dạng object immutable (SHA), SVN lưu theo revision số tăng dần.

## 2. Khác biệt giữa Git và GitHub/GitLab/Bitbucket
### Git là công cụ version control
- GitHub/GitLab là dịch vụ lưu trữ Git repository.

Giải thích ngắn:
- Git: là phần mềm (tool) chạy trên máy bạn để quản lý version của mã nguồn (các lệnh như git commit, git branch, git merge...).
- GitHub/GitLab/Bitbucket: là các dịch vụ hosting Git repository (remote). Họ cung cấp giao diện web, tính năng cộng tác (Pull Request/Merge Request), issue tracker, CI/CD, wiki, permission management, v.v.

Ví dụ: bạn dùng Git để commit và push, còn GitHub để mở Pull Request, review code và chạy pipeline kiểm thử.
## 3. Cài đặt Git & cấu hình ban đầu
- git config --global user.name, user.email
- Kiểm tra version, thiết lập SSH key.

Hướng dẫn nhanh (Windows):
- Cài đặt: tải Git for Windows từ https://git-scm.com/ và cài. Khi cài, bạn có thể chọn shell (Git Bash) và credential helper.
- Kiểm tra: `git --version`
- Cấu hình cơ bản:
	- `git config --global user.name "Tên của bạn"`
	- `git config --global user.email "email@domain.com"`
	- Thiết lập editor nếu muốn: `git config --global core.editor "code --wait"` (VS Code)
- Thiết lập SSH key để dùng với remote (GitHub/GitLab):
	- `ssh-keygen -t ed25519 -C "email@domain.com"` (hoặc `rsa` nếu cần)
	- Thêm public key (`~/.ssh/id_ed25519.pub`) vào GitHub/GitLab -> Settings -> SSH Keys.
	- Kiểm tra kết nối: `ssh -T git@github.com`

Credential helpers (Windows): Git for Windows tích hợp credential manager để lưu credential an toàn. Có thể bật bằng `git config --global credential.helper manager-core`.

![git config](image/git-config.png)

# Phần 2: Làm việc với repository
## 4. Khởi tạo và clone repository
- git init vs git clone
- Cấu trúc thư mục .git

Khởi tạo repo mới:
- `git init` : tạo một repository Git trống trong thư mục hiện tại. Sau đó thêm file và commit.

Clone từ remote:
- `git clone <url>` : sao chép toàn bộ repository (bao gồm lịch sử) từ remote về máy local.

Cấu trúc `.git` (một số file/ thư mục quan trọng):
- `HEAD` : tham chiếu đến branch hiện tại (thường là refs/heads/main).
- `config` : cấu hình repo local.
- `objects/` : lưu các object git (blobs, trees, commits) theo SHA.
- `refs/heads/` : chứa refs cho các branch local.
- `refs/remotes/` : chứa refs cho các remote.
- `index` : staging area (binary file).

Lưu ý: không sửa trực tiếp trong `.git` trừ khi biết rõ hậu quả.
## 5. Trạng thái file trong Git
- untracked, staged, committed, modified
- git status, git diff

Giải thích trạng thái:
- untracked: file mới chưa được Git theo dõi.
- modified: file đã thay đổi so với commit gần nhất nhưng chưa staged.
- staged (staged/indexed): thay đổi đã được thêm vào staging area (sẵn sàng commit).
- committed: thay đổi đã được ghi vào lịch sử commit.

Lệnh hay dùng:
- `git status` : kiểm tra trạng thái working directory và staging area.
- `git diff` : xem khác biệt giữa working directory và staging (hoặc dùng `git diff --staged`/`--cached` để so sánh staged vs last commit`).
- `git add <file>` : đưa thay đổi vào staging area. (`git add .` là đưa thay đổi tất cả các file vào staging area)
- `git restore --staged <file>` : bỏ staging (trả về modified nhưng chưa staged).

Tip: Dùng `git status -s` để hiển thị ngắn gọn.
## 6. Thêm và commit thay đổi
- `git add`, `git commit`
- Cách viết message commit tốt.

Quy trình cơ bản:
- `git add <file>` (hoặc `git add .`) để thêm thay đổi vào staging.
- `git commit -m "Tiêu đề ngắn"` để tạo commit từ staging area.

Commit message tốt: (sử dụng `git commit` khi đó git sẽ tự động mở trình soạn thảo văn bản để viết 1 commit dài)
- Dòng đầu: tóm tắt ngắn (<= 50 ký tự) viết bằng thì hiện tại (imperative).
- Dòng trống.
- Thân bài: mô tả chi tiết (gồm lý do, cách giải quyết, ảnh hưởng), mỗi dòng ~72 ký tự.

Ví dụ:
```
Fix login bug when username contains spaces

Trim username input before validation to avoid authentication
failures caused by leading/trailing spaces. Add unit test.
```

![How git works](image/how%20git%20work.png)

# Phần 3: Làm việc với lịch sử và phiên bản
## 7. Xem lịch sử commit
- git log, git show, git blame
- Giải thích commit ID (SHA-1 hash).

Xem lịch sử:
- `git log` : lịch sử commit. Tùy chọn: `git log --oneline --graph --decorate --all` để xem đồ thị ngắn gọn.
- `git show <commit>` : hiển thị chi tiết một commit (diff + message).
- `git blame <file>` : xem từng dòng trong file thuộc commit nào (tác giả, thời gian).

Commit ID (SHA):
- Mỗi commit có một hash (SHA-1 hoặc SHA-256 tùy phiên bản/ cấu hình) dùng để nhận diện duy nhất. Thường chỉ cần ghi 7–12 ký tự đầu để tham chiếu. (hash ở đây được coi là mã id của commit)
- Ví dụ: `a1b2c3d` có thể dùng thay cho full SHA.
## 8. Undo / Revert / Reset thay đổi
- git checkout, git restore, git reset, git revert
- Khi nào nên dùng revert thay vì reset.

Các lệnh và ý nghĩa:
- `git restore <file>` : hoàn nguyên (undo) những thay đổi của file trong working directory từ commit hoặc từ staged. 
- `git restore --staged <file>` : khôi phục working directory (lấy phiên bản từ staging area và ghi đè lên phiên bản trong Working Directory)
- `git reset --soft <commit>` : di chuyển HEAD về commit, giữ changes trong staging. (undo code lại về phiên bản commit gần nhất hoặc commit được chỉ đỉnh. Nhưng giữ lại toàn bộ thay đổi đã viết ở Staging Area)
- `git reset --mixed <commit>` (mặc định) : di chuyển HEAD, giữ changes trong working dir (bỏ staging).
- `git reset --hard <commit>` : di chuyển HEAD và xóa mọi thay đổi trong staging + working dir (cảnh báo: destructive).
- `git revert <commit>` : tạo một commit mới đảo ngược thay đổi của commit đã cho — an toàn khi commit đã được push lên remote.

Khi nào dùng gì:
- Nếu commit đã push và chia sẻ với người khác: dùng `git revert` để không rewrite lịch sử public.
- Nếu chỉ làm việc local và commit chưa push: có thể dùng `git reset` hoặc `git commit --amend`.
## 9. Làm việc với .gitignore
- Ý nghĩa, cú pháp, ví dụ thực tế(node_modules, build/, v.v.)

.gitignore xác định file/thư mục Git sẽ bỏ qua (không track). Một số cú pháp chính:
- `node_modules/` : bỏ thư mục.
- `*.log` : bỏ tất cả file có đuôi .log.
- `/dist` : bỏ thư mục dist ở root repo.
- `!important.txt` : phủ nhận rule (theo dõi file bị ignore trước đó).

Lưu ý: nếu file đã bị track trước khi thêm vào .gitignore, Git vẫn tiếp tục track; để dừng track cần: `git rm --cached <file>` rồi commit.

Bạn có thể tạo global gitignore cho OS của mình (ví dụ cho các file hệ thống) bằng `git config --global core.excludesfile ~/.gitignore_global`.

# Phần 4: Nhánh (Branching) & hợp nhất (Merging)
## 10. Branch là gì và tại sao cần branch

Branch (nhánh) là một tham chiếu (ref) trỏ tới một commit cụ thể. Branch cho phép phát triển tính năng tách biệt, thực hiện thử nghiệm, sửa lỗi mà không ảnh hưởng branch chính (main/master). Sau khi hoàn thành, branch được merge trở lại.

Lợi ích:
- Tách biệt công việc, dễ rollback.
- Hợp tác nhiều người trên nhiều feature cùng lúc.
- Kết hợp với CI/CD để kiểm thử tự động trên từng branch.
## 11. Tạo và chuyển nhánh
- git branch, git checkout, git switch

Các lệnh cơ bản:
- `git branch <name>` : tạo branch mới (không chuyển).
- `git checkout <name>` : chuyển sang branch (cũ).
- `git checkout -b <name>` : tạo và chuyển ngay.
- `git switch <name>` / `git switch -c <name>` : cú pháp mới thay `checkout` cho branch.

Ví dụ workflow:
- `git switch -c feature/login` tạo và chuyển sang branch feature/login.
- Sau khi hoàn thành: `git add .` -> `git commit -m "feat: add login"` -> `git push -u origin feature/login`.
## 12. Merge branch
- git merge, xử lý xung đột (conflict)

Merge cơ bản:
- `git checkout main`
- `git merge feature/login` : hợp nhất branch feature/login vào main.

Fast-forward vs True merge:
- Nếu main không có commit mới kể từ khi tạo feature, merge có thể fast-forward (chỉ di chuyển ref).
- Nếu có commit mới trên cả hai branch, Git sẽ tạo merge commit (3-way merge).

Xử lý conflict:
- Khi có conflict, Git đánh dấu file và chèn các marker `<<<<<<<`, `=======`, `>>>>>>>`.
- Mở file, chỉnh sửa để giữ nội dung mong muốn, sau đó `git add <file>` và `git commit` để hoàn tất merge.
## 13. Chiến lược branching phổ biến
- main, develop, feature/*, hotfix/* (Git Flow, GitHub Flow) - tên các nhánh

Một số chiến lược:
- Git Flow: workflow truyền thống với `develop` làm branch tích hợp cho feature, `main` chứa release; phù hợp dự án phức tạp, release định kỳ.
- GitHub Flow: đơn giản hơn, mọi feature được tạo từ `main`, PR để review, sau đó merge trực tiếp; phù hợp deploy liên tục (continuous delivery).
- Trunk-based development: giữ branch chính (trunk) rất nhỏ, feature nhỏ và tích hợp liên tục; giảm complexity cho CI/CD. (nói cách khác: merge code vào nhánh duy nhất rất thường xuyên, nhiều lần 1 ngày)

Chọn chiến lược tuỳ theo team (kích thước, release cadence, CI maturity).

# Phần 5: Remote repository (GitHub/GitLab)
## 14. Thêm remote và đẩy code
- git remote add, git push, git pull, git fetch

Các lệnh cơ bản:
- `git remote add origin git@github.com:user/repo.git` : thêm remote.
- `git push -u origin main` : push branch main và thiết lập upstream.
- `git fetch origin` : tải refs và commits từ remote nhưng không merge.
- `git pull` : tương đương `git fetch` + `git merge` (mặc định). Có thể dùng `git pull --rebase` để rebase local commits lên trên remote.

Push tags:
- `git tag -a v1.0.0 -m "release v1.0.0"`
- `git push origin v1.0.0` hoặc `git push --tags` để đẩy tất cả tag.
## 15. Làm việc nhóm: fork, clone, pull request
- Mô hình cộng tác GitHub cơ bản
## Giải quyết conflict khi làm việc nhóm
- Thực hành merge conflict thực tế

Fork & Pull Request workflow:
- Fork: tạo bản sao repo của người khác vào tài khoản của bạn.
- Clone fork về local, tạo branch, commit, push lên fork, rồi tạo Pull Request từ fork -> upstream repo (repo gốc).
- PR (Pull Request) sẽ chứa diff, reviewer có thể comment, request changes, và merge khi approve.

Giải quyết conflict (thực tế):
1. Khi PR có conflict, bạn sẽ thấy thông báo trên giao diện web.
2. Pull về local: `git fetch upstream` && `git checkout feature/branch` && `git merge upstream/main` hoặc rebase `git rebase upstream/main`.
3. Sửa conflict locally, `git add` các file đã sửa, `git commit` (nếu merge) hoặc `git rebase --continue` (nếu rebase).
4. Push lại branch: nếu dùng rebase phải dùng `git push --force-with-lease` (thận trọng).

Lưu ý: Luôn update branch của bạn trước khi mở PR để giảm khả năng conflict.


# Phần 6: Công cụ & kỹ năng nâng cao
## 17. Tag và versioning
`git tag`, `git describe`  

Người ta thường dùng git tag để đánh dấu phiên bản: `v1.0.0`, `v2.0.0`,...

Tag dùng để đánh dấu một commit quan trọng (ví dụ release). Có 2 loại tag:
- Lightweight tag: chỉ là một ref đến commit.
- Annotated tag: chứa metadata (tác giả, date, message) và được ký (gpg) nếu cần. (giống như 1 commit đầy đủ)

Ví dụ:
- `git tag v1.2.0`
- `git tag -a v1.2.0 -m "Release v1.2.0"`
- `git push origin v1.2.0` hoặc `git push --tags`.

`git describe --tags` giúp tạo tên version từ tag gần nhất và số commit kể từ tag đó. <nearest tag>-<số lượng commit kể từ tag>-g<Mã SHA viết tắt: của commit hiện tại>
## 18. Stash
- Lưu tạm thay đổi chưa commit (`git stash`)

Sử dụng khi bạn đang làm dở và cần chuyển branch nhưng chưa muốn commit:
- `git stash` : lưu các thay đổi (working dir + staged) vào stash.
- `git stash list` : xem các stash.
- `git stash apply stash@{0}` : áp dụng stash mà không xoá khỏi list.
- `git stash pop` : áp dụng rồi xoá stash đó.
- `git stash branch <branchname> stash@{0}` : tạo branch từ stash và áp dụng stash.

Lưu ý: stash không phải là thay thế cho commit — chỉ dùng cho tạm thời.
## 19. Rebase và squash
- Làm sạch lịch sử commit

Rebase:
- `git rebase <base>` : thay đổi cơ sở của branch bằng cách replay các commit.
	- Tìm điểm chung: xác định commit chung gần nhất giữa branch hiện tại và `<base>`
	- Lưu lại commit: tạm thời lưu lại tất cả commit ở nhánh hiện tại kể từ thời điểm chung đó.
	- Nhảy sang commit cuối cùng của `<base>`, áp dụng lại từng commit đã lưu theo đúng thứ tự. 
- `git rebase -i HEAD~N` : interactive rebase để squash, reword, reorder commit.

![git rebase](image/git%20rebase.png)


Squash:
- Dùng `git rebase -i` và thay `pick` thành `squash` hoặc `s` để gộp commit lại thành một commit duy nhất.

Cảnh báo: rebase/rewrite lịch sử chỉ an toàn trên branch local hoặc branch không được người khác dùng; tránh rewrite lịch sử public đã push.
## 20 Git alias & log formatting
- Tùy chỉnh lệnh git cho tiện

Một vài alias hữu ích:
- `git config --global alias.co checkout`
- `git config --global alias.br branch`
- `git config --global alias.ci commit`
- `git config --global alias.st status`

Log formatting ví dụ:
- `git log --oneline --graph --decorate --all` : xem tóm tắt với đồ thị.
- Tạo alias cho log: `git config --global alias.lg "log --oneline --graph --decorate --all"`.
## 21. Git reflog và khôi phục commit bị mất

`git reflog` lưu lại lịch sử di chuyển HEAD (local). Nếu vô tình `reset --hard` hoặc xóa branch, bạn có thể tìm commit cũ qua reflog và khôi phục:
- `git reflog` -> lấy SHA của trạng thái mong muốn -> `git checkout -b recover <sha>` hoặc `git reset --hard <sha>`.

Ví dụ phục hồi branch bị xóa:
- `git reflog` tìm commit HEAD trước khi xóa.
- `git branch recovered <sha>` để tạo branch mới từ commit đó.

---------------------------------

Tài nguyên tham khảo thêm:
- Pro Git (sách miễn phí): https://git-scm.com/book/vi/v2
- Git documentation: https://git-scm.com/docs



