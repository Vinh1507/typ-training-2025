# Báo cáo Tuần 3

## 1. Ôn tập

### Mảng 1 Chiều (1D Array)

- Truy cập: Dùng 1 chỉ số (index). Lưu ý: Chỉ số luôn bắt đầu từ 0.

- Cú pháp:

    ```java
    // Khai báo và khởi tạo kích thước
    int[] numbers = new int[5]; // Có 5 phần tử, từ index 0 đến 4

    // Khai báo và gán giá trị ngay
    String[] names = {"Alice", "Bob", "Charlie"};
    ```
### Mảng 2 Chiều (2D Array)

- Truy cập: Dùng 2 chỉ số (hàng, cột).

- Cú pháp:

    ```java
   // Khai báo và khởi tạo kích thước (3 hàng, 4 cột)
    int[][] matrix = new int[3][4];

    // Khai báo và gán giá trị ngay
    String[][] board = {
        {"X", "O", "X"},
        {"O", "X", "O"},
        {"X", "O", "O"}
    };
    ```
### Vòng lặp for
- Khi nào dùng: Khi bạn biết rõ số lần lặp (ví dụ: lặp 10 lần, lặp qua tất cả phần tử của mảng).

- Cú pháp:
    ```java
    // for (khởi tạo; điều kiện; bước nhảy)
    for (int i = 0; i < 5; i++) {
        System.out.println(i); // In ra 0, 1, 2, 3, 4
    }

    // for-each (dùng để duyệt mảng/collection)
    for (String name : names) {
        System.out.println(name);
    }
    ```
### Vòng lặp while
- Khi nào dùng: Khi bạn không biết trước số lần lặp, chỉ biết điều kiện dừng.

- Logic: Kiểm tra điều kiện TRƯỚC, rồi mới chạy.

- Lưu ý: Có thể không chạy lần nào nếu điều kiện sai ngay từ đầu.

- Cú pháp:

    ```java
    int count = 0;
    while (count < 3) {
        System.out.println("Hello");
        count++;
    }
    ```
### Vòng lặp do-while
- Khi nào dùng: Giống while, nhưng bạn muốn khối lệnh được chạy ít nhất 1 lần bất kể điều kiện đúng hay sai.

- Logic: Chạy 1 lần TRƯỚC, rồi mới kiểm tra điều kiện SAU.

- Cú pháp:

    ```java
    int number;
    do {
        System.out.println("Nhập một số (nhập 0 để thoát): ");
    } while (number != 0);
    ```
### Bổ sung

    - `break`: thoát khỏi vòng lạp ngay lập tức.
    - `continue`: Bỏ qua phần còn lại của lần lặp hiện tại và nhảy sang lần lặp tiếp theo 

## 2. Sort & Comparator

- **Đ/N**:
    - Sort: Là hành động sắp xếp (thường là phương thức `Collections.sort()` hoặc `Arrays.sort()`).

    - Comparator: Là bộ quy tắc (logic) mà bạn cung cấp cho hành động `Sort` để nó biết cách sắp xếp.

- **Sort**: Trong java có 2 phương thức `sort`.

    - `Collections.sort(List<T> list)`: Dùng để sắp xếp các List (như ArrayList).
    - `Arrays.sort(T[] array)`: Dùng để sắp xếp mảng.

    ```java
    List<Integer> numbers = Arrays.asList(5, 1, 3);
    Collections.sort(numbers); // Java tự biết sắp xếp số theo thứ tự tăng dần
    // numbers bây giờ là [1, 3, 5]

    List<String> names = Arrays.asList("Bob", "Alice", "Charlie");
    Collections.sort(names); // Java tự biết sắp xếp chuỗi theo vần ABC
    // names bây giờ là ["Alice", "Bob", "Charlie"]
    ```
- **Comparator**:
    - Để sắp xếp các class do chính mình định nghĩa ta cần dùng `comparator`.
    - Là một interface (một bản hợp đồng) yêu cầu bạn phải định nghĩa một phương thức tên là compare(). Phương thức này nhận vào 2 đối tượng (s1 và s2) và trả về một số nguyên (int) để cho biết s1 nên đứng trước, đứng sau, hay ngang hàng với s2.

        - Trả về số âm (-1): s1 đứng trước s2.

        - Trả về số dương (+1): s1 đứng sau s2.

        - Trả về số 0: s1 và s2 ngang hàng (giữ nguyên vị trí).
    
    - **Cách 1** (Anonymous inner class) 

    ```java
        Collections.sort(list, new Comparator<SinhVien>(){
            @Override
            public int compare(SinhVien o1,SinhVien o2) {
                // Lớp String có sẵn phương thức compareTo để so sánh chuỗi
                return o2.getHoTen().compareTo(o2.getHoTen());  
            }
        })
    ```

    - **Cách 2** (Biểu thứ lambda, Java 8+)

    ```java
        Collections.sort(list, (SinhVien o1, SinhVien o2) -> o2.getHoTen().compareTo(o2.getHoTen()));
    ```

    - **Cách 3** Tạo một lớp Comparator riêng

    ```java
    import java.util.Comparator;

    class SortByGpa implements Comparator<SinhVien> {
        @Override
        public int compare(SinhVien a, SinhVien b) {
            if(a.getGpa() > b.getGpa()) return -1;
            else return 1;
        }
    }
    ```
    Sử dụng

    ```java
        Collections.sort(list, new SortByGpa());
    ```


## 3. Set, Map



- ### Set

    - **Đ/N** : Là cấu trúc dữ liệu dùng để lưu trữ các phần tử và xử lý các phần tử không trùng nhau.

    - **Dặc điểm** :
        - Không thể lưu các phàn tử trùng nhau, nếu cố tình thêm thao tác `add()` sẽ trả về false vàng `Set` không thay đổi.
        - Không có chỉ số: chỉ có thể duyệt qua hoặc kiểm tra xem 1 phần tử có tồn tại hay không.
        - Thứ tự: Đa số `Set` không đẩm bảo về thứ tự. `HashSet` không đẩm bảo bất kì thứ tự nào. Tuy nhiên vẫn có `Set` khác đặc biết sẽ duy trì thứ tự.

    - **Các thao tác cơ bản**:

        - `mySet.add(element)` : Thêm một phần tử trả về `True` nếu thêm thàn công, `False` nếu thêm thất bại.

        - `mySet.remove(element)`: Xóa một phần tử.

        - `mySet.contains(element)`: Nó kiểm tra xem một phần tử có tồn tại trong Set hay không (trả về true/false).

        - `mySet.size()`: Trả về số lượng phần tử.

        - `mySet.isEmpty()`: Kiểm tra xem Set có rỗng không.

        - `mySet.clear()`: Xóa tất cả phần tử.

        - `mySet1.removeAll(mySet2)`: Xóa tất cả phần tử `mySet2` tồn tại trong `mySet1`.

        - `mySet1.retainAll(mySet2)`: Tìm các phần tử giao giữa 2 tập hợp.

        - `mySet1.addAll(mySet2)`: Tìm các phần tử hợp giữa 2 tập hợp.



    - **Các lớp triển khai (implementations):**

    3.1. `HashSet` (Túi lộn xộn - **Nhanh nhất**)

    - **Thứ tự**: Không đảm bảo thứ tự. Các phần tử lộn xộn.
    
    - **Hiệu năng**: Cực kỳ nhanh cho các thao tác add, remove, và contains (thời gian $O(1)$ - không đổi).
    
    - **Cách hoạt động**: Nó sử dụng một cơ chế gọi là "băm" (hashing) để lưu trữ các phần tử, cho phép tìm kiếm siêu nhanh.
    
    - **Khi nào dùng**: Khi chỉ cần biết một thứ gì đó có tồn tại hay không, và không quan tâm đến thứ tự.

    ```java
        public static void main(String[] args) {
            HashSet<Integer> set = new HashSet<>(); // Khai báo 1 set

            set.add(1);
            set.add(2);
            set.add(3);
            set.add(1);
            set.add(4);
            set.add(4);
            // 1 2 3 4

            System.out.println(set.size());

            for(int x : set){
                System.out.println(x + " ");
            }
            Iterator it = set.iterator();
            while(it.hasNext()){
                System.out.print(it.next() + " ");
            }
        }
    ```
    3.2. `TreeSet` (Túi đã sắp xếp - **Chậm hơn**)

    - **Thứ tự**: Đã sắp xếp! Các phần tử luôn được duy trì theo thứ tự "tự nhiên" (số từ nhỏ đến lớn, chữ cái theo vần ABC) hoặc theo một Comparator (bộ quy tắc).
        
    - **Hiệu năng**: Chậm hơn HashSet một chút (thời gian $O(\log n)$) vì nó phải duy trì cấu trúc cây để giữ thứ tự.
        
    - **Cách hoạt động**: Nó được xây dựng dựa trên cấu trúc Cây Đỏ-Đen (Red-Black Tree).
        
    - **Khi nào dùng**: Khi cần một bộ sưu tập không trùng lặp và luôn được sắp xếp.

     ```java
        public static void main(String[] args) {
            TreeSet<Integer> set = new TreeSet<>();

            set.add(4);
            set.add(2);
            set.add(1);
            set.add(3);
            // 1 2 3 4
        }
    ```

    3.3. `LinkedHashSet` (Túi ngăn nắp - **Nhanh**)

    Đây là sự kết hợp hoàn hảo giữa HashSet và TreeSet.
    
    - **Thứ tự**: Duy trì thứ tự đã chèn vào (insertion order).
    
    - **Hiệu năng**: Gần như nhanh bằng HashSet (thời gian $O(1)$).
    
    - **Cách hoạt động**: Nó là một HashSet nhưng có thêm một danh sách liên kết (linked list) chạy ngầm bên dưới để "nhớ" thứ tự.
    
    - **Khi nào dùng**: Khi cần tốc độ của HashSet nhưng vẫn muốn các phần tử giữ nguyên thứ tự khi duyệt qua chúng.

     ```java
        public static void main(String[] args) {
            LinkedHashSet<Integer> set = new LinkedHashSet<>();

            set.add(4);
            set.add(2);
            set.add(1);
            set.add(3);
            // 4 2 1 3
        }
    ```
#### Tổng kết:

    - Nhanh nhất, không quan tâm thứ tự?  :  `HashSet`
    - Các phần tử luôn được sắp xếp (A-Z, 1-10)? :  `TreeSet`
    - Các phần tử giữ nguyên thứ tự đã thêm vào?  :  `LinkedHashSet`.

### Map

- **Đ/N** : Là cấu trúc dữ liệu dùng để lưu trữ các phần tử dưới dạng các cặp **Key-Value (Khóa-Giá trị)**. Nó giống như một cuốn từ điển.

- **Đặc điểm** :
    - `Map` không cho phép các **Khóa (Key) trùng lặp**.
    - Nếu cố tình thêm một `Key` đã tồn tại, thao tác `put()` sẽ **cập nhật** giá trị (Value) cũ bằng giá trị mới.
    - **Giá trị (Value) có thể trùng lặp**.
    - Không có chỉ số: Truy cập phần tử thông qua `Key` (khóa), không phải qua chỉ số `index`.
    - Thứ tự: Tùy thuộc vào lớp triển khai. `HashMap` không đảm bảo bất kỳ thứ tự nào.

- **Các thao tác cơ bản**:

    - `myMap.put(Key, Value)` : Thêm hoặc cập nhật một cặp Key-Value. Nếu `Key` là mới, nó trả về `null`. Nếu `Key` đã tồn tại, nó trả về giá trị (Value) cũ trước khi cập nhật.

    - `myMap.get(Key)`: Lấy ra Giá trị (Value) dựa vào Khóa (Key). Trả về `null` nếu không tìm thấy Key.

    - `myMap.remove(Key)`: Xóa một cặp Key-Value dựa vào Khóa.

    - `myMap.containsKey(Key)`: Kiểm tra xem một Khóa có tồn tại trong `Map` hay không (trả về `true`/`false`).

    - `myMap.containsValue(Value)`: Kiểm tra xem một Giá trị có tồn tại trong `Map` hay không.

    - `myMap.size()`: Trả về số lượng cặp Key-Value.

    - `myMap.isEmpty()`: Kiểm tra xem `Map` có rỗng không.

    - `myMap.clear()`: Xóa tất cả các cặp Key-Value.

    - `myMap.keySet()`: Trả về một `Set` chứa tất cả các Khóa (Keys) từ `Map`.

    - `myMap.values()`: Trả về một `Collection` chứa tất cả các Giá trị (Values) từ `Map`.

    - `myMap.entrySet()`: Trả về một `Set` chứa tất cả các mục (Entry - là các cặp Key-Value). Đây là cách phổ biến để duyệt qua một `Map`.

- **Các lớp triển khai (implementations):**

    3.1. `HashMap` (Bảng băm - **Nhanh nhất**)

    - **Thứ tự**: Không đảm bảo thứ tự. Các phần tử lộn xộn.
    
    - **Hiệu năng**: Cực kỳ nhanh cho các thao tác `put`, `get`, và `containsKey` (thời gian $O(1)$ - không đổi).
    
    - **Cách hoạt động**: Nó sử dụng một cơ chế gọi là "băm" (hashing) dựa trên `Key` để lưu trữ, cho phép tìm kiếm siêu nhanh.
    
    - **Khi nào dùng**: Khi cần tra cứu nhanh và **không quan tâm** đến thứ tự.

    ```java
        public static void main(String[] args) {
            HashMap<String, Integer> map = new HashMap<>(); // Khai báo 1 map

            map.put("Alice", 90);
            map.put("Bob", 85);
            map.put("Charlie", 95);
            map.put("Alice", 100); // Cập nhật giá trị của Alice
            
            // "Alice" là Key duy nhất, "100" là Value
            // "Bob" là Key duy nhất, "85" là Value
            // "Charlie" là Key duy nhất, "95" là Value

            System.out.println(map.size()); // Kết quả: 3
            System.out.println(map.get("Bob")); // Kết quả: 85

            // Duyệt Map bằng cách dùng entrySet
            for(Map.Entry<String, Integer> entry : map.entrySet()){
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    ```
    3.2. `TreeMap` (Map Cây - **Đã sắp xếp**)

    - **Thứ tự**: Đã sắp xếp! Các mục (entry) luôn được duy trì theo thứ tự "tự nhiên" của **Khóa (Key)** (số từ nhỏ đến lớn, chữ cái theo vần ABC) hoặc theo một `Comparator`.
        
    - **Hiệu năng**: Chậm hơn `HashMap` một chút (thời gian $O(\log n)$) vì nó phải duy trì cấu trúc cây để giữ thứ tự.
        
    - **Cách hoạt động**: Nó được xây dựng dựa trên cấu trúc Cây Đỏ-Đen (Red-Black Tree).
        
    - **Khi nào dùng**: Khi cần một `Map` mà các **Khóa (Key)** luôn được sắp xếp.

    ```java
        public static void main(String[] args) {
            TreeMap<String, Integer> map = new TreeMap<>();

            map.put("Charlie", 95);
            map.put("Alice", 100);
            map.put("Bob", 85);
            
            // Khi duyệt, các Key sẽ được sắp xếp: Alice, Bob, Charlie
            for(String key : map.keySet()){
                System.out.println(key + ": " + map.get(key));
            }
        }
    ```

    3.3. `LinkedHashMap` (Map liên kết - **Giữ thứ tự chèn**)

    Đây là sự kết hợp hoàn hảo giữa `HashMap` và `TreeMap` về thứ tự.
    
    - **Thứ tự**: Duy trì thứ tự đã chèn vào (insertion order).
    
    - **Hiệu năng**: Gần như nhanh bằng `HashMap` (thời gian $O(1)$).
    
    - **Cách hoạt động**: Nó là một `HashMap` nhưng có thêm một danh sách liên kết kép (doubly-linked list) chạy ngầm bên dưới để "nhớ" thứ tự chèn.
    
    - **Khi nào dùng**: Khi cần tốc độ của `HashMap` nhưng vẫn muốn các phần tử giữ nguyên thứ tự khi duyệt qua chúng.

    ```java
        public static void main(String[] args) {
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

            map.put("Charlie", 95);
            map.put("Alice", 100);
            map.put("Bob", 85);
            
            // Khi duyệt, sẽ ra thứ tự chèn vào: Charlie, Alice, Bob
            for(Map.Entry<String, Integer> entry : map.entrySet()){
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    ```