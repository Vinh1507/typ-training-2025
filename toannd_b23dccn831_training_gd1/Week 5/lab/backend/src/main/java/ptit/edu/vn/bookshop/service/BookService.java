package ptit.edu.vn.bookshop.service;

import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.dto.request.BookRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.BookResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.BookPageResponseDTO;

public interface BookService {
    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);
    BookResponseDTO updateBook(BookRequestDTO bookRequestDTO, Long id);
    void deleteBook(Long id);
    BookResponseDTO getBook(Long id, boolean isAdmin);
    BookPageResponseDTO fetchAllBooks(Pageable pageable,String[] book, String[] category, String[] author, String[] publisher,boolean isAdmin);
}
