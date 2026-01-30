package ptit.edu.vn.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptit.edu.vn.bookshop.domain.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.id = :cartItemId AND c.cart.id = :cartId")
    int deleteByIdAndCartId(@Param("cartItemId") Long cartItemId, @Param("cartId") Long cartId);
}
