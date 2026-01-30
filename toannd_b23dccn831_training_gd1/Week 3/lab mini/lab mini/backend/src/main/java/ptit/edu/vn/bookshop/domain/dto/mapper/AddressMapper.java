package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.mapstruct.Mapper;
import ptit.edu.vn.bookshop.domain.dto.response.AddressResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponseDTO toDto(Address address);
}
