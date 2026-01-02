# 1. Caching là gì?

Trong điện toán, **bộ nhớ đệm (cache)** là một phần cứng hoặc phần mềm lưu trữ dữ liệu tại các điểm truy cập nhanh hơn. Dữ liệu được lưu trữ trong bộ nhớ đệm có thể là kết quả của tính toán trước đó hoặc bản sao dữ liệu được lưu trữ ở nơi khác.

**Caching** là hành động thực hiện lưu trữ và quản lý dữ liệu trong cache để tối ưu hóa quá trình truy cập dữ liệu trong tương lai. Caching bao gồm việc xác định dữ liệu nào nên được lưu trữ, cũng như quản lý việc lưu trữ và cập nhật dữ liệu đó trong cache để đảm bảo dữ liệu luôn được cập nhật và phản ánh chính xác trạng thái hiện tại.

Caching có thể được áp dụng ở nhiều cấp độ khác nhau, bao gồm **phần cứng** (ví dụ, _cache CPU_), **hệ điều hành**, **ứng dụng web** và **cơ sở dữ liệu**. Ví dụ, trong phát triển web, caching có thể được sử dụng để lưu trữ các trang web tĩnh, kết quả truy vấn cơ sở dữ liệu, hoặc các đối tượng dữ liệu thường xuyên được truy cập để giảm thời gian tải trang và giảm tải cho máy chủ.

# 2. Vì sao cần Caching/Redis?

Trong các hệ thống phần mềm, đặc biệt là hệ thống web và ứng dụng có nhiều người dùng, việc truy cập dữ liệu từ nguồn gốc như cơ sở dữ liệu (database), dịch vụ API bên ngoài hoặc thực hiện các phép tính phức tạp thường tốn thời gian và tài nguyên. Khi nhiều người dùng cùng truy cập một loại dữ liệu lặp đi lặp lại (ví dụ: danh sách sản phẩm, thông tin hồ sơ người dùng, cấu hình hệ thống, kết quả truy vấn phổ biến), hệ thống sẽ phải thực hiện lại cùng một thao tác nhiều lần. Điều này làm tăng độ trễ phản hồi và tạo áp lực lớn lên database.

Caching được sử dụng để giải quyết vấn đề trên bằng cách lưu tạm thời dữ liệu hoặc kết quả xử lý đã có sẵn vào bộ nhớ đệm. Khi có yêu cầu truy cập lại dữ liệu đó, hệ thống có thể trả kết quả trực tiếp từ cache thay vì truy vấn lại database hoặc tính toán lại từ đầu. Nhờ vậy, thời gian phản hồi được cải thiện rõ rệt và hệ thống hoạt động hiệu quả hơn, đặc biệt trong các tình huống tải cao.

Redis là một lựa chọn phổ biến để triển khai caching vì Redis hoạt động theo mô hình lưu trữ dữ liệu trong bộ nhớ (in-memory) nên tốc độ đọc/ghi rất nhanh. Bên cạnh đó, Redis có thể đóng vai trò như một cache dùng chung cho nhiều máy chủ ứng dụng. Điều này quan trọng trong các hệ thống triển khai theo mô hình nhiều server (scale-out), bởi nếu chỉ cache trong RAM của từng server riêng lẻ thì dữ liệu cache sẽ khó đồng bộ, và khi server khởi động lại thì cache cũng mất, gây giảm hiệu quả.

Một ưu điểm quan trọng khác của Redis là hỗ trợ cơ chế TTL (Time To Live), cho phép dữ liệu trong cache tự động hết hạn sau một khoảng thời gian. Điều này giúp hạn chế tình trạng dữ liệu bị “cũ” quá lâu và giảm công sức quản lý thủ công. Ngoài caching, Redis còn thường được sử dụng cho các mục đích khác như lưu session đăng nhập, giới hạn tần suất truy cập (rate limiting), hàng đợi (queue), pub/sub thông báo thời gian thực, hoặc cơ chế khóa phân tán (distributed lock), nhờ vào các cấu trúc dữ liệu linh hoạt và hiệu năng cao.

Tóm lại, caching và Redis là các giải pháp quan trọng giúp hệ thống tăng tốc phản hồi, giảm tải cho database và nâng cao khả năng chịu tải khi lượng truy cập tăng. Việc áp dụng caching/Redis đúng cách đặc biệt hữu ích trong các bài toán “đọc nhiều hơn ghi”, nơi dữ liệu được truy cập lặp lại thường xuyên và có thể chấp nhận chênh lệch cập nhật trong một khoảng thời gian ngắn.

# 3. Các chiến lược Caching phổ biến?

Các chiến lược caching được chia thành hai loại chính:

- Chiến lược đọc dữ liệu: bao gồm Cache aside và Read through.

- Chiến lược ghi dữ liệu: bao gồm Write around, Write back và Write through.

## 3.1. Cache aside

Chiến lược "Cache aside", còn được biết đến với tên gọi "Lazy loading" hay "Load-through cache". Mô hình này đặc biệt hữu ích trong các trường hợp mà việc cập nhật dữ liệu không quá thường xuyên, nhưng yêu cầu truy cập dữ liệu nhanh chóng.

Cách hoạt động cơ bản của chiến lược Cache aside:

Bước 1: Kiểm tra cache
Nếu dữ liệu có trong cache, dữ liệu sẽ được trả về từ cache, giúp giảm thiểu độ trễ và tải trên hệ thống lưu trữ chính hoặc cơ sở dữ liệu. Ngược lại, nếu dữ liệu không có trong cache, hệ thống sẽ thông báo tới ứng dụng để tiếp tục với bước tiếp theo.

Bước 2: Truy cập dữ liệu từ nguồn chính
Nếu dữ liệu yêu cầu không có trong cache, hệ thống sẽ truy cập dữ liệu từ nguồn dữ liệu chính, thường là một cơ sở dữ liệu hoặc một dịch vụ dữ liệu bên ngoài.

Bước 3: Cập nhật Cache
Sau khi dữ liệu được truy cập từ nguồn chính, hệ thống sẽ lưu trữ (cache) dữ liệu đó cho các yêu cầu trong tương lai và trả về dữ liệu cho người dùng hoặc ứng dụng yêu cầu. Việc này đảm bảo rằng dữ liệu sẽ được nhanh chóng truy cập trong lần yêu cầu tiếp theo mà không cần phải truy cập lại nguồn dữ liệu chính.

Bước 4: Xử lý dữ liệu lỗi thời
Dữ liệu trong cache cần được cập nhật hoặc loại bỏ khi nó không còn đồng bộ với dữ liệu trong nguồn chính (bị lỗi thời). Điều này thường được quản lý thông qua cơ chế hết hạn (TTL - Time to Live) hoặc bằng cách chủ động loại bỏ cache khi dữ liệu nguồn được cập nhật.

Ưu điểm của chiến lược cache aside:

Tiết kiệm tài nguyên: Cache-aside chỉ lưu trữ dữ liệu vào cache khi cần thiết, giảm thiểu việc sử dụng tài nguyên bộ nhớ cho dữ liệu không được truy cập thường xuyên.
Tính linh hoạt: Chiến lược này cho phép tự do quản lý việc đưa dữ liệu vào và lấy dữ liệu ra khỏi cache, giúp tối ưu hóa hiệu suất và sử dụng tài nguyên.
Dễ triển khai: Cache-aside là một phương pháp đơn giản và dễ triển khai, không đòi hỏi nhiều công sức để tích hợp vào các hệ thống phần mềm.
Nhược điểm của chiến lược cache aside:

Hiện tượng đọc không đồng nhất: Cần phải tự quản lý việc đồng bộ hóa dữ liệu giữa cache và nguồn dữ liệu chính. Có thể xảy ra khi dữ liệu trong cache bị cũ hoặc không được đồng bộ với nguồn gốc, dẫn đến sự không nhất quán giữa các phiên bản của dữ liệu.
Rủi ro về độ trễ: Khi dữ liệu không có sẵn trong cache, việc truy vấn từ nguồn gốc có thể tạo ra độ trễ đáng kể, ảnh hưởng đến thời gian phản hồi của hệ thống.
Trường hợp sử dụng: Web caching cho các trang web hoặc dịch vụ API có lượng truy cập cao nhưng dữ liệu không thay đổi thường xuyên.

Ví dụ: Một trang web tin tức có thể sử dụng cache aside để lưu trữ các bài báo được truy cập nhiều, giảm tải cho cơ sở dữ liệu bằng cách tránh truy vấn lặp lại cho cùng một nội dung.

## 3.2. Read through

Chiến lược Read through là một mô hình caching được thiết kế để tự động hóa quá trình tải và lưu trữ dữ liệu vào cache. Khác với chiến lược "Cache Aside", nơi ứng dụng phải chủ động kiểm tra cache, sau đó tải dữ liệu vào cache nếu nó không tồn tại, chiến lược Read through sử dụng một lớp trung gian (thường là một cache proxy hoặc cache library) để tự động xử lý việc tải dữ liệu vào cache.

Các bước hoạt động của chiến lược read through bao gồm:

Bước 1: Yêu cầu dữ liệu
Khi một ứng dụng cần truy cập dữ liệu, nó sẽ yêu cầu dữ liệu thông qua lớp cache thay vì trực tiếp từ nguồn dữ liệu chính (ví dụ: cơ sở dữ liệu).

Bước 2: Kiểm tra cache
Nếu dữ liệu có sẵn trong cache, cache sẽ trả về dữ liệu ngay lập tức cho ứng dụng, giảm thiểu độ trễ và tải trên nguồn dữ liệu chính. Ngược lại, nếu dữ liệu không có trong cache, Quá trình tiếp theo sẽ được kích hoạt.

Bước 3: Tải dữ liệu từ nguồn chính
Nếu dữ liệu yêu cầu không tồn tại trong cache, lớp trung gian sẽ tự động truy cập nguồn dữ liệu chính để lấy dữ liệu.

Bước 4: Cập nhật cache
Sau khi dữ liệu được lấy từ nguồn chính, nó sẽ được tự động lưu vào cache. Điều này đảm bảo rằng trong lần truy cập tiếp theo, dữ liệu có thể được trả về ngay lập tức từ cache mà không cần truy cập lại nguồn chính.

Bước 5: Trả về dữ liệu
Dữ liệu sau khi đã được lưu vào cache sẽ được trả về cho ứng dụng, giống như nó được trả về từ cache trong trường hợp dữ liệu đã sẵn có.

Ưu điểm của chiến lược Read through:

Tự động cập nhật cache: Hệ thống tự động đọc dữ liệu từ nguồn gốc và cập nhật vào cache mỗi khi cần thiết, giúp đảm bảo tính nhất quán của dữ liệu trong cache.
Giảm thiểu lỗi thời của dữ liệu: Bằng cách tự động cập nhật cache, chiến lược này giúp giảm thiểu tình trạng dữ liệu lỗi thời trong cache.
Giảm công sức quản lý: Không cần phải quản lý việc cập nhật cache một cách thủ công.
Nhược điểm của chiến lược Read through:

Cần có lớp trung gian: Cần có lớp trung gian hoặc thư viện hỗ trợ, có thể làm tăng độ phức tạp của hệ thống.
Trường hợp sử dụng: Ứng dụng cần tự động hóa việc tải dữ liệu vào cache, đảm bảo dữ liệu luôn sẵn có trong cache khi cần.

Ví dụ: Một hệ thống quản lý hàng tồn kho tự động cập nhật cache khi có yêu cầu dữ liệu về số lượng hàng, đảm bảo thông tin được truy cập nhanh chóng mà không cần truy vấn cơ sở dữ liệu mỗi lần.

## 3.3. Write around

Trong chiến lược Write around, khi dữ liệu mới được viết, nó được ghi trực tiếp vào nguồn dữ liệu chính mà không cập nhật dữ liệu đó vào cache ngay lập tức.

Bước 1: Yêu cầu ghi dữ liệu
Dữ liệu mới hoặc cập nhật được ghi trực tiếp vào nguồn dữ liệu chính, bỏ qua việc ghi vào cache.

Bước 2: Bỏ qua cache
Không cập nhật cache ngay sau khi ghi, giúp giảm tải trên bộ nhớ cache và tăng hiệu suất ghi dữ liệu.

Bước 3: Yêu cầu đọc dữ liệu
Khi có yêu cầu đọc dữ liệu, hệ thống kiểm tra trong cache. Nếu không có, dữ liệu mới từ nguồn chính sẽ được tải vào cache.

Bước 4: Cập nhật cache
Dữ liệu được cập nhật vào cache từ nguồn chính, sẵn sàng cho các yêu cầu đọc tiếp theo.

Ưu điểm của chiến lược Write around:

Giảm độ trễ ghi: Giảm bớt việc ghi dữ liệu không cần thiết vào cache bằng cách loại bỏ bước đồng bộ hóa dữ liệu giữa cache và nguồn dữ liệu chính, tiết kiệm tài nguyên cache cho dữ liệu thường xuyên được truy cập.
Nhược điểm của chiến lược Write around:

Rủi ro về độ trễ đọc: Có thể gây ra độ trễ khi đọc dữ liệu lần đầu tiên sau khi nó được ghi, do dữ liệu phải được tải từ nguồn dữ liệu chính vào cache.
Trường hợp sử dụng: Hệ thống cần tối ưu hiệu suất ghi, giảm bớt việc sử dụng cache cho dữ liệu không thường xuyên được truy cập.

Ví dụ: Một hệ thống lưu trữ dữ liệu lớn, nơi dữ liệu mới được ghi vào cơ sở dữ liệu nhưng chỉ được cache khi có yêu cầu đọc, nhằm tối ưu hóa không gian cache và hiệu suất.

## 3.4. Write back

Chiến lược Write back (còn được gọi là Write behind) là chiến lược caching mà dữ liệu được viết vào cache trước và sau đó được đồng bộ hóa với nguồn dữ liệu chính sau một khoảng thời gian nhất định hoặc dựa trên một sự kiện nhất định.

Các bước hoạt động của chiến lược Write back bao gồm:

Bước 1: Yêu cầu ghi dữ liệu
Khi có yêu cầu ghi dữ liệu, dữ liệu mới hoặc được cập nhật được ghi vào cache trước.

Bước 2: Đánh dấu dữ liệu
Dữ liệu trong cache được đánh dấu là "đã thay đổi" hoặc "dirty", để biểu thị rằng nó chưa được đồng bộ hóa với nguồn dữ liệu chính.

Bước 3: Đồng bộ hóa dữ liệu
Dữ liệu được đồng bộ hóa với nguồn dữ liệu chính dựa trên một chính sách định kỳ hoặc sự kiện cụ thể (ví dụ, cache đầy hoặc sau một khoảng thời gian nhất định).

Bước 4: Xác nhận đồng bộ
Sau khi dữ liệu được đồng bộ hóa thành công với nguồn dữ liệu chính, dấu "dirty" được gỡ bỏ.

Ưu điểm của chiến lược Write back:

Giảm độ trễ ghi: Cải thiện đáng kể hiệu suất ghi bằng cách giảm độ trễ, vì việc ghi vào cache nhanh hơn nhiều so với ghi vào nguồn dữ liệu chính.
Tối ưu hóa tài nguyên: Giảm tải trên nguồn dữ liệu chính, cho phép hệ thống xử lý hiệu quả hơn các yêu cầu khác.
Nhược điểm của chiến lược Write back:

Rủi ro mất dữ liệu: Nếu hệ thống gặp sự cố trước khi dữ liệu được đồng bộ hóa, có nguy cơ mất dữ liệu chưa được lưu trữ vào nguồn dữ liệu chính.
Quản lý phức tạp: Yêu cầu cơ chế quản lý cache và đồng bộ hóa dữ liệu hiệu quả để đảm bảo tính nhất quán và độ tin cậy của dữ liệu.
Trường hợp sử dụng: Ứng dụng đòi hỏi hiệu suất ghi cao và có thể chấp nhận được độ trễ trong việc đồng bộ hóa dữ liệu với nguồn dữ liệu chính.

Ví dụ: Hệ thống ghi nhật ký (logging) nơi dữ liệu được ghi nhanh chóng vào cache và sau đó đồng bộ hóa với cơ sở dữ liệu log một cách định kỳ, giảm độ trễ ghi và tối ưu hóa hiệu suất.

## 3.5 Write through

Chiến lược Write Through là một kỹ thuật quản lý cache trong đó dữ liệu được ghi vào cả cache và nguồn dữ liệu chính đồng thời.

Các bước hoạt động của chiến lược Write through bao gồm:

Bước 1: Yêu cầu ghi dữ liệu
Khi có yêu cầu ghi dữ liệu, hệ thống sẽ thực hiện ghi dữ liệu vào cả cache và nguồn dữ liệu chính. Quá trình này đảm bảo rằng dữ liệu trong cache luôn đồng bộ với nguồn dữ liệu chính.

Bước 2: Ghi vào cache
Dữ liệu mới hoặc được cập nhật ghi vào cache. Điều này giúp đảm bảo rằng các yêu cầu đọc tiếp theo cho dữ liệu đó sẽ truy cập nhanh chóng từ cache mà không cần phải truy vấn nguồn dữ liệu chính.

Bước 3: Ghi vào nguồn dữ liệu chính
Đồng thời, dữ liệu cũng được ghi vào nguồn dữ liệu chính. Điều này đảm bảo tính nhất quán và độ tin cậy của dữ liệu, vì mọi thay đổi đều được phản ánh ngay lập tức trong cả hai vị trí.

Trường hợp sử dụng: Các ứng dụng cần đảm bảo dữ liệu luôn được cập nhật và đồng bộ giữa cache và nguồn dữ liệu chính một cách ngay lập tức.

Ví dụ: Hệ thống xử lý giao dịch tài chính, nơi cần đảm bảo dữ liệu giao dịch được cập nhật tức thì vào cả cache và cơ sở dữ liệu để đảm bảo tính nhất quán và độ tin cậy của dữ liệu.

# 4. Kiểu dữ liệu cơ bản trong Redis

Trong Redis, **key** luôn được lưu dưới dạng **string**, còn **value** có thể thuộc nhiều kiểu dữ liệu khác nhau. Việc Redis hỗ trợ nhiều kiểu dữ liệu giúp hệ thống lưu trữ và thao tác dữ liệu theo đúng bài toán, từ đó tăng hiệu năng và giảm độ phức tạp khi xử lý.

Kiểu dữ liệu **String** là kiểu đơn giản và phổ biến nhất trong Redis. String có thể lưu chuỗi văn bản, số, hoặc dữ liệu dạng nhị phân. String thường được dùng để cache nội dung (ví dụ JSON/HTML), lưu token phiên đăng nhập, hoặc làm bộ đếm (counter) nhờ các thao tác tăng/giảm trực tiếp.

Kiểu dữ liệu **Hash** lưu dữ liệu theo dạng các cặp **field–value**, tương tự như một đối tượng (object) hoặc một bản ghi. Hash phù hợp để lưu thông tin có nhiều thuộc tính như user, sản phẩm, cấu hình,… vì có thể cập nhật từng thuộc tính riêng lẻ mà không cần ghi đè toàn bộ dữ liệu.

Kiểu dữ liệu **List** là một danh sách có thứ tự, cho phép thêm/xóa phần tử ở đầu hoặc cuối danh sách. List thường được sử dụng để triển khai các hàng đợi (queue) đơn giản, danh sách công việc, hoặc lưu log theo thứ tự thời gian bằng cách thêm phần tử liên tục.

Kiểu dữ liệu **Set** là một tập hợp **không cho phép trùng lặp** và không quan tâm đến thứ tự. Set phù hợp để lưu các tập dữ liệu cần đảm bảo tính duy nhất, chẳng hạn danh sách người dùng đã tương tác (like/follow), danh sách tag, hoặc kiểm tra một phần tử có thuộc tập hợp hay không.

Kiểu dữ liệu **Sorted Set (ZSet)** tương tự Set nhưng mỗi phần tử đi kèm một **score** (điểm) để sắp xếp theo thứ tự. ZSet rất phù hợp cho các bài toán cần xếp hạng và truy vấn theo thứ tự như leaderboard, top N sản phẩm, hoặc sắp xếp theo mức độ ưu tiên/thời gian.

Ngoài các kiểu cơ bản trên, Redis còn hỗ trợ một số kiểu dữ liệu mở rộng thường gặp. **Stream** phù hợp cho luồng sự kiện và xử lý theo mô hình consumer group. **Bitmap** hỗ trợ thao tác theo bit để tối ưu bộ nhớ cho các bài toán đánh dấu trạng thái. **HyperLogLog** giúp ước lượng số lượng phần tử khác nhau (unique) với bộ nhớ nhỏ, phù hợp để thống kê lượng truy cập lớn. Bên cạnh đó, Redis còn hỗ trợ **Geospatial** để lưu tọa độ và truy vấn theo khoảng cách, thường dùng cho các chức năng tìm kiếm địa điểm gần nhất.

Tóm lại, Redis cung cấp nhiều kiểu dữ liệu để đáp ứng đa dạng nhu cầu lưu trữ và xử lý. Việc lựa chọn đúng kiểu dữ liệu sẽ giúp hệ thống tối ưu cả về hiệu năng, bộ nhớ và tính rõ ràng trong thiết kế.
