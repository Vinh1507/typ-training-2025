package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.BookRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.BookResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.BookPageResponseDTO;
import ptit.edu.vn.bookshop.service.BookService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/books")
    @ApiMessage("Book created successfully")
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO bookResponseDTO = this.bookService.createBook(bookRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponseDTO);
    }

    @PutMapping("/books/{id}")
    @ApiMessage("Book updated successfully")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO bookResponseDTO = this.bookService.updateBook(bookRequestDTO, id);
        return ResponseEntity.ok(bookResponseDTO);
    }

    @DeleteMapping("/books/{id}")
    @ApiMessage("Book deleted successfully")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        this.bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books/{id}")
    @ApiMessage("Book retrieved successfully")
    public ResponseEntity<BookResponseDTO> getBook(@PathVariable Long id) {
        BookResponseDTO bookResponseDTO = this.bookService.getBook(id, false);
        return ResponseEntity.ok().body(bookResponseDTO);
    }

    @GetMapping("/books")
    @ApiMessage("Books retrieved successfully")
    public ResponseEntity<BookPageResponseDTO> getAllBooks(Pageable pageable,
                                                           @RequestParam(required = false) String[] book,
                                                           @RequestParam(required = false) String[] author,
                                                           @RequestParam(required = false) String[] category,
                                                           @RequestParam(required = false) String[] publisher) {
        BookPageResponseDTO bookPageResponseDTO = this.bookService.fetchAllBooks(pageable, book, category, author,
                publisher, false);
        return ResponseEntity.ok().body(bookPageResponseDTO);
    }

    @GetMapping("/admin/books/{id}")
    @ApiMessage("Book details retrieved by admin")
    public ResponseEntity<BookResponseDTO> getBookByAdmin(@PathVariable Long id) {
        BookResponseDTO bookResponseDTO = this.bookService.getBook(id, true);
        return ResponseEntity.ok().body(bookResponseDTO);
    }

    @GetMapping("/admin/books")
    @ApiMessage("Books list retrieved by admin")
    public ResponseEntity<BookPageResponseDTO> getAllBookByAdmin(Pageable pageable,
                                                                 @RequestParam(required = false) String[] book) {
        BookPageResponseDTO bookPageResponseDTO = this.bookService.fetchAllBooks(pageable, book, null,
                null, null, true);
        return ResponseEntity.ok().body(bookPageResponseDTO);
    }

    // home api
    
}