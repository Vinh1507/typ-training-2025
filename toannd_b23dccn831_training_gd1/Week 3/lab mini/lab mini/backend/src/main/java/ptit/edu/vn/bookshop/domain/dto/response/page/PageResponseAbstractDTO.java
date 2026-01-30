package ptit.edu.vn.bookshop.domain.dto.response.page;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PageResponseAbstractDTO {
    private int page;
    private int pageSize;
    private int pages;
    private Long total;
}
