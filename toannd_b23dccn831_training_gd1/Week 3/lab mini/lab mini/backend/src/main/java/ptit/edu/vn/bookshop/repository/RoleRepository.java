package ptit.edu.vn.bookshop.repository;

import ptit.edu.vn.bookshop.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>, JpaSpecificationExecutor<Role> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    Optional<Role> findByName(String name);

}
