package ptit.edu.vn.bookshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher,Long>, JpaSpecificationExecutor<Publisher> {
    boolean existsByEmail(String email);
    Page<Publisher> findAllByStatus(StatusEnum status, Pageable pageable);

}
