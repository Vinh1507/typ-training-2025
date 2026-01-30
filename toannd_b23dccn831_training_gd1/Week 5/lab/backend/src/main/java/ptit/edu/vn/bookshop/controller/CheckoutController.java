package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.CheckoutRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CheckoutResponseDTO;
import ptit.edu.vn.bookshop.service.CheckoutService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class CheckoutController {

    private final CheckoutService checkOutService;

    public CheckoutController(CheckoutService checkOutService) {
        this.checkOutService = checkOutService;
    }

    @GetMapping("/checkout")
    @ApiMessage("Checkout information retrieved successfully")
    public ResponseEntity<CheckoutResponseDTO> checkout(@Valid @RequestBody CheckoutRequestDTO checkoutRequestDTO) {
        CheckoutResponseDTO checkOutResponseDTO = this.checkOutService.getCheckout(checkoutRequestDTO);
        return ResponseEntity.ok().body(checkOutResponseDTO);
    }

}