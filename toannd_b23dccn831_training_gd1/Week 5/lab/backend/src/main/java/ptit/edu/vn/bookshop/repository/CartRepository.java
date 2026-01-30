package ptit.edu.vn.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ptit.edu.vn.bookshop.domain.entity.Cart;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByUserId(Long id);
}
