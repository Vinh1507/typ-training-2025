# [Nguyen Hoang Duc Tam - B25DCCN523] Training Basic - Week 3

## Phần 1: Java
1. ### Ôn tập
- Không quá khó do hoàn toàn tương tự với C++
- Mảng 1 chiều, 2 chiều
```Java
int[] arr = new int[a];
int[][] arr2D = new int[n][m];
```
- Vòng lặp
```Java
while(a-- > 0)
    arr[i] = sc.nextInt();

for(int i = 0; i < n; ++i)
    for(int j = 0; j < m; ++j)
        arr2D[i][j] = sc.nextInt();
```
2. ### Kiểu dữ liệu nguyên thuỷ và object
- Trong Java, các kiểu `byte`, `short`, `int`, `long`, `float`, `double`, `boolean` và `char` là kiểu dữ liệu nguyên thuỷ, ```String``` không phải là kiểu dữ liệu nguyên thuỷ.

|   | Nguyên thuỷ | Object |
|---|---|---|
| Phân biệt | Viết thường chữ cái đầu | Viết hoa chữ cái đầu| 
| Lưu | Lưu giá trị của biến (stack) | Lưu địa chỉ trỏ đến giá trị của biến (ở heap) |
| Gắn giá trị | Không thể | Có thể |
| Tốc độ | Nhanh hơn | Chậm hơn |
| Gọi hàm trực tiếp | Không thể | Có thể |

- Ví dụ về khả năng gọi hàm trực tiếp
```Java
String s = "TYP";
System.out.print(s.length());
```

- Các wrapper class cho các kiểu nguyên thuỷ
  
| Nguyên thuỷ | Wrapper Class |
|---|---|
| `byte` | `Byte` |
| `short` | `Short` |
| `int` | `Integer` |
| `long` | `Long` |
| `float` | `Float` |
| `double` | `Double` |
| `char` | `Character` |
| `boolean` | `Boolean` |

Java có thể tự động chuyển đổi giữa wrapper class và kiểu nguyên thuỷ.

```Java
Integer x = 20;             // Integer x = Integer.valueOf(20);
int y = x * 2;              // int y = x.intValue() * 2;
int z = x + y;            
```

3. ### Sort & Comparator
- Để sort 1 array trong Java ta dùng
```Java
int[] arr = {5, 4, 2, 1, 3};
Arrays.sort(arr);
// arr = {1, 2, 3, 4, 5}
```

- Tuy nhiên với cách này, ta chỉ có thể sort ascending, để sort tuỳ ý ta cần dùng đến các comparator. Nhưng với các kiểu dữ liệu nguyên thuỷ thì ta không thể dùng comparator.
- Các cách sử dụng comparator trong Java
1. Biểu thức Lambda
2. Anonymous class

```Java
var arr = new Integer[5]; 
arr[0] = 3;
arr[1] = 7;
arr[2] = 1;
arr[3] = 2;
arr[4] = 125;
// Lambda
Comparator<Integer> acs = (a, b) -> a - b;
// Anonymous class
Comparator<Integer> desc = new Comparator<Integer>() 
{
    @Override
    public int compare(Integer a, Integer b) 
    {
        return b - a;
    }
};

Arrays.sort(arr, acs);
Arrays.sort(arr, desc);
```
```int compare(T o1, T o2)``` trả về 1 trong 3 giá trị:
| Giá trị | Ý nghĩa |
| --- | ---|
|`-1` | o1 đứng trước o2 |
| `0` | o1 == o2 |
| `1` | o2 đứng trước o1 |

4. ### HashSet, LinkedHashSet, TreeSet
- Cách khai báo tổng quát
```Java
HashSet<T> name = new HashSet<>();
Set<T> name = new HashSet<>();
var name = new HashSet<>();
```
- Cách khai báo với custom comparator đối với TreeSet
```Java
Set<T> name = new TreeSet<>(comparator_name);
```

- Cách duyệt bằng Range-Based For loop
```Java
for(Integer x : se)
    System.out.print(x + " ");
```

- Điểm chung của 3 kiểu Set là với mỗi giá trị chỉ xuất duy nhất 1 lần, đồng thời các Set đều được dựa trên các CTDL nên tốc độ các hàm tìm kiếm sẽ rất nhanh.

| | HashSet | LinkedHashSet | TreeSet |
| --- | --- | --- | --- |
| Thứ tự | Không | Thứ tự nhập vào | Thứ tự tăng dần |
| Tốc độ tìm kiếm | O(1) | O(1) | O(logN)

- Các hàm chung giữa 3 loại Set

| | |
| --- | --- |
| `add(x)` | Thêm phần tử x vào set, nếu đã tồn tại hàm trả về false. |
| `remove(x)` | Xoá phần tử x khỏi set. |
| `contains(x)` | Trả về true nếu phần tử x tồn tại trong set. |
| `clear()` | Xoá toàn bộ phần tử. |
| `isEmpty()` | Check set rỗng. |
| `size()` | Trả về size của set. |

- Một số hàm thường dùng riêng cho TreeSet

| | |
| --- | --- |
| `first()` | Trả về phần tử đầu tiên. |
| `last()` | Trả về phần tử cuối cùng. |
| `floor(x)` | Trả về phần tử lớn nhất >= x, trả về null nếu không tồn tại. |
| `ceil(x)` | Trả về phần tử bé nhất <= x, trả về null nếu không tồn tại |
| `higher(x)` | Trả về phần tử lớn nhất > x, trả về null nếu không tồn tại. |
| `lower(x)` | Trả về phần tử bé nhất < x, trả về null nếu không tồn tại |

Lưu ý: Các hàm trên sẽ trả theo thứ tự được định nghĩa bởi comparator. 

5. ### HashMap, LinkedHashMap, TreeMap
- Sự khác biệt giữa ba loại Map tương tự giống như sự khác biệt giữa ba loại Set.
- Cách khai báo tổng quát
  
```Java
Map<keyT, valueT> name = new HashMap<>();
```
- Khai báo TreeMap với custom comparator tương tự, lưu ý chỉ compare giữa key.
  
- Cách duyệt
```Java
for(Map.Entry<Integer, Integer> p : mp.entrySet())
    System.out.println(p.getKey() + " " + p.getValue());
```

- Các hàm chung giữa 3 loại map

| | |
| --- | --- |
| `put(x, y)` | Đặt phần tử cặp giá trị (x, y) vào map. |
| `replace(x, y)` | Đặt phần tử cặp giá trị (x, y) vào map với điều kiện đã có phần tử có key là x. |
| `get(x)` | Trả về value của key x. |
| `containsKey(x)` | Trả về true nếu key x tồn tại trong set. |
| `containsValue(x)` | Trả về true nếu value x tồn tại trong set O(N). |
| `remove(x)` | Xoá cặp phần tử có key x. |
| `clear()` | Xoá toàn bộ phần tử. |
| `isEmpty()` | Check map rỗng. |
| `size()` | Trả về size của map. |

- Các hàm riêng cho TreeSet
  
| | |
| --- | --- |
| `firstKey()` | Trả về key của phần tử đầu tiên. |
| `lastKey()` | Trả về key của phần tử cuối cùng. |
| `floorKey(x)` | Trả về phần tử có key lớn nhất >= x, trả về null nếu không tồn tại. |
| `ceilKey(x)` | Trả về phần tử có key bé nhất <= x, trả về null nếu không tồn tại |
| `higherKey(x)` | Trả về phần tử có key lớn nhất > x, trả về null nếu không tồn tại. |
| `lowerKey(x)` | Trả về phần tử có key bé nhất < x, trả về null nếu không tồn tại |
| `firstEntry()` | Trả về Entry của phần tử đầu tiên. | 

mỗi hàm suffix ...Key, có 1 hàm suffix ...Entry tương ứng.


# Phần 2: Database (Thiết kế lược đồ E-R)
![alt text](assets/3-1.svg)
---

Em viết file này dựa trên quá trình tự tìm hiểu các tài liệu nước ngoài kết hợp với hỗ trợ từ AI. Vì kiến thức tự học đôi khi còn hạn chế, em rất mong nhận được những nhận xét từ anh/chị để hoàn thiện hơn. Cảm ơn anh/chị đã review bài cho em. Hy vọng sớm có dịp được cộng tác và học hỏi thêm từ anh/chị!