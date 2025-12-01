## Quick Response - QR


### 1. Bài toán chính:
>>- Hệ thống quét QR cho hàng triệu người dùng cùng lúc
>>- Không cho phép điểm danh hộ (1 người quét giúp nhiều người) tránh công cụ hack/replay, tránh scan trùng (duplicate) và đảm bảo tính chính xác (ai đã quét là người thật)

>>- Mong muốn: Mã QR đổi (rotate) theo khoảng ephemeral, single-use, có check client thành công trong khoảng thời gian, tăng độ khó cho bot / auto-script

### 2. Nguyên tắc security & design:

>> 1. Không tin tưởng QR tĩnh:
>> Dùng QR động (ephemeral) chứa token ký (HMAC/JWS) và timestamp + nonce.
>> Server phải verify signature và tính hợp lệ (expiry, single-use)

Nguồn : https://www.oloid.com/blog/qr-code-authentication

>> 2. Ràng buộc scan với tài khoản & thiết bị: yêu cầu app đã đăng nhập, kèm device attestation (play integrity / app attest) để phát hiện emulator / modified clients. Backend loại bỏ request không có attestation hợp lệ

Nguồn : https://developer.android.com/google/play/integrity/overview

>> 3. Single-use server-side-de-dup + short TTL - mỗi token chỉ redeem 1 lần: lưu trạng thái redemption trong Redis (TTL ngắn) để scale. Dùng atomic ops để tránh race conditions

Nguồn : https://forum.redis.io/t/store-millions-of-user-events-with-time-to-live-and-retrieve-when-user-login/2595

>> 4. Thêm yếu tố xác thực / proximity - Kết hợp BLE / NFC / GPS / Biometric / liveness / photo nghiêm ngặt để chống quét hộ.

- Có thể scan + app gửi beacon RSSI hoặc xác thực mặt

Nguồn tham khảo thiết kế hệ thống: https://www.ijesat.com/ijesat/files/V20I1101_1750331303.pdf
![alt text](static/image.png)


>> 5. Giám sát rate-limit, WAF, anomaly detection:
>> - Bắt patterns bất thường (một thiết bị scan cho quá nhiều user, IP scan hàng loạt) và block / đánh dấu

>> 6. Standard hạ tầng cho quy mô lớn:
>> even ingestion - Kafka, cache / fast lookup (redis), OLAP cho analytics, autoscaling microservices

### 3. Cơ chế QR an toàn

#### 3.1. QR chứa gì (token format ???)

```
QR encode 1 URL ngắn hoặc payload JSON chứa:
- id (qr_id, UUID)
- ts (issued timestamp)
- exp (expiry)
- nonce (random)
- aud (audience / event id)
- sig (HMAC-SH256 or JWS signature của toàn bộ payload)
```

```
Ví dụ payload:
{ "id":"uuid", "ts":1700000000, "exp":1700000000, "aud":"EVENT123", "nonce":"abc123" } 
// signature = HMAC(secret_k, base64(payload))
```


>> Server khi nhận request sẽ verify signature bằng key lưu trong KMS / HSM. Nếu dùng asymmetric: sign bằng private key và verify bằng public key.
```
- Chữ ký ngăn attacker tạo QR giả
- Expiry + nonce ngăn replay lâu
- id + server single-use ngăn reuse
```

#### 3.2. Flow (scan từ app) 
```
1. Giảng viên / host tạo QR động hiển thị trên màn hình (hoặc server tạo ngẫu nhiên mỗi x giây)
2. Người dùng mở app đã đăng nhập quét QR (app chỉ nhận QR dạng payload ký)
3. App gửi tới backend: {user_id, device_id, qr_id, ts_scan, attestation_blob (PlayIntegrity/AppAttest), optional: geolocation/ble_rssi/photo_hash}
4. Server verify:
    - chữ ký QR hợp lệ & chưa expire
    - qr_id chưa redeemed
    - attestation hợp lệ (App Attest / Play Integrity)
    - vị trí/ble/other policy pass
    - throttle/rate limit pass
5. Nếu ok => mark redeemed (atomic), lưu audit log (user, device, ts, proof), trả kết quả success về app
<mark redeemed phải atomic (Redis SETNX / DB transaction) để tránh race. Sử dụng idempotent endpoints>
```

#### 3.3. Khi QR hiển thị cho nhiều người cùng thấy
- QR server-side rotate: QR chỉ hợp lệ trong 30–120s; mỗi lần đổi server tạo mới token. Nếu sự kiện quá đông, giảm TTL còn 15–30s để giảm window quét hộ
- Kết hợp OTP/Challenge: sau quét, server gửi 1 push tới app kèm mã 6 chữ số phải nhập để hoàn tất (2nd factor)
=> Phương án này rất mạnh chống quét hộ nhưng ảnh hưởng đến trải nghiệm người dùng - UX


#### 3.4. Chống “điểm danh hộ” & tool-hack — các lớp phòng thủ cụ thể

##### 3.4.1. Bắt buộc app chính chủ + device attestation

```
- Android: Play Integrity API (thay thế SafetyNet) — backend verify attestation token để chắc request từ app chính thức, thiết bị certified
- iOS: App Attest / DeviceCheck — xác thực instance app
```
Nguồn ios: https://developer.apple.com/documentation/devicecheck/validating-apps-that-connect-to-your-server

##### 3.4.2. Kết hợp proximity proof (giảm điểm danh hộ)
```
1. BLE beacon handshake: server/host phát beacon, app đo RSSI và gửi kèm, nếu RSSI phù hợp (ở gần), chấp nhận => BLE khó giả mạo từ xa
2. NFC tap: tốt nhất nếu cần high-assurance (khó bị remote scan hộ)
3. Wi-Fi probe / geolocation: xác minh user ở tọa độ yêu cầu (nhưng GPS bị spoofable)
4. Smart camera + liveness/photo match: app chụp ảnh mặt, so sánh với ảnh trong hồ sơ (face match + liveness) => bảo mật & pháp lý/riêng tư và AI đã có thể làm giả mạo khuôn mặt chỉ có thể scan trực tiếp trong app
```
#### 3.4.3. Thách thức người dùng bằng second factor
Sau scan, app hiển thị confirm dialog yêu cầu nhập PIN/OTP từ server (gửi push hoặc SMS). Rất hiệu quả nhưng tăng friction

#### 3.4.4. Phát hiện hành vi bất thường (server-side analytics)
Tự động detect patterns: 1 device quét nhiều user trong short time, nhiều users quét cùng IP/device fingerprint, quá nhiều retries → block/quarantine. Dùng ML/simple rules + alert
* Nguồn: https://www.hkcert.org/blog/introduction-of-qr-code-attacks-and-countermeasures

#### 3.4.5. Anti-bot / WAF / Rate limiting
Cloudflare Bot Management / reCAPTCHA Enterprise / WAF rules. Throttle theo IP/device/account
* Nguồn: https://www.guardsquare.com/blog/google-play-integrity-api-app-attestation

### 4. Kiến trúc hệ thống (scale cho hàng triêu người dùng)

```
1. QR generation service (signed tokens) — tính toán & ký, keys lưu trong KMS/HSM
2. Public display service (màn hình/website) — chỉ hiển thị QR image (statics can be CDN). QR token generate ở server-side
3. Scan API / Auth service — verify token + attestation + business rules, viết event vào queue
4. Event ingestion — Kafka (ingest millions events/s)
5. Fast state store — Redis for single-use flags, rate limits, token blacklists
6. Analytics store — ClickHouse or similar OLAP for high-throughput analytics
Nguồn: https://clickhouse.com/docs/academic_overview
7. DB (authoritative) — PostgreSQL/Spanner for final authoritative records (writes aggregated events)
8. Monitoring / Alerting — Prometheus + Grafana, logs to ELK <có thể sử dụng dockedemon>
9. WAF/CDN — Cloudflare / AWS WAF in front <Có thể sử dụng bản free hoặc hệ thống free>
10. KMS / HSM — sign keys protection

Scaling patterns:
1. Các dịch vụ vi mô distributed microservices
2. edge verification để tăng hiệu suất
3. Sử dụng groups tự động mở rộng và mở rộng theo chiều ngang cho các stateless services. 
4. Kafka topics được partitioned theo event/region
5. Redis clusters với sharding để redemption checks
```

### 5. Tool / library & foundation có thể sử dụng
```
1. QR generation/scan:
    - Server-side generation: qrcode (Python), go-qrcode, ZXing (Java)
    - Client scanning: ZXing (Android), ML Kit / iOS AVFoundation

2. Crypto & signing:
    - OpenSSL | libsodium | jose libraries (jwt/jws), use asymmetric signatures (RSASSA / ECDSA) nếu muốn verify public-side

3. Attestation & anti-tamper:
    - Google Play Integrity API (Android)
    - Apple App Attest / DeviceCheck (iOS)

4. Backend infra:
    - Kafka (ingest)
    - Redis (dedupe TTL)
    - ClickHouse (analytics)
    - PostgreSQL (auth)
    - KMS (AWS KMS / GCP KMS / Azure KeyVault)

5. Anti-bot / WAF / Bot detection
    - Cloudflare Bot Management, reCAPTCHA Enterprise, commercial anti-fraud vendors

6. Face/liveness (có thể cần)
    - FaceTec, AWS Rekognition, Azure Face (liveness solutions exist commercially) — đảm bảo bảo mật quyền riêng tư
```

### 6. Quy trình triển khai & checklist
```
1. Thiết kế token format (signed payload, expiry, nonce, id)
2. Triển khai key management (HSM/KMS)
3. Xây service tạo QR: rotate theo khoảng (30s)
4. App xác thực & attestation: tích hợp Play Integrity / App Attest
5. API verify: thực hiện các check (sig, expiry, attestation, proximity, single-use)
6. Deploy Redis (atomic SETNX) để mark redeemed
7. Event pipeline (Kafka) → consumer ghi về DB/ClickHouse
8. Rules engine: phát hiện bất thường & block
9. WAF + rate limit + bot management
10. Test load: simulate millions of users (k6, Gatling), test race conditions for single-use
11. Audit & logging: lưu audit trail (vì cần chứng minh ai đã quét)
12. Privacy & compliance: nếu dùng ảnh/biometric, điều chỉnh chính sách & tuân thủ GDPR/PDPA
```

### 7. Hạn chế, rủi ro & cân nhắc thực tế
```
1. Attestation không hoàn hảo: Play Integrity / App Attest có giới hạn => không chống mọi attack; cần defense-in-depth
2. Proximity signals có thể spoof (GPS spoofing, BLE replay) => NFC mạnh hơn nhưng requires hardware
3. Face/liveness gây vấn đề riêng tư/pháp lý => cần xin consent rõ ràng
4. UX vs Security tradeoff: stronger checks (photo, OTP, NFC) ảnh hưởng trải nghiệm
5. Chi phí hạ tầng: scale to millions đòi hỏi thiết kế event pipeline + ops mature (dự án đang free chấp nhận yếu hơn trả phí)
```