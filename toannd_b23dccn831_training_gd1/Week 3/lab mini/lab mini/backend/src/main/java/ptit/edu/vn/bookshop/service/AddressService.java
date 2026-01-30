package ptit.edu.vn.bookshop.service;

import org.apache.coyote.BadRequestException;
import ptit.edu.vn.bookshop.domain.dto.request.AddressRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.AddressResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.AddressPageResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Address;


public interface AddressService {
    AddressResponseDTO createAddress(AddressRequestDTO AddressRequestDTO);
    AddressResponseDTO updateAddress(AddressRequestDTO AddressRequestDTO, Long id);
    void deleteAddress(Long id) ;
    AddressResponseDTO getAddress(Long id);
    AddressPageResponseDTO getAllAddresses();
    Address getAddressByIsDefault();
}
