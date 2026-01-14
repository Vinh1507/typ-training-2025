**Phần 1: CSS cơ bản**

- CSS (Cascading Style Sheets) là ngôn ngữ dùng để định dạng và trình bày giao diện cho các phần tử HTML trên trang web  
  ⇒ Giúp tạo giao diện đẹp, thống nhất cho web, tách biệt được nội dung và kiểu dáng từ đó dễ bảo trì hơn. Giúp web tối ưu tốc độ và cải thiện UI/UX  
- 3 Cách CSS cơ bản: Inline, Internal, External  
  	\+, Inline: Css được viết trực tiếp trong thuộc tính style của thẻ HTML  
  	\+, Internal: Css được viết trong thẻ \<style\> bên trong phần \<head\>  
  	\+, External: Viết trong file .Css rồi liên kết bằng \<link\> ⇒ Tối ưu, dễ tái sử dụng nên được khuyến khích dùng nhất  
- Cú pháp:

  \+, selector {

  	property: value;

  }

  ⇒ Với selector là phần tử HTML cần định dạng, property: thuộc tính cần thay đổi, value: giá trị gán cho thuộc tính

- Css Selector:

  \+, Element selector: Áp dụng cho tất cả các thẻ h1-\>6, p, div,...

  	p { 

      color: red; 

  }

  \+, Class selector: Áp dụng cho phần tử mà chứa class selector đó

  	.co-red{

  	    color: red;

  }


  


  \+, Id selector: Áp dụng cho phần tử chứa id selector đó

  	\#header{

  		font-size: 20px;

  }

  \+, Descendant selector: chọn các thẻ trong các thẻ khác để được áp dụng css

  	div p{

  		color: blue;

  }

		\+, Group selector: Áp dụng cùng kiểu cho nhiều selector  
			h1, h2, h3{  
				color: orange;  
}

**Phần 2: Styling cơ bản**

- Typography:

  \+, font-family: Chọn font chữ, có thể áp dụng nhiều font, cách nhau bằng dấu phẩy

  \+, font-size: Kích thước chữ có thể là px, %, em, rem

  \-px: cố định

  \-%:  % so với font-size của phần tử cha

  \-em: tỉ lệ so với font-size của phần tử cha

  \-rem: tỉ lệ so với font-size của root

  \+, font-weight: Độ đậm nhạt có thể là normal, bold hay 1 số 

  \+, color: Màu chữ có thể là tên, mã HEX, RGB

  \+, text-align: Căn lề văn bản left, center, right

  \+, line-height: Khoảng cách dòng


- Background & Color:

  \+, background-color: màu nền phần tử

  \+, background-image: Đặt hình nền

  \+, background-size: Kích thước hình nền 

  	\+, cover: cắt bỏ nếu tỷ lệ khác

  	\+, contain: phóng to đến khi vừa khít trong phần tử, không cắt nhưng có thể xuất hiện khoảng trống

  	\+, auto

  \+, background-position: Vi trí bắt đầu hiển thị hình nền, center, top, right, 50%, 20%,...

  \+, background-repeat: quy định có lặp lại hình nền hay không

- Box Model

  \+, width/height: Kích thước nội dung

  \+, padding: khoảng cách bên trong giữ nội dung và viền

  \+, border: viền bao quanh phần tử

  \+, margin: khoảng cách bên ngoài giữ phần tử và xung quanh

  \+, box-sizing: cách tính kích thước tổng content-box, border-box: width bao gồm cả padding và border

**Phần 3: Layout và Positioning**

- Display property:  
  	\+, display: block ⇒ hiển thị dạng khối  
  	\+, display: inline ⇒ hiển thị dạng nội tuyền  
  	\+, display: inline-block ⇒ hiển thị dạng kết hợp block và inline  
  	\+, display: none ⇒ Ẩn hiện phần tử  
- Positioning:

  \+, position: static ⇒ vị trí mặc định

  \+, position: relative ⇒ vị trí tương đối, dùng các thuộc tính top, left, right, bottom để di chuyển nhẹ. Làm container cha cho các phần tử absolute bên trong

  \+, position: absolute ⇒ vị trí tuyệt đối, vị trí của nó được xác định so với phần tử cha gần nhất có position khác static, nếu kh có sẽ theo body hoặc thẻ html

  \+, position: fixed ⇒ được cố định so với cửa sổ trình duyệt, áp dụng cho header, navbar cố định ở đầu trang

- Flexbox layout:

  \+, display: flex ⇒ biến phần tử cha thành flex container, các phần tử con thanh flex items, toàn bộ cách sắp xếp con sẽ do flexbox điều khiển, không còn theo khối block, inline thông thường

  \+, flex-direction ⇒ xác định hướng sắp xếp phần tử con

  	\+, row: từ trái sang phải, row-reverse: từ phải sang trái, column: từ trên xuống dưới, column-reverse: sắp xếp từ dưới lên trên

  \+, justify-content: ⇒ Căn chỉnh theo trục chính

  	\+, center: căn giữa theo trục chính, space-between: chia đều khoảng trống giữa các item, space-around: chia đều và có khoảng trống 2 bên item, space-evenly: chia đều hoàn toàn kể cả 2 biên

  \+, align-items: căn chỉnh theo trục phụ: stretch, flex-start, center, flex-end, baseline

  \+, flex-wrap: cho phép xuống dòng khi cần, nếu không đủ chỗ cho phép các phần tử xuống hàng/cột. Mặc định flexbox không xuống dòng, tất cả item bị nén vào 1 hàng. nowarp, wrap, wrap-reverse

  \+, flex-grow: cho biết độ giãn ra khi còn chỗ trống

  \+, flex-shrink: xác định mức độ co lại khi không đủ chỗ

  \+, flex-basis: xác định kích thước ban đầu

  ⇒ Cú pháp rút gọn: flex: \<grow\> \<shrink\>\<basis\>

- Responsive design  
  	\+, Media queries: điều kiện css cho từng loại thiết bị  
  		@media screen and (điều kiện){  
  			/css áp dụng khi điều kiện đúng/

  }  
  \+, với @media \-\> từ khoá chỉ định bắt đầu media query  
  \+, screen \-\> loai thiết bị : screen, print, speech,...  
  \+ điều kiện: giới hạn kích thước  
  ví dụ:  
  	  
          @media screen and (max-width: 768px) {  
              body {  
                  background-color: lightblue;  
              }  
    
              .menu {  
                  display: none;  
              }  
          }  
    
  \+, 1 số breakpoints phổ biến: 320px, 768px, 1024px, 1200px  
  \+, responsive unit:  
  	\-px: cố định

  \-%:  % so với font-size của phần tử cha  
  \-em: tỉ lệ so với font-size của phần tử cha  
  \-rem: tỉ lệ so với font-size của thẻ \<html\>  
  \- 1vw : 1% chiều rộng của màn hình trình duyệt  
  \- 1vh : 1% chiều cao của màn hình

**Phần 5\. Css Effects và Animations**

- Css transitions:

  \+, transition: giúp hiệu ứng thay đổi từ từ giữa 2 trạng thái css

  \+, transition-property: chọn thuộc tính cần hiệu ứng

  \+, transition-duration: thời gian thực hiện

  \+, transition-timing-function: các tốc độ thay đổi theo thời gian: ease, linear, ease-in, ease-out, ease-in-out

  \+, transition-delay: độ trễ trước khi bắt đầu

- Css transforms:

  \+, transform: translate(): Di chuyển phần tử, thay đổi hình dạng trong không gian 2D, 3D, xoay, phóng to, nghiêng….

- translate(x,y) : di chuyển phần tử theo trục x,y  
- scale(x,y): phóng to, thu nhỏ phần tử  
- rotate(deg): xoay phần tử quanh tâm  
- skew(x,y): nghiêng phần tử theo trục x, y

\- Css Animations: tạo chuyển động liên tục, lặp lại  
	⇒ khác với transition chỉ hd khi có sự kiện, animation có thể tự chạy liên tục hoặc theo cách mình định nghĩa

- Gồm 2 phần  
  	\+, @keyframes \-\> định nghĩa các trạng thái trong animation  
  	\+, animation-\* \-\> gán animation cho phần tử


    
          @keyframes moveBox {  
              from {  
                  left: 0;  
              }  
    
              to {  
                  left: 200px;  
              }  
          }  
    
          .box {  
              position: relative;  
              animation-name: moveBox;  
              animation-duration: 2s;  
          }


		⇒ Box di chuyển từ trái sang phải trong 2s  
			\+, animation-name: tên của animation( trùng với tên trong @keyframes)  
			\+, animation-duration: thời gian thực hiện  
			\+, animation-timing-function: tốc độ thay đổi(ease, linear, ease-in-out)  
			\+, animation-delay: thời gian chờ trước khi chạy  
			\+, animation-iteration-count: số lần lặp   
			\+, animation-fill-mode: giữ trạng thái sau khi kết thúc(forwards, backwards, both)  
	⇒  animation: name duration timing delay iteration direction fill-mode;