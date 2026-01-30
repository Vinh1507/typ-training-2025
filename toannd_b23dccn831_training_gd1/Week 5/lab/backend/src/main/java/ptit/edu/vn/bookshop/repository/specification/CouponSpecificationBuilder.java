package ptit.edu.vn.bookshop.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ptit.edu.vn.bookshop.domain.entity.Coupon;

public class CouponSpecificationBuilder extends GenericSpecificationBuilder<Coupon> {
    public CouponSpecificationBuilder(){
        super();
    }
}
