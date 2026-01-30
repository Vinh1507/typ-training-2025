package ptit.edu.vn.bookshop.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.AddressRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.AddressResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.AddressPageResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Address;
import ptit.edu.vn.bookshop.domain.entity.User;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.exception.UsernameNotFoundException;
import ptit.edu.vn.bookshop.exception.BadRequestException;
import ptit.edu.vn.bookshop.repository.AddressRepository;
import ptit.edu.vn.bookshop.service.AddressService;
import ptit.edu.vn.bookshop.service.UserService;
import ptit.edu.vn.bookshop.domain.dto.mapper.AddressMapper;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, UserService userService, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.addressMapper = addressMapper;
    }


    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO addressRequestDTO) {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        User user = this.userService.getUserByUsername(email);
        Address address = new Address();
        address.setReceiverName(addressRequestDTO.getReceiverName());
        address.setPhone(addressRequestDTO.getPhone());
        address.setStreet(addressRequestDTO.getStreet());
        address.setWard(addressRequestDTO.getWard());
        address.setDistrict(addressRequestDTO.getDistrict());
        address.setCity(addressRequestDTO.getCity());
        address.setStatus(StatusEnum.ACTIVE);

        //  Kiểm tra user đã có địa chỉ hay chưa
        boolean hasAnyAddress = this.addressRepository.existsByUser(user);
        if (!hasAnyAddress) {
            address.setIsDefault(true);
            }
        // khi tạo địa chỉ mới và lấy địa chỉ đấy làm mặc dịnh thì đổi các địa chỉ còn lại là false
        else if (Boolean.TRUE.equals(addressRequestDTO.getIsDefault())) {
            this.addressRepository.updateIsDefaultFalseByUser(user);
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }
        address.setUser(user);

        this.addressRepository.save(address);
        return this.addressMapper.toDto(address);
    }

    @Override
    public AddressResponseDTO updateAddress(AddressRequestDTO addressRequestDTO, Long id) {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        User user = this.userService.getUserByUsername(email);
        Address address = this.addressRepository.findById(id).orElseThrow(()-> new IdInvalidException("Address not found."));
        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to update this address.");
        }
        address.setReceiverName(addressRequestDTO.getReceiverName());
        address.setPhone(addressRequestDTO.getPhone());
        address.setStreet(addressRequestDTO.getStreet());
        address.setWard(addressRequestDTO.getWard());
        address.setDistrict(addressRequestDTO.getDistrict());
        address.setCity(addressRequestDTO.getCity());

        if (Boolean.TRUE.equals(addressRequestDTO.getIsDefault())) {
            // Bỏ mặc định của các địa chỉ khác của user
            this.addressRepository.updateIsDefaultFalseByUser(user);
            address.setIsDefault(true);
        } else {
            // Nếu không gửi isDefault hoặc gửi false thì giữ nguyên giá trị cũ
            if (address.getIsDefault() == null) {
                address.setIsDefault(false);
            }
        }
        address.setUser(user);

        this.addressRepository.save(address);
        return this.addressMapper.toDto(address);
    }

    @Override
    public void deleteAddress(Long id)  {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        User user = this.userService.getUserByUsername(email);
        Address address = this.addressRepository.findById(id).orElseThrow(()-> new IdInvalidException("Address not found."));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this address.");
        }
        // không thể xóa địa chỉ mặc định
        if(Boolean.TRUE.equals(address.getIsDefault())){
            throw new BadRequestException("The default address cannot be deleted.");
        }
        address.setStatus(StatusEnum.DELETED);
        this.addressRepository.save(address);
    }

    @Override
    public AddressResponseDTO getAddress(Long id) {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        User user = this.userService.getUserByUsername(email);
        Address address = this.addressRepository.findById(id).orElseThrow(()-> new IdInvalidException("Address not found."));
        if(!address.getStatus().equals(StatusEnum.ACTIVE)){
            throw new IllegalStateException("Address status is not ACTIVE");
        }
        return this.addressMapper.toDto(address);
    }

    @Override
    public AddressPageResponseDTO getAllAddresses() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        User user = this.userService.getUserByUsername(email);

        List<Address> addresses = this.addressRepository.findByUser(user);

        List<AddressResponseDTO> addressResponseDTOs = addresses.stream()
                .filter(address -> address.getStatus() == StatusEnum.ACTIVE)
                .map(addressMapper::toDto)
                .collect(Collectors.toList());

        AddressPageResponseDTO responseDTO = new AddressPageResponseDTO();
        responseDTO.setAddresses(addressResponseDTOs);
        responseDTO.setTotal(addressResponseDTOs.size());
        return responseDTO;
    }

    @Override
    public Address getAddressByIsDefault() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        User user = this.userService.getUserByUsername(email);
        Address address = this.addressRepository.findByUserAndIsDefaultTrue(user)
                .orElseThrow(() -> new IllegalArgumentException("Address not found for user"));
        return address;
    }

}
