package ptit.edu.vn.bookshop.repository;

import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.dto.response.page.BookPageResponseDTO;



public interface SearchRepository {
    BookPageResponseDTO searchBooksWithFilters(Pageable pageable, String[] book, String[] category, String[] auhor, String[] publisher);
}
