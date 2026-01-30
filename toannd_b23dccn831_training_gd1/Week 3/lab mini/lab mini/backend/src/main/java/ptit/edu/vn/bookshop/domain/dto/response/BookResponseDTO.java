package ptit.edu.vn.bookshop.domain.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.BookStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String language;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal finalPrice;
    @Enumerated(EnumType.STRING)
    private BookStatusEnum status;
    private String imageUrl;
    private Instant createdAt;
    private Instant updateAt;
    private BookCategoryResponseDTO category;
    private BookAuthorResponseDTO author;
    private BookPublisherResponseDTO publisher;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCategoryResponseDTO {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookAuthorResponseDTO {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookPublisherResponseDTO {
        private Long id;
        private String name;
    }

}
