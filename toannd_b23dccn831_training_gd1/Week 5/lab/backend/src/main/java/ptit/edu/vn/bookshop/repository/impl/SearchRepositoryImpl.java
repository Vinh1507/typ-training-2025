package ptit.edu.vn.bookshop.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ptit.edu.vn.bookshop.domain.dto.response.page.BookPageResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Author;
import ptit.edu.vn.bookshop.domain.entity.Book;
import ptit.edu.vn.bookshop.domain.entity.Category;
import ptit.edu.vn.bookshop.domain.entity.Publisher;
import ptit.edu.vn.bookshop.repository.SearchRepository;
import ptit.edu.vn.bookshop.repository.specification.*;
import ptit.edu.vn.bookshop.domain.dto.mapper.BookMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class SearchRepositoryImpl implements SearchRepository {
    private final EntityManager entityManager;
    private final BookMapper bookMapper;

    public SearchRepositoryImpl(EntityManager entityManager, BookMapper bookMapper) {
        this.entityManager = entityManager;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookPageResponseDTO searchBooksWithFilters(Pageable pageable, String[] book, String[] category, String[] author, String[] publisher) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
        List<Predicate> relationPredicates = new ArrayList<>();
        if (book != null && book.length > 0) {
            relationPredicates.add(buildPredicates(builder, root, book, pattern));
        }
        if (category != null && category.length > 0) {
            Join<Book, Category> categoryJoin = root.join("category");
            relationPredicates.add(buildPredicates(builder, categoryJoin, category, pattern));
        }
        if (author != null && author.length > 0) {
            Join<Book, Author> authorJoin = root.join("author");
            relationPredicates.add(buildPredicates(builder, authorJoin, author, pattern));
        }
        if (publisher != null && publisher.length > 0) {
            Join<Book, Publisher> publisherJoin = root.join("publisher");
            relationPredicates.add(buildPredicates(builder, publisherJoin, publisher, pattern));
        }

        Predicate finalPredicate = builder.and(relationPredicates.toArray(new Predicate[0]));
        query.where(finalPredicate);

        List<Book> books = this.entityManager.createQuery(query)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        long total = countBookJoinCategory(book, category, author, publisher);

        BookPageResponseDTO responseDTO = new BookPageResponseDTO();
        responseDTO.setPage(pageable.getPageNumber() + 1);
        responseDTO.setPageSize(pageable.getPageSize());
        responseDTO.setTotal(total);
        responseDTO.setPages((int) Math.ceil((double) total / pageable.getPageSize()));
        responseDTO.setBooks(books.stream().map(bookMapper::mapBookToBookResponseDto).collect(Collectors.toList()));
        return responseDTO;
    }

    private long countBookJoinCategory(String[] book, String[] category, String[] author, String[] publisher) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Book> root = query.from(Book.class);

        Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
        List<Predicate> relationPredicates = new ArrayList<>();
        if (book != null && book.length > 0) {
            relationPredicates.add(buildPredicates(builder, root, book, pattern));
        }

        if (category != null && category.length > 0) {
            Join<Book, Category> categoryJoin = root.join("category");
            relationPredicates.add(buildPredicates(builder, categoryJoin, category, pattern));
        }
        if (author != null && author.length > 0) {
            Join<Book, Author> authorJoin = root.join("author");
            relationPredicates.add(buildPredicates(builder, authorJoin, author, pattern));
        }
        if (publisher != null && publisher.length > 0) {
            Join<Book, Publisher> publisherJoin = root.join("publisher");
            relationPredicates.add(buildPredicates(builder, publisherJoin, publisher, pattern));
        }
        Predicate finalPredicate = builder.and(relationPredicates.toArray(new Predicate[0]));
        query.select(builder.countDistinct(root)).where(finalPredicate);
        return this.entityManager.createQuery(query).getSingleResult();
    }

    private Predicate toPredicate(@NotNull final Path<?> root, @NotNull final CriteriaBuilder builder, SpecSearchCriteria criteria) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue().toString() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
        };
    }

    private Predicate buildPredicates(CriteriaBuilder builder, From<?, ?> rootOrJoin, String[] conditions, Pattern pattern) {
        if (conditions == null || conditions.length == 0 || rootOrJoin == null) {
            return builder.conjunction();
        }

        List<Predicate> predicates = new ArrayList<>();
        for (String condition : conditions) {
            Matcher matcher = pattern.matcher(condition);
            if (matcher.find()) {
                SpecSearchCriteria criteria = new SpecSearchCriteria(
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(3),
                        matcher.group(4),
                        matcher.group(5)
                );
                predicates.add(toPredicate(rootOrJoin, builder, criteria));
            }
        }
        return predicates.isEmpty()
                ? builder.conjunction()
                : builder.and(predicates.toArray(new Predicate[0]));
    }
}
