package ptit.edu.vn.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ptit.edu.vn.bookshop.domain.entity.Author;
import ptit.edu.vn.bookshop.domain.entity.Book;
import ptit.edu.vn.bookshop.domain.entity.Publisher;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    boolean existsByNameAndAuthorAndPublisher(String book, Author author, Publisher publisher);
}
