package ptit.edu.vn.bookshop.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptit.edu.vn.bookshop.domain.constant.BookStatusEnum;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.BookRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.BookResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.BookPageResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Author;
import ptit.edu.vn.bookshop.domain.entity.Book;
import ptit.edu.vn.bookshop.domain.entity.Category;
import ptit.edu.vn.bookshop.domain.entity.Publisher;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.repository.*;
import ptit.edu.vn.bookshop.repository.specification.BookSpecificationBuilder;
import ptit.edu.vn.bookshop.service.BookService;
import ptit.edu.vn.bookshop.domain.dto.mapper.BookMapper;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final SearchRepository searchRepository;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, CategoryRepository categoryRepository,
                           AuthorRepository authorRepository, PublisherRepository publisherRepository, SearchRepository searchRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.searchRepository = searchRepository;
    }

    @Override
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        Book book = this.bookMapper.mapBookRequestDtoToBook(bookRequestDTO);
        Long categoryId = bookRequestDTO.getCategory().getId();
        Long authorId = bookRequestDTO.getAuthor().getId();
        Long publisherId = bookRequestDTO.getPublisher().getId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IdInvalidException("Category does not exist"));

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IdInvalidException("Author does not exist"));

        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new IdInvalidException("Publisher does not exist"));

        if (category.getStatus().equals(StatusEnum.INACTIVE) || author.getStatus().equals(StatusEnum.INACTIVE) ||
                publisher.getStatus().equals(StatusEnum.INACTIVE)) {
            throw new IllegalArgumentException("Category or Author or Publisher is inactive");
        }
        if (book.getPrice() == null || book.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (book.getQuantity() == null || book.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity must be >= 0");
        }
        if (bookRequestDTO.getDiscount() != null) {
            if (bookRequestDTO.getDiscount().compareTo(BigDecimal.ZERO) < 0
                    || bookRequestDTO.getDiscount().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("Discount must be between 0 and 100");
            }
        }
        boolean exists = this.bookRepository.existsByNameAndAuthorAndPublisher(book.getName(), author, publisher);
        if (exists) {
            throw new IllegalArgumentException("Book already exists with same name, author and publisher");
        }
        if (book.getDiscount() == null) {
            book.setDiscount(BigDecimal.valueOf(0.0));
        }
        book.setImage(bookRequestDTO.getImageUrl());
        book.setCategory(category);
        book.setAuthor(author);
        book.setPublisher(publisher);
        return this.bookMapper.mapBookToBookResponseDto(this.bookRepository.save(book));
    }

    @Override
    public BookResponseDTO updateBook(BookRequestDTO bookRequestDTO, Long id) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Book does not exists"));

        if (bookRequestDTO.getCategory() != null && bookRequestDTO.getCategory().getId() != null) {
            Category category = categoryRepository.findById(bookRequestDTO.getCategory().getId())
                    .orElseThrow(() -> new IdInvalidException("Category does not exist"));
            if (category.getStatus().equals(StatusEnum.INACTIVE)) {
                throw new IllegalStateException("Category must be active");
            }
            book.setCategory(category);
        }

        if (bookRequestDTO.getAuthor() != null && bookRequestDTO.getAuthor().getId() != null) {
            Author author = authorRepository.findById(bookRequestDTO.getAuthor().getId())
                    .orElseThrow(() -> new IdInvalidException("Author does not exist"));
            if (author.getStatus().equals(StatusEnum.INACTIVE)) {
                throw new IllegalStateException("Author must be active");
            }
            book.setAuthor(author);
        }

        if (bookRequestDTO.getPublisher() != null && bookRequestDTO.getPublisher().getId() != null) {
            Publisher publisher = publisherRepository.findById(bookRequestDTO.getPublisher().getId())
                    .orElseThrow(() -> new IdInvalidException("Publisher does not exist"));
            if (publisher.getStatus().equals(StatusEnum.INACTIVE)) {
                throw new IllegalStateException("Publisher must be active");
            }
            book.setPublisher(publisher);
        }
        if (bookRequestDTO.getPrice() != null && bookRequestDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (bookRequestDTO.getQuantity() != null && bookRequestDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity must be greater than or equal to 0");
        }
        if (bookRequestDTO.getDiscount() != null) {
            if (bookRequestDTO.getDiscount().compareTo(BigDecimal.ZERO) < 0
                    || bookRequestDTO.getDiscount().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("Discount must be between 0 and 100");
            }
        }
        if (book.getDiscount() == null) {
            book.setDiscount(BigDecimal.valueOf(0.0));
        }
        if (bookRequestDTO.getName() != null) book.setName(bookRequestDTO.getName());
        if (bookRequestDTO.getDescription() != null) book.setDescription(bookRequestDTO.getDescription());
        if (bookRequestDTO.getLanguage() != null) book.setLanguage(bookRequestDTO.getLanguage());
        if (bookRequestDTO.getQuantity() != null) book.setQuantity(bookRequestDTO.getQuantity());
        if (bookRequestDTO.getPrice() != null) book.setPrice(bookRequestDTO.getPrice());
        if (bookRequestDTO.getDiscount() != null) book.setDiscount(bookRequestDTO.getDiscount());
        if (bookRequestDTO.getStatus() != null) book.setStatus(bookRequestDTO.getStatus());
        if (bookRequestDTO.getImageUrl() != null) book.setImage(bookRequestDTO.getImageUrl());

        return this.bookMapper.mapBookToBookResponseDto(this.bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Book does not exists"));
        book.setStatus(BookStatusEnum.DELETED);
        this.bookRepository.save(book);
    }

    @Override
    public BookResponseDTO getBook(Long id, boolean isAdmin) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Book does not exists"));
        if (!isAdmin && book.getStatus().equals(BookStatusEnum.DELETED)) {
            throw new IllegalStateException("Book does not exist");
        }
        return this.bookMapper.mapBookToBookResponseDto(book);
    }

    @Override
    public BookPageResponseDTO fetchAllBooks(Pageable pageable, String[] book, String[] category, String[] author, String[] publisher, boolean isAdmin) {
        Page<Book> bookPage;
        // tim kiem book voi cac thành phần liên quan
        if (category != null && category.length > 0 ||
                                                author != null && author.length > 0 ||
                                                publisher != null && publisher.length > 0){
            return this.searchRepository.searchBooksWithFilters(pageable, book, category, author, publisher);
        }
        // TH4: tim kiem theo book,
        if (book != null && book.length > 0) {
            BookSpecificationBuilder builder = new BookSpecificationBuilder();
            for (String b : book) {
                Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
                Matcher matcher = pattern.matcher(b);
                if (matcher.find()) {
                    builder.with(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5));
                }
            }
            if (!isAdmin) {
                builder.with("status", "!:", BookStatusEnum.DELETED.name(), "", "");
            }
            bookPage = this.bookRepository.findAll(builder.build(), pageable);
        } else {
            bookPage = this.bookRepository.findAll(pageable);
        }
        BookPageResponseDTO bookPageResponseDTO = new BookPageResponseDTO();
        bookPageResponseDTO.setPage(pageable.getPageNumber() + 1);
        bookPageResponseDTO.setPageSize(pageable.getPageSize());
        bookPageResponseDTO.setPages(bookPage.getTotalPages());
        bookPageResponseDTO.setTotal(bookPage.getTotalElements());
        bookPageResponseDTO.setBooks(bookPage.stream().map(bookMapper::mapBookToBookResponseDto).collect(Collectors.toList()));
        return bookPageResponseDTO;
    }

}
