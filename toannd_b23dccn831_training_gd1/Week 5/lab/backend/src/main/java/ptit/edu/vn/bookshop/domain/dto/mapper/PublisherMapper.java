package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.mapstruct.Mapper;
import ptit.edu.vn.bookshop.domain.dto.request.PublisherRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.PublisherResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Publisher;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
   Publisher toEntity(PublisherRequestDTO dto);
   PublisherResponseDTO toResponseDto(Publisher publisher);
}
