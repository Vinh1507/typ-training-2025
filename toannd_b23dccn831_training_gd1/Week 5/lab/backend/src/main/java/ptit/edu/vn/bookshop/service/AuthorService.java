package ptit.edu.vn.bookshop.service;

import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.dto.request.AuthorRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.AuthorResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.AuthorPageResponseDTO;

public interface AuthorService {
    AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO);
    AuthorResponseDTO updateAuthor(AuthorRequestDTO authorRequestDTO, Long id);
    void deleteAuthor(Long id);
    AuthorResponseDTO fetchAuthor(Long id, boolean isAdmin);
    AuthorPageResponseDTO fetchAllAuthors(Pageable pageable, String[] author, boolean isAdmin);

}
