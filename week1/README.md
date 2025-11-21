---
## Phần 1: Giới thiệu tổng quan về Git

1. **Git là gì?**
- Lịch sử Git, lý do Git được tạo ra.
- So sánh Git với các VCS khác (SVN, Mercurial).
2. **Khác biệt giữa Git và GitHub/GitLab/Bitbucket**
- Git là công cụ version control.
- GitHub/GitLab là dịch vụ lưu trữ Git repository.
3. **Cài đặt Git & cấu hình ban đầu**
- `git config --global user.name`, `user.email`
- Kiểm tra version, thiết lập SSH key.
---

## Phần 2: Làm việc với repository

4. **Khởi tạo và clone repository**
   - `git init` vs `git clone`
   - Cấu trúc thư mục `.git`
5. **Trạng thái file trong Git**
   - `untracked`, `staged`, `committed`, `modified`
   - `git status`, `git diff`
6. **Thêm và commit thay đổi**
   - `git add`, `git commit`
   - Cách viết message commit tốt.

---

## Phần 3: Làm việc với lịch sử và phiên bản

7. **Xem lịch sử commit**
   - `git log`, `git show`, `git blame`
   - Giải thích commit ID (SHA-1 hash).
8. **Undo / Revert / Reset thay đổi**
   - `git checkout`, `git restore`, `git reset`, `git revert`
   - Khi nào nên dùng revert thay vì reset.
9. **Làm việc với `.gitignore`**
   - Ý nghĩa, cú pháp, ví dụ thực tế (node_modules, build/, v.v.)

---

## Phần 4: Nhánh (Branching) & hợp nhất (Merging)

10. **Branch là gì và tại sao cần branch**
11. **Tạo và chuyển nhánh**
    - `git branch`, `git checkout`, `git switch`
12. **Merge branch**
    - `git merge`, xử lý xung đột (conflict)
13. **Chiến lược branching phổ biến**
    - `main`, `develop`, `feature/*`, `hotfix/*` (Git Flow, GitHub Flow)

---

## Phần 5: Remote repository (GitHub/GitLab)

14. **Thêm remote và đẩy code**
    - `git remote add`, `git push`, `git pull`, `git fetch`
15. **Làm việc nhóm: fork, clone, pull request**
    - Mô hình cộng tác GitHub cơ bản
16. **Giải quyết conflict khi làm việc nhóm**
    - Thực hành merge conflict thực tế

---

## Phần 6: Công cụ & kỹ năng nâng cao

17. **Tag và versioning**
    - `git tag`, `git describe`
18. **Stash**
    - Lưu tạm thay đổi chưa commit (`git stash`)
19. **Rebase và squash**
    - Làm sạch lịch sử commit
20. **Git alias & log formatting**
    - Tùy chỉnh lệnh git cho tiện
21. **Git reflog và khôi phục commit bị mất**

---

## Phần 7: Thực hành dự án thực tế

22. **Tình huống giả lập:**

- Merge conflict, revert code, rollback phiên bản, tạo release.
- Khi nào thì cần thực hiện việc fetch, pull, ..

---
