package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.springframework.stereotype.Component;
import ptit.edu.vn.bookshop.domain.dto.request.BookRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.BookResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Author;
import ptit.edu.vn.bookshop.domain.entity.Book;
import ptit.edu.vn.bookshop.domain.entity.Category;
import ptit.edu.vn.bookshop.domain.entity.Publisher;

import java.math.BigDecimal;

@Component
public class BookMapper {
    public Book mapBookRequestDtoToBook(BookRequestDTO requestDTO) {
        Book book = new Book();
        book.setName(requestDTO.getName());
        book.setDescription(requestDTO.getDescription());
        book.setPrice(requestDTO.getPrice());
        book.setLanguage(requestDTO.getLanguage());
        book.setQuantity(requestDTO.getQuantity());
        book.setDiscount(requestDTO.getDiscount());
        book.setImage(requestDTO.getImageUrl());
        if (requestDTO.getCategory() != null) {
            Category category = new Category();
            category.setId(requestDTO.getCategory().getId());
            book.setCategory(category);
        }
        if (requestDTO.getAuthor() != null) {
            Author author = new Author();
            author.setId(requestDTO.getAuthor().getId());
            book.setAuthor(author);
        }
        if (requestDTO.getPublisher() != null) {
            Publisher publisher = new Publisher();
            publisher.setId(requestDTO.getPublisher().getId());
            book.setPublisher(publisher);
        }
        return book;
    }

    public BookResponseDTO mapBookToBookResponseDto(Book book) {
        BookResponseDTO response = new BookResponseDTO();
        response.setId(book.getId());
        response.setName(book.getName());
        response.setDescription(book.getDescription());
        response.setPrice(book.getPrice());
        response.setLanguage(book.getLanguage());
        response.setQuantity(book.getQuantity());
        response.setDiscount(book.getDiscount());
        response.setFinalPrice(book.getPrice().subtract(book.getPrice().multiply(book.getDiscount().divide(BigDecimal.valueOf(100))))
                .setScale(0, BigDecimal.ROUND_HALF_UP));
        response.setStatus(book.getStatus());
        response.setImageUrl(book.getImage());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdateAt(book.getUpdatedAt());
        if (book.getCategory() != null) {
            BookResponseDTO.BookCategoryResponseDTO category = new BookResponseDTO.BookCategoryResponseDTO();
            category.setId(book.getCategory().getId());
            category.setName(book.getCategory().getName());
            response.setCategory(category);
        }
        if (book.getAuthor() != null) {
            BookResponseDTO.BookAuthorResponseDTO author = new BookResponseDTO.BookAuthorResponseDTO();
            author.setId(book.getAuthor().getId());
            author.setName(book.getAuthor().getName());
            response.setAuthor(author);
        }
        if (book.getPublisher() != null) {
            BookResponseDTO.BookPublisherResponseDTO publisher = new BookResponseDTO.BookPublisherResponseDTO();
            publisher.setId(book.getPublisher().getId());
            publisher.setName(book.getPublisher().getName());
            response.setPublisher(publisher);
        }
        return response;
    }
}
