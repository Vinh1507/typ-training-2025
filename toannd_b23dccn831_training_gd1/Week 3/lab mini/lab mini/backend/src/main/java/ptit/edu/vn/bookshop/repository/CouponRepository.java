package ptit.edu.vn.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ptit.edu.vn.bookshop.domain.entity.Coupon;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> , JpaSpecificationExecutor<Coupon> {
    boolean existsByCode(String code);
    Optional<Coupon> findByCode(String code);
}
