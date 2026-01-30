package ptit.edu.vn.bookshop.domain.entity;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;;
import java.io.Serializable;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("redisToken")
public class RedisToken implements Serializable {
    private String id;
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String resetToken;
    private long accessTtl;
    private long refreshTtl;
}
