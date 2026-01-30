package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.OrderCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.OrderUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.UpdateStatusRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.OrderResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.OrderPageResponseDTO;
import ptit.edu.vn.bookshop.service.OrderService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;


@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @ApiMessage("Create new order")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreateRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = this.orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    @PutMapping("/orders/{id}")
    @ApiMessage("")
    public ResponseEntity<OrderResponseDTO> getOrder(@Valid @RequestBody OrderUpdateRequestDTO orderUpdateRequestDTO, @PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = this.orderService.updateOrder(orderUpdateRequestDTO, id);
        return ResponseEntity.ok().body(orderResponseDTO);
    }

    @DeleteMapping("/orders/{id}")
    @ApiMessage("")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        this.orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/{id}")
    @ApiMessage("")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = this.orderService.getOrder(id);
        return ResponseEntity.ok().body(orderResponseDTO);
    }

    //user
    //cho xem danh sach theo tung trang thai cua don hang
    @GetMapping("/orders")
    @ApiMessage("")
    public ResponseEntity<OrderPageResponseDTO> getAllOrders(Pageable pageable,
                                                             @RequestParam(required = false) String[] orders) {
        OrderPageResponseDTO orderPageResponseDTO = this.orderService.getAllOrders(pageable, orders);
        return ResponseEntity.ok().body(orderPageResponseDTO);
    }

    // cap nhap trang thai don hang
    @PatchMapping("/orders/{id}/status")
    @ApiMessage("")
    public ResponseEntity<OrderResponseDTO> updateStatusOrder(
            @Valid @RequestBody UpdateStatusRequestDTO updateStatusRequestDTO,
            @PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = this.orderService.updateOrderStatus(updateStatusRequestDTO, id);
        return ResponseEntity.ok().body(orderResponseDTO);
    }
    //admin co quyen cap nhap full thong tin don hang


}
