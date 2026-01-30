package ptit.edu.vn.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ptit.edu.vn.bookshop.domain.constant.TokenType;

import java.time.Instant;
import java.util.Optional;

//public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
//    Optional<UserToken> findByTokenValue(String tokenValue);
//    void deleteByUserIdAndTokenType(Long userId, TokenType tokenType);
//    void deleteByExpiryTimeBefore(Instant now);
//    Optional<UserToken> findByUserIdAndTokenTypeAndVerifiedFalse(Long userId, TokenType tokenType);
//
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM UserToken ut WHERE ut.verified = false AND ut.createdAt < :threshold")
//    void deleteUnverifiedTokensOlderThan(@Param("threshold") Instant threshold);
//}
