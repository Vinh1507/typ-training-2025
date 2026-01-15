# Tài liệu hệ thống Check-in sự kiện

_Bài toán: Check-in sự kiện quy mô lớn, điểm danh chấm công,..._

## MÔ TẢ BÀI TOÁN

```
Sự kiện có 2000 người tham dự
- Mỗi người có userId trong hệ thống (user_0001 -> user_5000)
- Ban tổ chức chiếu QR lên màn hình lớn
- QR tự động refresh mỗi 15 giây (vì lý do bảo mật)
- Mọi người trong phòng quét QR để check-in
- Mỗi người CHỈ được check-in 1 LẦN DUY NHẤT trong suốt sự kiện
Authentication Flow:
  1. Giả định user đã đăng nhập thành công
  2. Client gửi userId trực tiếp trong payload
  3. Backend chỉ validate userId có trong DB
  → Đủ để chứng minh logic check-in và chống duplicate

Timeline:
09:00:00  QR_001 (event_abc) xuất hiện
09:00:05  User A quét QR_001 → "Check-in thành công"
09:00:12  User B quét QR_001 → "Check-in thành công"
09:00:15  QR_001 hết hạn → QR_002 (event_abc) xuất hiện
09:00:18  User C quét QR_002 → "Check-in thành công" (QR mới nhưng OK)
09:00:22  User A quét QR_002 → "Bạn đã check-in rồi" (đã quét QR_001)
09:00:30  QR_002 hết hạn → QR_003 (event_abc) xuất hiện
09:00:35  User D quét QR_003 → "Check-in thành công"
09:00:40  User A quét QR_003 → "Bạn đã check-in rồi" (vẫn bị chặn)
...
```

1. YÊU CẦU BÀI TOÁN

| ID       | Yêu Cầu           | Mô Tả Chi Tiết                                                                                   |
| -------- | ----------------- | ------------------------------------------------------------------------------------------------ |
| **FR-1** | Xác thực user     | Kiểm tra userId có tồn tại trong hệ thống                                                        |
| **FR-2** | Sinh mã QR        | Tạo mã QR cho event, tự động refresh mỗi 15 giây                                                 |
| **FR-3** | Check-in          | Ghi nhận check-in của user vào event                                                             |
| **FR-4** | Chống trùng lặp   | **Mỗi user chỉ check-in 1 lần cho 1 event**<br/>Không phụ thuộc QR code nào (QR có thể thay đổi) |
| **FR-5** | Auto-refresh QR   | Backend tự động sinh QR mới mỗi 15s cho cùng 1 event                                             |
| **FR-6** | Kiểm tra hết hạn  | QR hết hiệu lực sau 15s                                                                          |
| **FR-7** | Lưu lịch sử       | Ghi log tất cả check-in attempts                                                                 |
| **FR-8** | Thống kê realtime | Đếm số người đã check-in vào event                                                               |

#### Yêu cầu phí chức năng

| Danh Mục        | Yêu Cầu              | Chỉ Số Mục Tiêu                              |
| --------------- | -------------------- | -------------------------------------------- |
| **Performance** | Response time        | p95 < 100ms, p99 < 500ms                     |
| **Throughput**  | Concurrent users     | 1000-2000 người cùng lúc                     |
| **Consistency** | Duplicate prevention | **100% - Mỗi user chỉ check-in 1 lần/event** |
| **Latency**     | QR refresh           | < 100ms để sinh QR mới                       |

2. TECH STACK (MAIN)

```
Backend:      Node.js(20) + Express.js
Database:     PostgreSQL 15
Cache:        Redis 7
Queue:        RabbitMQ 3
Monitoring:   PM2
```

3. CẤU HÌNH MÁY CHỦ

```
CPU:    8vCPU
RAM:    8GB
Disk:   100GB SSD
OS:     Window 11
```

4. LOW-LEVEL DESIGN <Br>
   4.1. Database
   **Bảng Users**

```sql
CREATE TABLE users (
    id INT PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_users_user_id ON users(user_id);
```

**Bảng Events**

```sql
CREATE TABLE events (
    id INT PRIMARY KEY,
    event_id VARCHAR(100) UNIQUE NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_events_id ON events(event_id);
CREATE INDEX idx_events_status ON events(status);
CREATE INDEX idx_events_time ON events(start_time, end_time);
```

**Bảng QRCodes**

```sql
CREATE TABLE qr_codes (
    id INT PRIMARY KEY,
    token_id UUID UNIQUE NOT NULL,
    event_id VARCHAR(100) NOT NULL,
    qr_data TEXT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

CREATE INDEX idx_qr_token ON qr_codes(token_id);
```

**Bảng Check_ins**

```sql
CREATE TABLE check_ins (
    id INT PRIMARY KEY,
    check_in_id VARCHAR(50) UNIQUE NOT NULL,
    event_id VARCHAR(100) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    token_id UUID NOT NULL,
    checked_in_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (event_id) REFERENCES events(event_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE UNIQUE INDEX idx_unique_event_user_checkin
ON check_ins(event_id, user_id);
```

4.2. API DESIGN

#### Endpoint 1: Lấy mã QR hiện tại

```http
GET /api/v1/events/{eventId}/qr/current
```

**Response 200 OK:**

```json
{
  "success": true,
  "data": {
    "eventId": "event_abc",
    "eventName": "Tech Conference 2025",
    "qrCode": "eyJ0b2tlbklkIjoiNzNhYzg5ZjQtMmUxMS00ZGE3LWI5YzYtOGYzZDVhNmUyYjQxIiwiZXZlbnRJZCI6ImV2ZW50X2FiYyIsInRpbWVzdGFtcCI6MTY5ODc2NTQzMiwic2lnbmF0dXJlIjoiOGYzZTlhMWIyYzRkNWU2ZjdhOGI5YzBkMWUyZjNhNGIifQ==",
    "tokenId": "73ac89f4-2e11-4da7-b9c6-8f3d5a6e2b41",
    "expiresAt": "2025-10-30T10:15:47Z",
    "expiresInSeconds": 12,
    "totalAttendees": 245
  },
  "timestamp": "2025-10-30T10:15:35Z"
}
```

**QR Code Format (Base64-encoded JSON):**

```json
{
  "tokenId": "73ac89f4-2e11-4da7-b9c6-8f3d5a6e2b41",
  "eventId": "event_abc",
  "timestamp": 1698765432,
  "signature": "8f3e9a1b2c4d5e6f7a8b9c0d1e2f3a4b"
}
```

**Signature Algorithm:**

```javascript
const signature = crypto
  .createHmac("sha256", SECRET_KEY)
  .update(`${tokenId}:${eventId}:${timestamp}`)
  .digest("hex");
```

#### Endpoint 2: Check-In

**Request:**

```http
POST /api/v1/qr/check-in
Content-Type: application/json

{
  "qrCode": "eyJ0b2tlbklkIjoiNzNhYzg5ZjQtMmUxMS00ZGE3LWI5YzYtOGYzZDVhNmUyYjQxIiwiZXZlbnRJZCI6ImV2ZW50X2FiYyIsInRpbWVzdGFtcCI6MTY5ODc2NTQzMiwic2lnbmF0dXJlIjoiOGYzZTlhMWIyYzRkNWU2ZjdhOGI5YzBkMWUyZjNhNGIifQ==",
  "userId": "user_0123",
}
```

Để đơn giản hóa, tập trung vào việc show giải pháp -> payload chỉ cần có userId coi như họ đã đăng nhập thành công

**Response 200 OK (Success - First check-in):**

```json
{
  "success": true,
  "data": {
    "checkInId": "checkin_abc123",
    "eventId": "event_abc",
    "eventName": "Tech Conference 2025",
    "message": "Check-in thành công",
    "checkedInAt": "2025-10-30T10:15:35Z",
    "totalAttendees": 246,
    "yourPosition": 246
  },
  "timestamp": "2025-10-30T10:15:35Z"
}
```

**Response 409 Conflict (User đã check-in):**

```json
{
  "success": false,
  "error": {
    "code": "ALREADY_CHECKED_IN",
    "message": "Bạn đã check-in sự kiện này rồi",
    "details": {
      "eventId": "event_abc",
      "eventName": "Tech Conference 2025",
      "previousCheckInAt": "2025-10-30T10:10:15Z",
      "minutesAgo": 5
    }
  },
  "timestamp": "2025-10-30T10:15:35Z"
}
```

**Response 404 Not Found (User không tồn tại):**

```json
{
  "success": false,
  "error": {
    "code": "USER_NOT_FOUND",
    "message": "User không tồn tại trong hệ thống",
    "details": {
      "userId": "user_9999"
    }
  },
  "timestamp": "2025-10-30T10:15:35Z"
}
```

**Response 410 Gone (QR hết hạn):**

```json
{
  "success": false,
  "error": {
    "code": "QR_EXPIRED",
    "message": "Mã QR đã hết hạn, vui lòng quét lại",
    "details": {
      "expiredAt": "2025-10-30T10:15:30Z",
      "currentTime": "2025-10-30T10:15:47Z"
    }
  },
  "timestamp": "2025-10-30T10:15:47Z"
}
```

**Response 400 Bad Request (Chữ ký sai):**

```json
{
  "success": false,
  "error": {
    "code": "INVALID_QR_CODE",
    "message": "Mã QR không hợp lệ hoặc đã bị giả mạo"
  },
  "timestamp": "2025-10-30T10:15:35Z"
}
```

#### Endpoint 3: Thống kê

**Request:**

```http
GET /api/v1/events/{eventId}/statistics
```

**Response 200 OK:**

```json
{
  "success": true,
  "data": {
    "eventId": "event_abc",
    "eventName": "Tech Conference 2025",
    "totalCheckedIn": 1847,
    "capacity": 2000,
    "occupancyRate": 92.35,
    "currentQR": {
      "tokenId": "73ac89f4-2e11-4da7-b9c6-8f3d5a6e2b41",
      "expiresInSeconds": 8
    },
    "checkInRate": {
      "lastMinute": 45,
      "last5Minutes": 203,
      "last15Minutes": 567
    },
    "peakTime": "2025-10-30T10:05:00Z"
  },
  "timestamp": "2025-10-30T11:30:00Z"
}
```

5. HIGH-LEVEL DESIGN

```
                  INTERNET
                     │
                     ▼
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    ┃  SERVER (8 vCPU, 8GB RAM)      ┃
    ┃                                ┃
    ┃  ┌──────────────────────────┐  ┃
    ┃  │ Node.js (8 workers)      │  ┃
    ┃  │ Express.js - Port 3000   │  ┃
    ┃  └──────────┬───────────────┘  ┃
    ┃             │                  ┃
    ┃    ┌────────┼───────┬          ┃
    ┃    │        │       │          ┃
    ┃    ▼        ▼       ▼          ┃
    ┃  Redis  Postgres RabbitMQ      ┃
    ┃  :6379   :5432    :5672        ┃
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
```

FLOW:

```
┌──────────┐
│  User    │
└─────┬────┘
      │
      │ POST /api/v1/qr/check-in
      │ {
      │   qrCode: "...",
      │   userId: "user_0123"  ← Giả định đã đăng nhập
      │ }
      │
      ▼
┌────────────────────────────────────────────┐
│  Node.js Worker                            │
│                                            │
│  STEP 1: Decode QR                         │
│    • Base64 decode                         │
│    • Extract: tokenId, eventId,            │
│               timestamp, signature         │
│                                            │
│  STEP 2: Verify Signature                  │
│    • HMAC validation                       │
│    • if invalid: RETURN 400                │
│                                            │
│  STEP 3: Check Expiry                      │
│    • age = now - timestamp                 │
│    • if age > 15s: RETURN 410 GONE         │
│                                            │
│  STEP 4: Validate User                     │
│    • Query: SELECT id FROM users           │
│              WHERE user_id = 'user_0123'   │
│    • Use index: idx_users_user_id          │
│    • if NOT found: RETURN 404              │
│                                            │
└────────┬───────────────────────────────────┘
         │
         │  CRITICAL: Check if user already checked in
         │ Key: checkin:{eventId}:{userId}
         │                ↑
         │                └─ Dùng eventId
         ▼
   ┌─────────────────────────────────────────┐
   │  Redis                                  │
   │                                         │
   │  Key: checkin:{eventId}:{userId}        │
   │  Example:                               │
   │    checkin:event_abc:user-001           │
   │    checkin:event_abc:user-002           │
   │                                         │
   │  SET checkin:{eventId}:{userId} "1"     │
   │      NX EX 86400                        │
   │      ↑                                  │
   │      └─ TTL 24h (cả ngày sự kiện)       │
   │                                         │
   │  Kết quả:                               │
   │    User A quét QR_001: "OK"             │
   │    User B quét QR_001: "OK"             │
   │    User A quét QR_002: null             │
   │      (QR khác nhưng cùng event)         │
   │                                         │
   └──────┬──────────────────────────────────┘
          │
          ├─────► "OK" (First check-in for this user)
          │       │
          │       ▼
          │    ┌────────────────────────────┐
          │    │  Push to RabbitMQ          │
          │    │  Payload: {                │
          │    │    eventId, userId,        │
          │    │    tokenId, timestamp      │
          │    │  }                         │
          │    └──────┬─────────────────────┘
          │           │ (Background)
          │           ▼
          │    ┌────────────────────────────┐
          │    │  RabbitMQ Consumer         │
          │    └──────┬─────────────────────┘
          │           │
          │           ▼
          │    ┌────────────────────────────┐
          │    │  PostgreSQL                │
          │    │                            │
          │    │  INSERT INTO check_ins (   │
          │    │    event_id, user_id, ...  │
          │    │  ) VALUES (...)            │
          │    │  ON CONFLICT               │
          │    │    (event_id, user_id)     │
          │    │    DO NOTHING              │
          │    │                            │
          │    └────────────────────────────┘
          │           │
          │           ▼
          │    ┌────────────────────────────┐
          │    │  RETURN 200 OK             │
          │    │  {                         │
          │    │    success: true,          │
          │    │    message: "Check-in      │
          │    │             thành công",   │
          │    │    totalAttendees: 245     │
          │    │  }                         │
          │    │                            │
          │    └────────────────────────────┘
          │
          └─────► null (User đã check-in rồi)
                  │
                  ▼
               ┌──────────────────────────────────┐
               │  RETURN 409 CONFLICT             │
               │  {                               │
               │    success: false,               │
               │    error: {                      │
               │      code: "ALREADY_CHECKED_IN", │
               │      message: "Bạn đã            │
               │                check-in rồi"     │
               │    }                             │
               │  }                               │
               │                                  │
               └──────────────────────────────────┘

```
