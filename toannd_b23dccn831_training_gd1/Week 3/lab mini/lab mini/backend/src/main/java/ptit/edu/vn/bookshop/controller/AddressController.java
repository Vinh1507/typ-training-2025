package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.AddressRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.AddressResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.AddressPageResponseDTO;
import ptit.edu.vn.bookshop.service.AddressService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    @ApiMessage("Create address successfully")
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO addressResponseDTO = this.addressService.createAddress(addressRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponseDTO);
    }

    @PutMapping("/addresses/{id}")
    @ApiMessage("Update address successfully")
    public ResponseEntity<AddressResponseDTO>  updateAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO,
                                                             @PathVariable Long id) {
        AddressResponseDTO addressResponseDTO = this.addressService.updateAddress(addressRequestDTO, id);
        return ResponseEntity.ok().body(addressResponseDTO);
    }

    @DeleteMapping("/addresses/{id}")
    @ApiMessage("Delete address successfully")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        this.addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/addresses/{id}")
    @ApiMessage("Get address detail successfully")
    public ResponseEntity<AddressResponseDTO> getAddress(@PathVariable Long id) {
        AddressResponseDTO addressResponseDTO = this.addressService.getAddress(id);
        return ResponseEntity.ok().body(addressResponseDTO);
    }

    @GetMapping("/addresses")
    @ApiMessage("Get all addresses successfully")
    public ResponseEntity<AddressPageResponseDTO> getAllAddresses() {
        AddressPageResponseDTO addressPageResponseDTO = this.addressService.getAllAddresses();
        return ResponseEntity.ok().body(addressPageResponseDTO);
    }
}