package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.AuthorRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.AuthorResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.AuthorPageResponseDTO;
import ptit.edu.vn.bookshop.service.AuthorService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/authors")
    @ApiMessage("Author created successfully")
    public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorRequestDTO authorRequestDTO) {
        AuthorResponseDTO authorResponseDTO = this.authorService.createAuthor(authorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorResponseDTO);
    }

    @PutMapping("/authors/{id}")
    @ApiMessage("Author updated successfully")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@Valid @RequestBody AuthorRequestDTO authorRequestDTO, @PathVariable Long id) {
        AuthorResponseDTO authorResponseDTO = this.authorService.updateAuthor(authorRequestDTO, id);
        return ResponseEntity.ok().body(authorResponseDTO);
    }

    @DeleteMapping("/authors/{id}")
    @ApiMessage("Author deleted successfully")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        this.authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/authors/{id}")
    @ApiMessage("Author retrieved successfully")
    public ResponseEntity<AuthorResponseDTO> getAuthor(@PathVariable Long id) {
        AuthorResponseDTO authorResponseDTO = this.authorService.fetchAuthor(id, false);
        return ResponseEntity.ok().body(authorResponseDTO);
    }

    @GetMapping("/authors")
    @ApiMessage("Authors retrieved successfully")
    public ResponseEntity<AuthorPageResponseDTO> getAllAuthors(Pageable pageable,
                                                               @RequestParam(required = false) String[] author) {
        AuthorPageResponseDTO responseDTO = this.authorService.fetchAllAuthors(pageable, author, false);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/admin/authors/{id}")
    @ApiMessage("Author details retrieved by admin")
    public ResponseEntity<AuthorResponseDTO> getAuthorByAdmin(@PathVariable Long id) {
        AuthorResponseDTO authorResponseDTO = this.authorService.fetchAuthor(id, true);
        return ResponseEntity.ok().body(authorResponseDTO);
    }

    @GetMapping("/admin/authors")
    @ApiMessage("Authors list retrieved by admin")
    public ResponseEntity<AuthorPageResponseDTO> getAllAuthorsByAdmin(Pageable pageable,
                                                               @RequestParam(required = false) String[] author){
        AuthorPageResponseDTO responseDTO = this.authorService.fetchAllAuthors(pageable, author, true);
        return ResponseEntity.ok().body(responseDTO);
    }
}
