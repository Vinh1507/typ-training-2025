package ptit.edu.vn.bookshop.domain.dto.mapper;


import org.mapstruct.Mapper;
import ptit.edu.vn.bookshop.domain.dto.request.AuthorRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.AuthorResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toEntity(AuthorRequestDTO dto);
    AuthorResponseDTO toResponseDTO(Author author);
}

