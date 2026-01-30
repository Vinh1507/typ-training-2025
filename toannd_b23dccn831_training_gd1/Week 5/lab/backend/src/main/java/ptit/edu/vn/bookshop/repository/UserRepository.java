package ptit.edu.vn.bookshop.repository;

import ptit.edu.vn.bookshop.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
