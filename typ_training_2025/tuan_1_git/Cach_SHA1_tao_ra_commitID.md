## Phần 3.2 : Cách SHA1 tạo ra commit ID
* Tôi đang ở trong thư mục dự án và sử dụng lệnh `git show` để hiển thị chi tiết commit gần nhất.
```shell 
ngtukien@NgTuKien:~/Documents/TYP/typ-training-2025$ git show
commit 9a371571e5c1fed307a47594c043335e92892c8b (HEAD -> main)
Author: NguyenTuKien <KienNT.B23CN465@stu.ptit.edu.vn>
Date:   Fri Oct 17 13:35:14 2025 +0700

    Update nội dung phần 3

diff --git "a/kiennt_b23dccn465_training_gd1/Tu\341\272\247n 1: Git v\303\240 Github/Ph\341\272\247n 3: L\303\240m vi\341\273\207c v\341\273\233i l\341\273\213ch s\341\273\255 v\303\240 phi\303\252n b\341\272\243n.md" "b/kiennt_b23dccn465_training_gd1/Tu\341\272\247n 1: Git v\303\240 Github/Ph\341\272\247n 3: L\303\240m vi\341\273\207c v\341\273\233i l\341\273\213ch s\341\273\255 v\303\240 phi\303\252n b\341\272\243n.md"
index 3ec1a9b..0da8cdc 100644
-- "a/kiennt_b23dccn465_training_gd1/Tu\341\272\247n 1: Git v\303\240 Github/Ph\341\272\247n 3: L\303\240m vi\341\273\207c v\341\273\233i l\341\273\213ch s\341\273\255 v\303\240 phi\303\252n b\341\272\243n.md"  
++ "b/kiennt_b23dccn465_training_gd1/Tu\341\272\247n 1: Git v\303\240 Github/Ph\341\272\247n 3: L\303\240m vi\341\273\207c v\341\273\233i l\341\273\213ch s\341\273\255 v\303\240 phi\303\252n b\341\272\243n.md"  
...
```
* Vậy mã hash của commit gần nhất là `9a371571e5c1fed307a47594c043335e92892c8b`. Và giờ tôi sẽ tái tạo lại nó.
* Vì commit ID được tái tạo dựa trên các nội dung commit, gồm ID commit cha, tác gỉa, thời gian, thông điệp commit và 1 số thông tin khác. Nên để tái tạo lại commit ID này, tôi cần lấy các thông tin đó.
```shell
ngtukien@NgTuKien:~/Documents/TYP/typ-training-2025$ git --no-replace-objects cat-file commit HEAD
tree 52c86f809693a8296ff4c119513e016d1af92676
parent 3ef920827b9e95e04664abf3c60eb78fcb54ba52
author NguyenTuKien <KienNT.B23CN465@stu.ptit.edu.vn> 1760682914 +0700
committer NguyenTuKien <KienNT.B23CN465@stu.ptit.edu.vn> 1760682914 +0700

Update nội dung phần 3
```
    - `git cat-file commit HEAD`: Lệnh này yêu cầu Git: "Hãy tìm đối tượng được HEAD trỏ tới, coi nó là một commit, và hiển thị nội dung metadata của nó cho tôi xem."
    - `--no-replace-objects`: Vì git có thể thay đổi lịch sử commit thông qua các cơ chể như `git rebase` hoặc `git replace`. Nên để đảm bảo rằng tôi lấy đúng commit gốc, tôi sử dụng tùy chọn này để yêu cầu Git không sử dụng bất kỳ đối tượng thay thế nào khi hiển thị commit.
* Như trên, tôi thấy các thông tin của 1 commit trong metadata bao gồm:
    - Tree: 52c86f809693a8296ff4c119513e016d1af92676
    - Parent: 3ef920827b9e95e04664abf3c60eb78fcb54ba52
    - Author: NguyenTuKien <KienNT.B23CN465@stu.ptit.edu.vn> 1760682914 +0700
    - Committer: NguyenTuKien <KienNT.B23CN465@stu.ptit.edu.vn> 1760682914 +0700
    - Message: Update nội dung phần 3
    * Bây giờ tôi sẽ sử dụng các thông tin trên để tái tạo lại commit ID.
```shell
ngtukien@NgTuKien:~/Documents/TYP/typ-training-2025$ printf "commit %s\0" $(git --no-replace-objects cat-file commit HEAD | wc -c)
commit 267
```
* Tôi sử dụng lệnh `git cat-file commit HEAD | wc -c` để đếm số byte trong nội dung commit, kết quả là 267 byte.
* Bây giờ tôi sẽ thử in ra các thông tin mà tôi vừa có gồm số bit và các thông tin commit.
```shell
ngtukien@NgTuKien:~/Documents/TYP/typ-train(printf "commit %s\0" $(git --no-replace-objects cat-file commit HEAD | wc -c); git cat-file commit HEAD)mmit HEAD)
commit 267tree 52c86f809693a8296ff4c119513e016d1af92676
parent 3ef920827b9e95e04664abf3c60eb78fcb54ba52
author NguyenTuKien <KienNT.B23CN465@stu.ptit.edu.vn> 1760682914 +0700
committer NguyenTuKien <KienNT.B23CN465@stu.ptit.edu.vn> 1760682914 +0700

Update nội dung phần 3
```
* Kết quả in ra giống hệt nội dung commit, chỉ khác là có thêm dòng `commit 267` ở đầu.
* Bây giờ tôi sẽ sử dụng lệnh `sha1sum` để tính toán mã hash SHA-1 của nội dung trên.
```shell
ngtukien@NgTuKien:~/Documents/TYP/typ-training-2025$ (printf "commit %s\0" $(git --no-replace-objects cat-file commit HEAD | wc -c); git cat-file commit HEAD) | sha1sum
9a371571e5c1fed307a47594c043335e92892c8b  -
ngtukien@NgTuKien:~/Documents/TYP/typ-training-2025$ git show 9a371571e5c1fed307a47594c043335e92892c8b
commit 9a371571e5c1fed307a47594c043335e92892c8b (HEAD -> main)
Author: NguyenTuKien <KienNT.B23CN465@stu.ptit.edu.vn>
Date:   Fri Oct 17 13:35:14 2025 +0700

    Update nội dung phần 3

diff --git "a/kiennt_b23dccn465_training_gd1/Tu\341\272\247n 1: Git v\303\240 Github/Ph\341\272\247n 3: L\303\240m vi\341\273\207c v\341\273\233i l\341\273\213ch s\341\273\255 v\303\240 phi\303\252n b\341\272\243n.md" "b/kiennt_b23dccn465_training_gd1/Tu\341\272\247n 1: Git v\303\240 Github/Ph\341\272\247n 3: L\303\240m vi\341\273\207c v\341\273\233i l\341\273\213ch s\341\273\255 v\303\240 phi\303\252n b\341\272\243n.md"
index 3ec1a9b..0da8cdc 100644
...
```
* Kết quả mã hash SHA-1 tính toán được là `9a371571e5c1fed307a47594c043335e92892c8b`, giống hệt mã hash của commit ban đầu.
* Như vậy tôi đã tái tạo thành công commit ID dựa trên các thông tin commit. Điều này chứng tỏ rằng Git sử dụng thuật toán SHA-1 để tạo ra commit ID dựa trên nội dung commit, đảm bảo tính duy nhất và toàn vẹn của mỗi commit trong repository.
* Tài liệu tham khảo: 
  - https://git-scm.com/book/en/v2/Git-Internals-Git-Objects#_sha_1
  - https://gist.github.com/masak/2415865
