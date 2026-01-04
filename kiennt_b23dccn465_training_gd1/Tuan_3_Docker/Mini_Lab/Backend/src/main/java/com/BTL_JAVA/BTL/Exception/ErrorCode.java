package com.BTL_JAVA.BTL.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // ===== General/User (1xxx) =====
    INVALID_KEY(1001, "invalid key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not exists", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Khong co quyen truy cap", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "You age must be at leats {min}", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(1009, "Review is not exists", HttpStatus.NOT_FOUND),

    // Order / Product basic
    CART_IS_EMPTY(1010, "Cart is empty. Cannot create an order.", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(1011, "The selected address was not found.", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(1012, "The requested order was not found.", HttpStatus.NOT_FOUND),
    CANNOT_CANCEL_ORDER(1013, "This order cannot be canceled.", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1014, "Product not found.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK(1015, "Insufficient stock for this product.", HttpStatus.BAD_REQUEST),
    FEEDBACK_NOT_FOUND(1016, "Feedback not found.", HttpStatus.NOT_FOUND),
    ALREADY_FEEDBACKED(1017, "Ban da danh gia san pham nay roi.", HttpStatus.BAD_REQUEST),
    NOT_PURCHASED_PRODUCT(1018, "Ban chua mua san pham nay.", HttpStatus.BAD_REQUEST),

    // Variation / Category (102x)
    INVALID_VARIATION(1020, "Variation is invalid", HttpStatus.BAD_REQUEST),
    DUPLICATE_VARIATION(1021, "Duplicate variation", HttpStatus.BAD_REQUEST),
    VARIATION_NOT_FOUND(1022, "Variation is not exists", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(1023, "Category is not exists", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(1024, "Product already exists", HttpStatus.BAD_REQUEST),
    VARIATION_EXISTED(1025, "Variation already exists", HttpStatus.BAD_REQUEST),

    // ===== Sales (2xxx) =====
    SALE_NOT_EXISTED(2001, "Sale khong ton tai", HttpStatus.NOT_FOUND),
    SALES_NOT_FOUND(2002, "Khong tim thay Sale", HttpStatus.NOT_FOUND),
    INVALID_SALE_DATE(2003, "End date khong the truoc Start date", HttpStatus.BAD_REQUEST),
    INVALID_SALE_VALUE(2004, "Value khong duoc am", HttpStatus.BAD_REQUEST),
    INVALID_SALE_NAME(2005, "Bat buoc phai co ten", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_IN_SALE(2006, "San pham da ton tai trong Sale", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_IN_SALE(2007, "Khong tim thay san pham trong Sale", HttpStatus.BAD_REQUEST),
    ADD_TO_SALE_FAILED(2008, "Them san pham that bai", HttpStatus.INTERNAL_SERVER_ERROR),
    REMOVE_FROM_SALE_FAILED(2009, "Xoa san pham that bai", HttpStatus.INTERNAL_SERVER_ERROR),
    SALE_OVERLAPPING(2010, "Da co sale hoat dong trong thoi gian nay", HttpStatus.BAD_REQUEST),

    // ===== CRUD generic (4xxx) =====
    CREATE_FAILED(4001, "Tao that bai", HttpStatus.INTERNAL_SERVER_ERROR),
    UPDATE_FAILED(4002, "Cap nhat that bai", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_FAILED(4003, "Xoa that bai", HttpStatus.INTERNAL_SERVER_ERROR),

    // ===== Cart (5xxx) =====
    CART_NOT_FOUND(5001, "Khong thay cart", HttpStatus.NOT_FOUND),
    INVALID_QUANTITY(5002, "So luong phai lon hon 0", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIATION_NOT_FOUND(5003, "Khong tim thay variation", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_STOCK(5004, "Trong kho khong du", HttpStatus.BAD_REQUEST),
    CART_UPDATE_FAILED(5005, "Update that bai", HttpStatus.INTERNAL_SERVER_ERROR),

    // ===== Address (6xxx) =====
    CANNOT_REMOVE_DEFAULT_ADDRESS(6007, "Khong cap nhat duoc do khong co DefaultAddress.", HttpStatus.BAD_REQUEST),

    // ===== Misc (9xxx) =====
    UNCATEGORIED_EXCEPTION(9999, "Khong xac dinh", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
