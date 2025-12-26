import http from 'k6/http';
import { check, sleep } from 'k6';

// Cấu hình test
export const options = {
  stages: [
    { duration: '15s', target: 7000 }, // Tăng dần lên 100 users trong 30s
    { duration: '30s', target: 7000 },  // Tăng lên 500 users
    { duration: '15s', target: 0 },   // Giảm về 0
  ],
};

export default function () {
  const urlInitiate = 'http://localhost:8080/api/login/initiate';
  const payload = JSON.stringify({ username: 'test_user' });
  const params = { headers: { 'Content-Type': 'application/json' } };

  // 1. User vào trang Login -> Tạo QR
  const res = http.post(urlInitiate, payload, params);
  
  check(res, { 'status was 200': (r) => r.status == 200 });

  if (res.status === 200) {
    const body = JSON.parse(res.body);
    const txId = body.tx.tx;
    const urlVerify = `http://localhost:8080/api/login/verify/${txId}`;

    // 2. Giả lập hành vi Polling (Check status 5 lần, mỗi lần cách nhau 2s)
    for (let i = 0; i < 5; i++) {
        const verifyRes = http.get(urlVerify);
        check(verifyRes, { 'verify status 200': (r) => r.status == 200 });
        sleep(2); // Nghỉ 2s
    }
  }
  
  sleep(1);
}