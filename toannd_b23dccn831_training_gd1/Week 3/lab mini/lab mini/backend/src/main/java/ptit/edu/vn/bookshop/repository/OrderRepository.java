package ptit.edu.vn.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ptit.edu.vn.bookshop.domain.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> , JpaSpecificationExecutor<Order> {
}
