//package ptit.edu.vn.bookshop.util.task;
//
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import ptit.edu.vn.bookshop.repository.UserTokenRepository;
//
//import java.time.Instant;
//
//public class TokenCleanupTask {
//    private final UserTokenRepository userTokenRepository;
//
//    public TokenCleanupTask(UserTokenRepository userTokenRepository) {
//            this.userTokenRepository = userTokenRepository;
//    }
//
//    @Scheduled(fixedRate = 600_000)
//    @Async("taskExecutor")
//    public void deleteExpiredTokens() {
//        Instant now = Instant.now();
//        // Xóa token chưa verify quá 10 phút
//        Instant threshold = now.minusSeconds(600);
//        this.userTokenRepository.deleteUnverifiedTokensOlderThan(threshold);
//    }
//}
