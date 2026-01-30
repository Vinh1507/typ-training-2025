package ptit.edu.vn.bookshop.domain.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.dto.response.BookResponseDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookPageResponseDTO extends PageResponseAbstractDTO {
    List<BookResponseDTO> books;
}
