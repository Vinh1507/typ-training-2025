package ptit.edu.vn.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ptit.edu.vn.bookshop.domain.entity.Address;
import ptit.edu.vn.bookshop.domain.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByUser(User user);
    // Cập nhật tất cả địa chỉ của user thành isDefault = false
    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user = :user")
    void updateIsDefaultFalseByUser(@Param("user") User user);

    Optional<Address> findByUserAndIsDefaultTrue(User user);

    boolean existsByUser(User user);
}
