package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.User.AddressRequest;
import com.BTL_JAVA.BTL.DTO.Response.User.AddressResponse;
import com.BTL_JAVA.BTL.Entity.Address;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.AddressRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {

    AddressRepository addressRepository;
    UserRepository userRepository;

    public AddressResponse getDefaultAddress() {
        try {
            User user = getCurrentUser();
            Optional<Address> defaultAddress = addressRepository.findByUserAndIsDefaultTrue(user);

            return defaultAddress.map(this::toAddressResponse)
                    .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
    }

    public List<AddressResponse> getAllAddresses() {
        try {
            User user = getCurrentUser();
            List<Address> addresses = addressRepository.findByUser(user);
            return addresses.stream()
                    .map(this::toAddressResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
    }

    public AddressResponse createAddress(AddressRequest request) {
        try {
            User user = getCurrentUser();

            List<Address> userAddresses = addressRepository.findByUser(user);

            boolean shouldSetDefault = userAddresses.isEmpty() || request.isDefaultAddress();

            if (shouldSetDefault) {
                userAddresses.forEach(addr -> addr.setDefault(false));
                addressRepository.saveAll(userAddresses);
            }

            Address address = Address.builder()
                    .street(request.getStreet())
                    .ward(request.getWard())
                    .city(request.getCity())
                    .isDefault(shouldSetDefault)
                    .user(user)
                    .build();

            Address savedAddress = addressRepository.save(address);
            return toAddressResponse(savedAddress);

        } catch (Exception e) {
            throw new AppException(ErrorCode.CREATE_FAILED);
        }
    }

    public AddressResponse updateAddress(Integer addressId, AddressRequest request) {
        try {
            User user = getCurrentUser();
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

            if (address.getUser().getId() != user.getId()) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            if (request.getStreet() != null) {
                address.setStreet(request.getStreet());
            }
            if (request.getWard() != null) {
                address.setWard(request.getWard());
            }
            if (request.getCity() != null) {
                address.setCity(request.getCity());
            }

            if (request.isDefaultAddress() && !address.isDefault()) {
                List<Address> userAddresses = addressRepository.findByUser(user);
                userAddresses.forEach(addr -> addr.setDefault(false));
                addressRepository.saveAll(userAddresses);
                address.setDefault(true);
            }
            else if (!request.isDefaultAddress() && address.isDefault()) {
                List<Address> userAddresses = addressRepository.findByUser(user);
                long defaultCount = userAddresses.stream().filter(Address::isDefault).count();

                if (defaultCount <= 1) {
                    throw new AppException(ErrorCode.CANNOT_REMOVE_DEFAULT_ADDRESS);
                }
                address.setDefault(false);
            }

            Address updatedAddress = addressRepository.save(address);
            return toAddressResponse(updatedAddress);

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.UPDATE_FAILED);
        }
    }


    public void deleteAddress(Integer addressId) {
        try {
            User user = getCurrentUser();
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

            if (address.getUser().getId() != user.getId()) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            addressRepository.delete(address);

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }

    private User getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByFullName(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return user;
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .address_id(address.getId())
                .street(address.getStreet())
                .ward(address.getWard())
                .city(address.getCity())
                .is_default(address.isDefault())
                .build();
    }
}