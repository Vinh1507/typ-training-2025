import requests
import concurrent.futures
import time

# CẤU HÌNH TẤN CÔNG
API_URL = "http://127.0.0.1:8000/api/orders/buy"
TOTAL_REQUESTS = 500  # Số lượng đơn muốn mua
CONCURRENT_THREADS = 50  # Số luồng chạy song song (giả lập 50 người bấm cùng 1 lúc liên tục)

# Dữ liệu mua hàng (Giả sử user 'admin' mua sản phẩm ID 4)
payload = {
    "username": "admin",
    "product_id": "4"
}


def send_buy_request(index):
    try:
        response = requests.post(API_URL, json=payload)
        if response.status_code == 200:
            res_json = response.json()
            if res_json.get("status") == "success":
                return "SUCCESS"
            else:
                return "FAILED_LOGIC"
        else:
            return f"ERROR_{response.status_code}"
    except Exception as e:
        return "ERROR_CONNECTION"


def main():
    print(f"🚀 BẮT ĐẦU STRESS TEST: {TOTAL_REQUESTS} requests...")
    print(f"🔥 Target: {API_URL}")

    start_time = time.time()

    success_count = 0
    fail_count = 0
    error_count = 0

    with concurrent.futures.ThreadPoolExecutor(max_workers=CONCURRENT_THREADS) as executor:
        futures = [executor.submit(send_buy_request, i) for i in range(TOTAL_REQUESTS)]

        for i, future in enumerate(concurrent.futures.as_completed(futures)):
            result = future.result()
            if result == "SUCCESS":
                success_count += 1
            elif result == "FAILED_LOGIC":
                fail_count += 1
            else:
                error_count += 1

            if (i + 1) % 1000 == 0:
                print(f"   ⏳ Đã gửi {i + 1}/{TOTAL_REQUESTS} requests...")

    end_time = time.time()
    duration = end_time - start_time

    print("\n" + "=" * 40)
    print("📊 KẾT QUẢ KIỂM TRA CHỊU TẢI")
    print("=" * 40)
    print(f"⏱  Thời gian chạy: {duration:.2f} giây")
    print(f"⚡ Tốc độ trung bình: {TOTAL_REQUESTS / duration:.0f} req/s")
    print("-" * 20)
    print(f"✅ Thành công (Vào Queue): {success_count}")
    print(f"⛔ Thất bại (Hết hàng):     {fail_count}")
    print(f"❌ Lỗi mạng/Server sập:    {error_count}")
    print("=" * 40)


if __name__ == "__main__":
    main()