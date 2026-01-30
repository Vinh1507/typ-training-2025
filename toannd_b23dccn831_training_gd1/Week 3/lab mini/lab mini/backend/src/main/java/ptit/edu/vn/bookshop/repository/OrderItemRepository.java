package ptit.edu.vn.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.edu.vn.bookshop.domain.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
