🧾 BÁO CÁO LÝ THUYẾT – GIAI ĐOẠN 4: Javascript

I. JS cơ bản 1
1.  Biến
2.  Toán tử
3.  Kiểu dữ liệu
    6 kiểu: Number,String, Boolean, Undefined, Null, Symbol
4.  1 số hàm built-in
    - alert(): hiện popup 
    - confirm(): hiện popup có lựa chọn yes/no
    - prompt(): dùng để lấy thông tin từ người dùng
    - console.log(): hiện dữ liệu
    - console.warn()
    - console.eror()
    - setTimeout(function(){
        //code
    },time): code sẽ chạy 1 lần duy nhất sau 1 khoảng thời gian
    - setInterval(function(){
        //code
    },time):  code sẽ chạy lặp lại sau 1 khoảng thời gian
5.  Typeof: trả về kiểu dữ liệu của 1 biến
6.  Làm việc với String
- length
- indexOf: tìm vtri đầu tiên của 1 chuỗi nằm trong 1 chuỗi
- lastIndexOf(): tìm vtri cuối cùng của 1 chuỗi nằm trong 1 chuỗi
- slice(start,end ): cắt 1 chuỗi và trả về chuỗi mới
- replace(): thay thế 1 chuỗi thành chuỗi mới
- toUpperCase() và toLowerCase()
- trim()
- charAt()
- split() : chuyển 1 chuỗi thành 1 array
- isNaN() : kiểm tra xem 1 biến có phải là NaN không
7. Làm việc với Number
- toString
- toFixed(): làm tròn
8. Làm việc với Array
- toString();
- join(",") : chuyển aray thành string
- pop() : dùng để xóa phần tử cuối mảng trả về phần tử cuối mảng đó
- push(): thêm phần tử vào cuối mảng
- shift(): xóa phần tử đầu mảng trả về phần tử đầu mảng đó
- unshift("",""): thêm 1 hoặc nhiều phần tử vào đầu mảng
- splice(): xóa hoặc chèn phần tử mới vào bảng
- concat(): dùng để nối 2 array
- slice(): cắt các phần tử

II. JS cơ bản 2
1. If
2. for
3. for in: dùng để lấy ra key của 1 object
4. for of: dùng để lấy ra phần tử của aray,object
5.  while
6. break
7. continue

III. JS cơ bản 3
1. functions
- derlaration function
    function tenHam(thamSo1, thamSo2){
        //code
    }
- expression function
    var tenHam= function(thamSo 1,thamSo 2){
        //code
    }
- arrow function
    var tenHam=(thamSo 1, thamSo 2)=>{
        //code
    }
2. try catch
3. làm việc với array nâng cao
-   arr.forEach(function(currentValue,index,aray){
        //code
    }): Để duyệt qua mỗi phần tử của mảng và thực hiện một hành động nào đó,Có thể thay đổi trực tiếp mảng ban đầu
-   arr.every(function(currentValue, index, array) {
	    // code xử lý
	}) : kiểm tra các phần tử của mảng có thỏa mãn 1 điều kiện gì đó
-   arr.some(function(currentValue, index, array) {
	    // code xử lý
	}); :kiểm tra chỉ cần 1 phần tử của mảng thỏa mãn điều kiện gì đó là được
-   arr.find(function(currentValue, index, array) {
	    // code xử lý
	}); : Tìm xem trong mảng có giá trị giống với giá trị đang cần tìm không, nếu có thì trả về chính phần tử của mảng đó. Nếu không có thì trả về undefined.
-   arr.filter(function(currentValue, index, array) {
	    // code xử lý
	}); :Giống find, nhưng cho phép tìm được nhiều phần tử và trả về một array gồm các phần tử đã tìm được.
-   arr.map(function(currentValue, index, array) {
	    // code xử lý
	}); Hàm map() sẽ lặp qua từng phần tử của mảng. Giá trị trả về của hàm map là một mảng mới, với số lượng phần tử bằng với mảng cũ, nhưng giá trị của các phần tử thì được quyết định bởi lệnh return của hàm map.
-   	arr.reduce(function(total, currentValue, currentIndex, array) {
	    // code xử lý
	}, initialValue); Hàm reduce() sẽ duyệt qua từng phần tử trong mảng và tính toán các phần tử đó, sau đó trả về một giá trị cuối cùng.



IV. JS cơ bản 4
1. BOM window
window.innerHeight;
window.innerWidth;
window.open(url,name,options): mở 1 cửa sổ mới
myWindow.close(): đóng cửa sổ
window.location.href: lấy đường dẫn của trang web

2.  BOM Srceen
-   screen.width: lấy chiều rộng màn hình 
-   screen.height

3. BOM Location
-   location.reload() :load lại trang
- 
4. BOM history
-   history.length: đếm tổng số trang đã lưu trong history
- history.back(): trở lại trang trước
- history.forward(): đi tới trang kế tiếp
- history.go(number): đi tới một trang

5. BOM Navigator

- navigator.cookieEnabled: Để kiểm tra trình duyệt có bật Cookie hay không.
- navigator.appName: Để kiểm tra tên trình duyệt.
- navigator.appCodeName: Để kiểm tra tên mã code của trình duyệt.
- navigator.appVersion: Để kiểm tra Version của trình duyệt.
- navigator.platform: Xem hệ điều hành mà người dùng đang sử dụng.
- navigator.language: Để kiểm tra ngôn ngữ của trình duyệt.

6. BOM popup
- alert,confirm,prompt
7. BOM Timing
-   setTimeout(function,miliseconds)
-   setInterval

8. Cookies
-    Tạo cookie: document.cookie='name=value'
- Lấy giá trị cookie: var giatri= document.cookie
-   Đổi giá trị cho cookie: document.cookie='name=value'
-   xóa cookie : xét ngày hết hạn cho cookie về thời gian trước đấy: document.cookie="username=;expires=Thu,01 Jan 1970 00:00:00 UTC";

V. JS cơ bản 5
1. DOM Elemnent
- var element = document.getElementById('idName');
- var element = document.getElementsByTagName('tagName');
- var element = document.getElementsByClassName('className');
- var element = document.querySelector('selector');
- var element = document.querySelectorAll('selector');
2. DOM HTML
- document.querySelector("h2").innerHTML; lấy nd của element

- document.querySelector("h2").innerHTML = "Nội dung mới"; thay đổi nội dung của element
- document.querySelector("h2") .getAttribute("attributeName"); lấy giá trị của thuộc tính html
-  document.querySelector("h2"). setAttribute("attributeName", "Nội dung mới"); thay đổi giá trị thuộc tính

3. DOM CSS
document.getElementById("idName").style.propertyName = 'value'; cú pháp thiết lập giá trị
document.getElementById("idName").style.propertyName; cú pháp lấy giá trị

4. DOM Events Listener
element.addEventListener("eventname", function(e) {
	// Code
});

5. DOM Navigation
- parentNode: dùng để truy cập nút cha của nút được chỉ định trong cây DOM
- childNodes: truy cập tất cả các node con của một phần tử nhất định. Node con có thể là một văn bản, chú thích,…
- firstElementChild: trả về phần tử là node con đầu tiên của phần tử cha.
- lastElementChild: trả về phần tử là node con cuối cùng của phần tử cha.
- nextElementSibling: trả về phần tử là node kế tiếp.
- previousElementSibling: trả về phần tử là node trước đó.
- nodeName: Trả về tên một node.

6. DOM Nodes

- var tenBien = document.createElement(“tagName"); tạo 1 node mới
- var tenBien = document.createTextNode("Nội dung…"); tạo 1 text node

- element_parent.appendChild(node_insert); dùng để thêm vào vị trí cuối cùng của đối tượng thẻ html nào đó
- element_parent.insertBefore(node_insert, node_child); them 1 node vào trước 1 node nào đó
- element_parent.removeChild(node_remove); xóa 1 node khỏi node hiện tại
- element_parent.replaceChild(node_insert, node_remove); để thay thế 1 node con bằng 1 node con khác

VI. JS nâng cao
1. localStorage
- localStorage là kho lưu trữ ở phía người dùng.
- Lưu trữ dữ liệu vô thời hạn, dữ liệu sẽ được lưu trữ cho tới khi người dùng xóa lịch sử duyệt web.
localStorage có 5 phương thức:
- localStorage.setItem(key, value);: Thêm dữ liệu vào localStorage
- localStorage.getItem(key) : Lấy dữ liệu từ localStorage
- localStorage.removeItem(key) : Xóa dữ liệu ra khỏi localStorage
- localStorage.clear(); Xóa toàn bộ dữ liệu ra khỏi localStorage
- localStorage.key(index) : Lấy tên key của dữ liệu đang lưu trữ trong localStorage

2. sessionStorage
- sessionStorage là kho lưu trữ theo phiên.
- Lưu trữ dữ liệu cho một phiên làm việc, có nghĩa dữ liệu sẽ bị mất khi bạn tắt browser.
sessionStorage có 5 phương thức:
- sessionStorage.setItem(key, value);: Thêm dữ liệu vào sessionStorage
- sessionStorage.getItem(key); Lấy dữ liệu từ sessionStorage
- sessionStorage.removeItem(key); Xóa dữ liệu ra khỏi sessionStorage
-sessionStorage.clear(); Xóa toàn bộ dữ liệu ra khỏi sessionStorage
- sessionStorage.key(index); Lấy tên key của dữ liệu đang lưu trữ trong sessionStorage

3. Callbacks
- Hàm callback (gọi lại) là một hàm được truyền dưới dạng đối số cho một hàm khác.
- Hàm callback có thể được chạy sau khi những chức năng khác kết thúc.

4. Promise
- Promise dùng để giải quyết vấn đề callback hell.
- Callback hell là có nhiều hàm lồng nhau gây ra khó chịu, nguyên nhân gây ra tình trạng callback hell là do: giả sử để chạy được hàm b thì hàm a phải thực thi xong đã, và để chạy được hàm c thì hàm b phải thực thi xong.
- Promise có cách viết đơn giản hơn so với callback, mỗi hàm nằm trên một dòng nên sẽ dễ nhìn hơn.
- Cú pháp:
var promise = new Promise((resolve, reject) => {
  // resolve(): Nếu thành công chạy vào hàm này
  // reject(): Nếu thất bại chạy vào hàm này
});
promise
  .then((success) => {
    // Nếu thành công chạy vào đây
  })
  .catch((error) => {
    // Nếu thất bại chạy vào đây
  })
  .finally(() => {
    // Luôn luôn chạy vào đây
  })

- Promise có 3 trạng thái:
Pending: Khi promise đang chạy thì sẽ ở trạng thái này, kết quả là undefined.
Fulfilled: Khi promise đã chạy xong thì sẽ ở trạng thái này, kết quả là một giá trị.
Rejected: Khi promise bị lỗi thì sẽ ở trạng thái này, kết quả là một object lỗi.

- Sử dụng Promise.all
Promise.all giúp cho các promise được thực thi song song nhau, tổng thời gian chạy của cả chương trình chỉ bằng thời gian chạy của promise chạy lâu nhất.
Cú pháp:
Promise.all([promise1, promise2, …])
  .then(([success1, success2, …]) => {
    // Nếu tất cả promise thành công thì chạy vào đây
  })
  .catch((error) => {
    // Chỉ cần một promise lỗi thì sẽ chạy vào đây
  })
  .finally(() => {
    // Luôn chạy vào đây
  })


5. Fetch API
- Phương thức Fetch dùng để gọi lên trên server thông qua một API để lấy dữ liệu từ trên server trả về.
- Api hiểu đơn giản thì nó là một url để cho phép bên Front-end có thể giao tiếp được với bên Back-end.
Cú pháp:
fetch('http://example.com/movies.json')
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
  })
  .catch((error) => {
    console.log(error);
  });

6. Async/Await
- Async / Await là một tính năng của JavaScript giúp chúng ta làm việc với các hàm bất đồng bộ theo cách dễ hiểu hơn.
Nó được xây dựng trên Promise.
- Async: khai báo một hàm bất đồng bộ.
Tự động biến đổi một hàm thông thường thành một Promise.
Từ khóa Async được đặt trước 1 hàm.
- Await: tạm dừng việc thực hiện các hàm async.
Khi được đặt trước một Promise, nó sẽ đợi cho đến khi Promise kết thúc và trả về kết quả.
Await chỉ có thể được sử dụng bên trong các function async.
