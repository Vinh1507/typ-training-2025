1. **Biến, kiểu dữ liệu và toán tử**  
- Khai báo biến:

  	\+, var : phạm vi toàn hàm

  	\+, let : phạm vi block {}

  	\+, const : tương tự let nhưng nó không thể bị gán lại

- Kiểu dữ liệu cơ bản: String, Number, Boolean, Object, Array, Null, Undefined  
- Toán tử: số học ( \+, \-, \*, /), so sánh ( \==, \===, \!= , \!== , \>, \<) 

  và logic( &&, ||, \!)

2. **Rẽ nhánh và vòng lặp**  
- Rẽ nhánh: if…else, switch…case và toán tử 3 ngôi  
- Vòng lặp: for, while, do…while, for…of, for…in  
3. **Hàm function**  
- Declaration function(hàm định nghĩa):

  	\+, có tính hoisting( hàm được gọi trước lúc khai báo)

  	\+, cú pháp: 	

  function(tham số 1, tham số 2\)

  \+, có đối tượng arguments

  vd: 

  function tong(){

      let tong \= 0;

      for(const item of arguments){

          tong \+= item;

      }

      return tong

  }

  var total \= tong(10,20,30,40);

  console.log(total)

  	


  


- Express function(Hàm biểu thức):

  \+, Không có tính hoisting

  \+, Có đối tượng arguments

  \+, var tên hàm \= function(tham số 1, tham số 2){

  		// code function

  		}

			vd:  
			  
				var tong \= function(){  
    let sum \= 0;  
    for(const x of arguments){  
        sum\+=x;  
    }  
    return sum;  
}  
var ans \= tong(10,20,30,40,50);  
console.log(ans);  
	

- Arrow function:

  	\+, Không có tính hosting

  	\+, Không có đối tượng arguments

  	\+, Cú pháp: var tenHam \= (tham số 1, tham số 2\) ⇒{

  			// code function

  			}

4. **Array**  
- **array.toString()** : chuyển array thành String, tự động thêm dấu chấm phẩy để ngăn cách  
- **array.join(separator)**: thêm dấu bất kỳ để ngăn cách, mặc định là ,  
    
- **array.pop()**: xoá phần tử cuối mảng, trả vể phần tử cuối mảng  
    
- **array.push()**:Thêm 1 hoặc nhiều phần tử, trả về độ dài mới của mảng

  console.log(list.push(“bootstrap”, “reactjs”))

- array.shift(): Dùng để xoá phần tử đầu mảng, trả về phần tử đầu mảng


- **array.unshift()** : Thêm 1 hoặc nhiều phần tử vào đầu mảng, trả về độ dài mới của mảng  
    
- **array.splice(index, howmany,item1…itemx)**: Xoá hoặc chèn phần tử mới vào mảng

  	\+, index: vi tri them/ xoa ( bat buoc)

  	\+, howmany: so phan tu can xoa

  	\+, item1-\> itemx: so phan tu can them vao


- **array1.concat(array2,array3)**: dung de noi 2 array, khong lam anh huong den mang ban dau  
    
- **array.slice(start,end)**: dung de cat cac phan tu, khong lam anh huong de mang ban dau  
  - **forEach()** :Duyệt qua mỗi phần tử của 1 mảng và thực hiện 1 hành động nào đó  
    - Cú pháp:  
      arr.forEach(function(currentValue, Index, array){  
      	// code  
      });  
      \+, currentvalue:phần tử hiện tại(tham số bắt buộc) nhưng có thể dùng \_ để thay thế nếu không sử dụng đến phần tử đó  
      \+, index : Chỉ số của phần tử hiện tại đang được xử lý  
      \+, array: Mảng hiện tại đang gọi hàm forEach()  
      vd:  
      	  
      const mang \= \[1,2,3,4,5,6,7,8,9,10\];  
      let sum \= 0;  
        
      mang.forEach((item, index, arrayOrigin)\=\>{  
          mang\[index\] \= item \+ 1;  
      });  
      console.log(mang); // sua luon tran mang ban dau  
      ⇒Không có return mà sửa trực tiếp trên mảng ban đầu  
        
  - **every()** : Kiểm tra tất cả các phần tử của 1 mảng phải thoả mãn 1 điều kiện gì đó  
    	\+, arr.every(function(currentvalue, index, array){  
    		return currentvalue \>= 5;  
    });  
      
  - **some()** : Kiểm tra có ít nhất 1 phần tử của 1 mảng phải thoả mãn 1 điều kiện gì đó  
    \+, arr.some(function(currentvalue, index, array){  
    		return currentvalue \>= 5;  
    });  
      
  - **find()**: Tìm xem trong mảng có giá trị giống với giá trị đang tìm không

    \+, neu co thi tra ve chinh phan tu cua mang do, neu khong thi tra ve undefined ⇒ chi tim duoc 1 phan tu trong mang

    \+, cu phap

    	\+, arr.find(function(currentvalue, index, array){

    			return item.ten \== “sinh”;

        });

    

  - **filter()** : Giống hàm find, tìm được nhiều phần tử và trả về 1 mảng các phần tử tìm được

    \+, Cú pháp:

    	arr.filter(function(currentValue, index, array){

    		// code xu ly

    });

    

    

    

    

    

    

- **map()** : Lặp qua từng phần tử của mảng và trả về 1 mảng mới

  \+, Số lượng phần tử bằng mảng cũ

  \+, Giá trị trả về được quyết định bởi return 

  \+, Cú pháp:

  	arr.map(function(currentValue, index, array){

  		// code xu ly

  });


  - **reduce()** : Duyệt qua từng phần tử của mảng để tính toán và trả về 1 giá trị cuối cùng.  
    \+, arr.reduce(function(accumulator, currentvalue, currentIndex, array){  
    	// code  
    }, initialValue);  
    		\+, accumulator: la gia tri cua lenh return cho moi lan lap  
    		\+, initalValue: gia tri khoi tao ban dau(khong bat buoc)  
    const numbers \= \[1,2,3,4\];  
    const sum \= numbers.reduce((total, item)\=\>{  
        console.log(total);  
        console.log(item);  
        return total \+ item; // gan cho total vong lap sau  
    });  
    console.log(sum);  
    ⇒ Nếu không truyền initalValue thì mặc định nó là phần tử đầu tiên của mảng  
5. **String**  
- string.length  
    
- string.indexOf() : Tìm vị trí đầu tiên của 1 chuỗi trong 1 chuỗi

  \+, Nếu không thấy trả về \- 1

  \+, Có phân biệt hoa thường

  \+, string.indexOf(searchValue, start), start vi tri bat dau


- lastIndexOf() : Tìm vị trí cuối cùng trong 1 chuỗi

  \+, string,lastIndexOf(searchValue, start), start vi tri bat dau tinh tu trai qua phai va se tim kiem nguoc lai tu phai qua trai(mac dinh tu 0\) ⇒ lay ra dc vi tri dau tien xuat hien tinh tu phai qua trai


- slice() : Cắt 1 chuỗi và trả về 1 chuỗi mới, chuỗi ban đầu không thay đổi

  \+, string.slice(start,end)  ⇒ tu start \- \> end-1

  	⇒ky tu cuoi cung la \-1, khong truyen tham so la copy


- replace() : Thay thế 1 chuỗi bằng 1 chuỗi mới nhưng chỉ thay thế chuỗi đầu tiên tìm thấy 

  string.replace(searchValue, newValue)


- toUpperCase() va toLowerCase() : Viết hoa, viết thường hết string  
  	  
- trim(): Dùng để cắt bỏ khoảng trắng ở đầu và cuối


- charAt(): dung de lay ky tu thong qua index  
  		\+, string.charAt(index)  
- split() : Tách chuỗi thành 1 string

  \+, string.split()

  \+, regex 

  	var myString \= 'HTML,          CSS,  JS          ';

  myString \= myString.replace(/^\\s\+|\\s\+$|\\s\+(?=\\s)/g,"");

  console.log(myString.split(","))

6. **Callback và Callback hell**  
- Hàm callbacks là 1 hàm được truyền tham số vào đối số của 1 hàm khác


  const kiemTraSoDuong \= (number)\=\>{

      if(number \>= 0){

          console.log("so duong");

      }

      else{

          console.log("so am");

      }

  }


  const kiemTraChanLe \= (number)\=\>{

      if(number % 2 \== 0){

          console.log("so chan");

      }

      else{

          console.log("so le");

      }

  }

  const tinhTong \= (a,b, callback) \=\>{

      const kqua \= a \+ b;

      // kiemTraSoDuong(kqua);

      // kiemTraChanLe(kqua);

      callback(kqua);

  }

  tinhTong(10,20, kiemTraSoDuong); // truyen ham

  tinhTong(10,20, kiemTraChanLe); // truyen ham


  tinhTong(10, 20, (number)\=\>{ // truyen ham

      kiemTraSoDuong(number); // goi ham

      kiemTraChanLe(number); // goi ham

  });


  ⇒ Sau tach thanh cac file rieng roi import lai sau


  const loginSuccess \= () \=\>{

      console.log("success");

  };


  const checkLogin \= (datam,next)\=\>{

      const email \= "lamtest@gmail.com";

      const password \= "123456";


      if(data.email \=== email && data.password \=== password){

          next();

      }

      else{

          console.log("false");

      }

  }


  let data \= {

      email : "lamtest@gmail.com",

      password: "123456"

  };


  checkLogin(data, loginSuccess);


- Callback hell là hiện tượng nhiều callback lồng nhau gây ra sự khó đọc và khó bảo trì  
7. **OOP trong JS**  
- Class là khuôn mẫu để tạo ra object  
- Object là đối tượng thực tế của class, mỗi Object có riêng các giá trị của thuộc tính nhưng đều sẽ dùng chung phương thức từ class  
- vd:

  class Person {

    constructor(name, age) {

      this.name \= name; 

      this.age \= age;

    }

    greet() {

      console.log(\`Hi, I'm ${this.name}, ${this.age} years old\`);

    }

  }


  // Tạo object

  const person1 \= new Person("Lam", 20);

  person1.greet(); 


- Tham chiếu Reference: khi gán object cho biến khác, không tạo bản sao mà nó trỏ tới cùng 1 reference, thay đổi biến này sẽ ánh hưởng đến biến kia

  let objA \= { x: 10 };

  let objB \= objA; // objB tham chiếu objA

  objB.x \= 20;

  console.log(objA.x); // 20

- Kế thừa: Class con extend class cha, sử dụng super() để gọi constructor của class cha, class con có thể ghi đè (override) phương thức  
  class Student extends Person {

    constructor(name, age, major) {  
      super(name, age);   
      this.major \= major;  
    }  
    greet() {  
      console.log(\`Hi, I'm ${this.name}, studying ${this.major}\`);  
    }  
  }  
  const student1 \= new Student("Lan", 21, "C");  
  student1.greet();  
    
8. **DOM**  
- DOM là viết tắt của Document Object Model  
- Là mô hình các đối tượng tài liệu trong HTML  
- Để lấy được các thẻ HTML  
- DOM gồm 3 thành phần là Element: head, title, body,..,  Attribute: href,..., Text

		⇒ Mục đích:

- Lấy các thẻ, thay đổi các thuộc tính, thay đổi css, tạo, xoá, thêm các thẻ HTML

              **\-  Các loại DOM:**

- **DOM Document**:là DOM lớn nhất dùng để chứa toàn bộ các phần tử HTML  
- **DOM Element** : lấy các thẻ HTML  
- **DOM HTML** : thay đổi nội dung và thuộc tính của các thẻ trong HTML  
- DOM CSS : thay đổi CSS cua the  html  
- **DOM Event**: gán các sự kiện vào thẻ HTML  
- **DOM Listener**: lắng nghe các sự kiện tác động lên thẻ HTML  
- DOM Navigation: thể hiện mối quan hệ cha con giữa các thẻ HTML  
- DOM Nodes: để thao tác với HTML thông qua đối tượng Object

  **8.1 DOM Element**

- **getElementById**

  \+, Lấy 1 thẻ HTML theo id ⇒ kết quả trả về 1 phần tử do id là duy nhất, tìm được 1 thằng xong sẽ dừng lại luôn

  vd: 

  var element \= document.getElementById(‘idName’)


- **getElementByTagName**

  \+, Lấy 1 thẻ HTML theo tên thẻ, kết quả trả về là 1 mảng các Object

  vd:

  var element \= document.getElementsByTagName(“h2”)

- **getElementByClassName**

  \+, Lấy ra thẻ HTML theo class, kết quả trả về 1 mảng các Object

  vd:

  var element \= document.getElementsByClassName(“classname”);

- **querySelector ⇒ hay sử dụng**

  	\+, Lấy ra 1 thẻ HTML theo bộ chọn trong CSS(selector), kết quả trả về 1 phần tử, nếu tìm thấy sẽ dừng lại luôn. Bộ chọn selector có thể là \#id, .class, tag hoặc kết hợp như css div p,...

  	vd:

  		const title \= document.querySelector(“.title”)

  const title \= document.querySelector(“\#menu li a”)


  \<div id="app"\>

    \<p class="text"\>Hello\</p\>

    \<p class="text"\>World\</p\>

  \</div\>

  \<script\>

    const firstP \= document.querySelector(".text");

    console.log(firstP.innerText); // (chỉ phần tử đầu tiên)

  \</script\>


  

- **querySelectorAll ⇒ hay su dung**

  \+, Để lấy ra các thẻ HTML theo bộ trọn trong css(selector), kết quả trả về danh sách tất cả phần tử(NodeList), nếu không thấy phần tử nào thì trả về rỗng không phải null

  vd:

  	const title \= document.querySelectorAll(“\#menu li a”)


  	\<div id="app"\>

    \<p class="text"\>Hello\</p\>

    \<p class="text"\>World\</p\>

    \<p class="text"\>JavaScript\</p\>

  \</div\>


  \<script\>

    const allP \= document.querySelectorAll(".text");

    console.log(allP.length); // 3

    allP.forEach(p \=\> console.log(p.innerText));

    // Hello

    // World

    // JavaScript

  \</script\>

  ⇒ querySelectorAll() trả về NodeList không phải Array nhưng có thể duyệt bằng forEach(), nếu muốn sử dụng hàm mảng(map, filter) cần phải chuyển sang arr

  		const arr \= Array.from(allP);

		**8.2 DOM HTML**

- **Lấy nội dung của 1 element dùng innerHTML**

  \+, vd:

  const test \= document.querySelector(“h2”).innerHTML;

  ⇒ Lấy hết nếu trong h2 có cả các thẻ khác vd \<b\>\</b\>

  const test \= document.querySelector(“h2”).innerText;

  ⇒ Chỉ lấy text trong h2

- **Thay đổi nội dung**: để thay đổi nội dung của 1 element sử dụng innerHTML và gán lại giá trị

  \+, vd: 

  document.querySelector(“h2”).innerHTML \= “new value”


  

- **Lấy giá trị của thuộc tính HTML:**

  	vd: 

  const test \= document.querySelector(“h2”).getAttribute(“class/id”)

- **Thay đổi giá trị của thuộc tính**: Để thay đổi giá trị hiện tại của thuộc tính HTML sử dụng setAttribute()

  vd:

  document.querySelector(“h2”).setAttribute(“class”, “noidungmoi”);

  const h2\_01 \= document.querySelector(“\#h2-03”);

  h2\_01.setAttribute(“class”, “test-2”)

	**8.3 DOM CSS**

- Cú pháp thiết lập giá trị:  
        document.getElementById(“idName”).style.propertyName=”value”  
- Cú pháp lấy giá trị:

                 document.getElementById(“idName”).style.propertyName;

- Luu y: Ten thuoc tinh viet theo kieu camelCase

  font-size ⇒ fontSize

  margin-bottom ⇒marginBottom


	**8.4 DOM Events**

- Là 1 tác động nào đó lên thẻ HTML, để bắt được sự kiện và thực thi 1 chương trình  
- Cú pháp:

  element.eventname \= function(){

  	//

  }

- **Danh sach 1 so Dom Events:**

  \+, onload : khi chương trình load mọi thứ xong thì code trong đó mới được chạy

      \<script\>

          const listTagA \= document.querySelectorAll(“\#menu li a”)

      \</script\>


  

  ⇒ dat o dau body kqua khac voi dat cuoi body do no can load du lieu ⇒ phai sd window onload

     

      \<script\>

          window.onload\=()\=\>{

              const listTagA \= document.querySelectorAll(“\#menu li a”);

              console.log(listTagA);

          }

      \</script\>


  

  \+, onblur :kích hoạt 1 phần tử mất trọng tâm không được focus vào nữa

  \<script\>

          const input \= document.querySelector("\#input");

          input.onblur \= (event)\=\>{

              console.log(event.target.value);

              event.target.value \= value.toUpperCase();

          }

      \</script\>


  


  \+, onfocus : Kích hoạt 1 phần tử được focus vào

  	⇒ tương tự onblur


  


  

  \<script\>

          const input \= document.querySelector("\#input");

          input.onfocus \= (event)\=\>{

              console.log(event.target.value);

              event.target.value \= value.toUpperCase();

          }

      \</script\>


  


  \+, onkeydown : Kích hoạt khi 1 phím được nhấn

  \<script\>

          const input \= document.querySelector("\#input");

          input.onkeydown \= (event)\=\>{

  		 alert(event.key);

              console.log(event);

          }

      \</script\>


  ⇒ chay cac cau lenh trong function truoc khi insert vao input

  \+, onkeyup : Kích hoạt khi 1 phím được nhả ra

  \<script\>

          const input \= document.querySelector("\#input");

          input.onkeyup \= (event)\=\>{

  		 alert(event.key);

              console.log(event);

          }

      \</script\>

			⇒ insert vao input roi moi chay cac cau trong function

\+, onclick : Kích hoạt khi click vào phần tử  
\<body\>  
    \<style\>  
        .d-none{  
            display: none;  
        }  
    \</style\>  
    \<div id\="ads-1" class \= "ads test1 tes2 tes 3"\>  
        \<button id \= "close-ads"\>x\</button\>  
        \<a href\="\#" target\="\_blank"\>  
            \<img src\="content4.jpg" alt\="" width\="200px"\>  
        \</a\>  
    \</div\>  
    \<script\>  
        const ads1 \= document.querySelector("\#ads-1");  
        const closeAds \= document.querySelector("\#close-ads");  
        closeAds.onclick \= ()\=\>{  
            ads1.setAttribute("class","d-none");  
        }  
        setTimeout(()\=\>{  
            ads1.setAttribute("class", "d-none");  
        },10000)  
    \</script\>  
\</body\>

\+, onchange : Kích hoạt khi giá trị được thay đổi khác đi so với trước đó  
**8.5** **DOM Events Listener** 

- Giống Dom Event nhưng khác ở chỗ 1 element có thể gọi được nhiều sự kiện  
- Có thể huỷ bỏ lắng nghe sự kiện bất kỳ, DOM Events không làm được điều đó  
- Cú pháp:

  element.addEventListener(“eventname”,function(e){

  });

  \+, element: là phần tử muốn bắt sự kiện

  \+, eventname: tên sự kiện bỏ đi tiền tố on

  	vd: Voi event thi cung 1 button.onclick nhung truyen vao 2 ham khac nhau thi no chi thuc hien 1 ham

  \<body\>

      \<button id\="button"\>

          Click

      \</button\>

      \<script\>

          const button \= document.querySelector("\#button");

          const ham1 \= ()\=\>{

              console.log("thuc hien cong viec 1")

          }

          const ham2 \= ()\=\>{

              console.log("thuc hien cong viec 2")

          }

          button.addEventListener("click" ,()\=\>{

              ham1();

          });

          button.addEventListener("click" ,()\=\>{

              ham2();

          });

      \</script\>

  \</body\>


- Huỷ bỏ lắng nghe sự kiện: sau 1 thời gian 1 hàm không chạy nữa

  \<body\>

      \<button id\="button"\>

          Click

      \</button\>

      \<script\>

          const button \= document.querySelector("\#button");

          const ham1 \= ()\=\>{

              console.log("thuc hien cong viec 1")

          }

          const ham2 \= ()\=\>{

              console.log("thuc hien cong viec 2")

          }

          button.addEventListener("click" ,ham1);

          button.addEventListener("click" ,ham2);

          setTimeout(()\=\>{

              button.removeEventListener("click",ham2)

          },3000)

      \</script\>

  \</body\>


**8.6 DOM Navigation**

- Thể hiện mối quan hệ cha-con của các thẻ HTML  
- Các thuộc tính:

  \+, parentNode: Truy cap phan tu cha

  	vd: 

  const child1 \= document.querySelector(“\#child1”);

  	console.log(child1.parentNode);     

  \+, childNodes: Truy cap vao cac phan tu con

  	⇒ lay ra cac cac khoang trang vi khoang trang cx la 1 node con

  \+, firstElementChild: truy cap vao phan tu con dau tien

  \+, lastElementChild: truy cap vao phan tu con cuoi cung

  \+, nextElementSibling: truy cap vao phan tu con ket tiep

  \+, previousElementSibling: truy vap vao phan tu truoc do

  \+, nodeName: lay ra ten node

	**8.7 DOM Nodes**

- var tenBien \= document.createElement(“tagName”) : Tao 1 phan tu html  
  	tagname: p, h1, div….  
   \<body\>  
      \<script\>  
          const div \= document.createElement("div");  
          document.querySelector("body").appendChild(div);  
          div.innerHTML \= "Quang cao";  
      \</script\>  
  \</body\>  
    
- document.createTextNode(): tao ra 1 chuoi van ban  
- const p \= document.createElement("p");  
-         const text \= document.createTextNode("noi dung")  
-         p.appendChild(text);  
  \=\> tuong tu dung innerHTML nhung dai hon  
- element\_parent.appendChild(node-insert): Them vao phan tu cuoi cung cua 1 the html khac  
    
- element\_parent.insertBefore(node-insert, node-child): Them vao 1 node vao truoc 1 node con khac. node\_child la node con b muon them node-insert vao dang truoc no  
  \<ul id\="menu"\>  
          \<li\>Item\</li\>  
          \<li\>Item\</li\>  
          \<li\>Item\</li\>  
      \</ul\>  
      \<button id \= "button"\>Add\</button\>  
      \<script\>  
          const p \= document.createElement("p");  
          const text \= document.createTextNode("noi dung")  
          p.appendChild(text);  
      \</script\>  
      \<script\>  
          const menu \= document.querySelector("\#menu");  
          const button \= document.querySelector("\#button");  
          button.addEventListener("click", ()\=\>{  
              const liFirst \=menu.querySelector("li:nth-child(1)");  
              const li \= document.createElement("li");  
              li.innerHTML \= "Item new";  
    
              menu.insertBefore(li, liFirst);  
          })  
      \</script\>  
    
- element\_parent.removeChild(node-remove): Xoa 1 node con ra khoi node cha  
    
  const menu \= document.querySelector("\#menu");  
          const buttonsDelete \= menu.querySelectorAll("button");  
          for(const buttonDelete of buttonsDelete){  
              buttonDelete.addEventListener("click", (event)\=\>{  
                  menu.removeChild(event.target.parentNode);  
              });  
          }  
    
- element\_parent.replaceChild(node\_insert, node\_remove);

  \<script\> 

          const menu \= document.querySelector("\#menu");

          const button \= document.querySelector("\#button");

  // lay ra button add 

          button.addEventListener("click",()\=\>{

              const liSecond \= document.querySelector("li:nth-child(2)");

              const li \= document.createElement("li");

              li.innerHTML \= \`

                  \<input value \= "Cong viec 4"/\> \<button\>X\</button\>

              \`;

              menu.replaceChild(li,liSecond);

          });

      **\</script\>**

**9\. JSON**

- JSON : Javascript Object Notation  
- Là 1 định dạng dữ liệu lưu dưới dạng String. **Cho phép lưu các kiểu dữ liệu cơ bản như:** **number, string, boolean, array, object, null**

  	\+, **Không cho phép: function, date, undefined**

- Trường hợp giá trị của JSON là dạng Object thì :

  \+, Có các cặp key/value

  \+, Key đặt trong dấu nháy kép


  var objectJS \= {

      fullName: "Tran Quang Lam",

      phone: "0123456789",

      email: "tlam15282@gmail.com"

  }


  var objectJSON \= \`

  {

      "fullName": "Tran Quang Lam",

      "phone": "0123456789",

      "email" : "tlam15282@gmail.com"

  }

  \`;


**10\. Promise**

- Promise dùng để giải quyết callback hell  
- Promise có cách viết đơn giản và dễ nhìn hơn callback  
- Cú pháp:

  var promise \= new Promise((resolve, reject)=\>{

  	// resolve() neu thanh cong vao ham nay

  	// reject() neu that bai vao ham nay

  });


  promise

  	.then((success))=\>{

  		// neu thanh cong

  });

  .catch((error) \=\>{

  	// neu that bai

  })

  .finally(()=\>{

  // luon luon chay vao day	

  })

- Trong đó

  \+, new Promise: khởi tạo promise

  \+, resolve: là 1 hàm callback xử lý cho hành động thành công

  \+, reject: là 1 hàm callback xử lý cho hành động thất bại

  \+, . then : thành công chạy đến đây

  \+, .catch: thất bại vào đây

  \+, .finally: luôn chạy vào đây

  var a;

  var promise \= new Promise((resolve, reject)\=\>{

      if(a \=== undefined){

          reject();

      }

      else{

          resolve()

      }

  });


  promise

      .then(()\=\>{

          console.log("Thanh cong");

      })

      .catch(()\=\>{

          console.log("That bai");

      })

      // .finally(()=\>{

      //     console.log("luon luon chay vao")

      // })


  


  


  var a\=10;

  var promise \= new Promise((resolve, reject)\=\>{

      if(a \=== undefined){

          reject();

      }

      else{

          resolve(a); // cho den khi bao gio tra ra kqua thi chay vao then

      }

  });


  promise

      .then((resultA)\=\>{

          console.log(resultA);

          return resultA;

      })

      // then ben tren return roi moi xuong then ben duoi

      .then((resultA)\=\>{

          // neu then tren khong tra ve gi thi undedined

          const resultB \= resultA \+ 10

          console.log(resultB);

      })

      .catch(()\=\>{

          console.log("That bai");

      })

      // .finally(()=\>{

      //     console.log("luon luon chay vao")

      // })


- Promise co 3 trang thai:

  \+, Pending: Khi promise dang chay thi se o trang thai nay

  ⇒ kqua la undefined

  \+, Fulfilled: Khi promise da chay xong thi se o trang thai nay

  ⇒ kqua la 1 gia tri

  \+, Reject: khi promise bi loi se o trang thai nay

  ⇒ Kqua la 1 object loi

⇒ ham promise la ham co the cho doi

const promise \= new Promise((resolve, reject) \=\>{  
    setTimeout(()\=\>{  
        resolve();  
    },3000);  
});

setTimeout(() \=\>{  
    console.log("sau 1 giay: ", promise);  
},1000);

	⇒ Đồng bộ: chạy lần lượt chạy hết bên trên xong xuống đến bên dưới. Bất đồng bộ: chạy chưa xong bên trên đã chạy tiếp bên dưới  
**11\.  Async/Await**

- Async/Await là 1 tính năng của hs, giúp ta làm việc với các hàm bất đồng bộ 1 cách dễ hiểu hơn  
- Nó được xây dựng trên Promise  
- Async: Khai báo 1 hàm bất đồng bộ

  	\+, Tự động biến đổi 1 hàm thanh 1 Promise

  	\+, Từ khoá Async đc đặt trước 1 hàm

- Await: Tạm dừng việc thực hiện các hàm async(thay cho then)

  	\+, Khi được đặt trc 1 promise, nó sẽ đợi promise kết thúc và trả về kq

  	\+, Await chỉ có thể đc sử dụng bên trong các hàm Async


  

  //4

  const fetchApi \= async (api)\=\>{

      const response \= await fetch(api);

      // console.log(response)

      const data \= await response.json();

      return data;

  }


  fetchApi("http://example.com/movies.json")

      .then((data)\=\>{

          console.log(data);

      })


  

**12\. Fetch**

- Phương thức Fetch dùng để gọi lên server thông qua API để lấy dữ liệu từ server trả về  
- Cú pháp:

  fetch('http://example.com/movies.json')

      .then((response)\=\>{

          return response.json();

      })

      .then((data)\=\>{

          console.log(data);

      })

      .catch((error)\=\>{

          console.log(error);

      });


	⇒ fetch(): dùng để gửi yêu cầu lên thông qua API  
	    then(): dùng để thực thi khi có phản hồi từ Server trả về  
	    catch(): được thực thi khi không có phản hồi tử Server  
⇒ Bản chất Fetch() là 1 promise

	

