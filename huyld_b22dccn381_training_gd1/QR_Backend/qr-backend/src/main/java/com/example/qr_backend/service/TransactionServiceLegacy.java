package com.example.qr_backend.service;

import com.example.qr_backend.entity.LoginTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

@Service
public class TransactionServiceLegacy {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public int processTransactionSafe(String transactionId, boolean isApproved) {
        String key = "qr_tx:" + transactionId;
        System.out.println("Key 2: " + key);
        String expectedStatus = "WAITING";
        String newStatus = isApproved ? "APPROVED" : "DENIED";

        List<Object> txResult = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                // 1. WATCH: Giám sát key
                operations.watch(key);

                String currentStatus = (String) operations.opsForHash().get(key, "status");

                //Case 1: Expired: there is no key.
                if(currentStatus == null) {
                    operations.unwatch();
                    return Collections.singletonList("EXPIRED_SIGNAL");
                }

                //Case 2: There is key but the status has been changed. (You are late).
                if (!expectedStatus.equals(currentStatus)) {
                    operations.unwatch();
                    return Collections.singletonList("ALREADY_PROCESSED_SIGNAL");
                }

                // 4. MULTI: Bắt đầu Transaction
                operations.multi();

                // 5. WRITE: Ghi vào hàng đợi
                operations.opsForHash().put(key, "status", newStatus);

                // 6. EXEC: Thực thi
                return operations.exec();
            }
        });

        // Xử lý kết quả trả về
        // QUY ƯỚC TRẢ VỀ:
        //  1: Thành công
        //  0: Hết hạn / Không tồn tại
        // -1: Đã bị xử lý trước đó (Logic check fail hoặc Race condition)

        //Nếu result rỗng -> Race Condition (WATCH phát hiện thay đổi)
        if (txResult == null || txResult.isEmpty()) {
            return -1;
        }

        if(txResult != null && !txResult.isEmpty()) {
            Object firstResult = txResult.get(0);

            if("EXPIRED_SIGNAL".equals(firstResult)) {
                return 0;
            }

            if ("ALREADY_PROCESSED_SIGNAL".equals(firstResult)) {
                return -1; // Đã bị xử lý (Logic sai)
            }
        }

        return 1;
    }
}
