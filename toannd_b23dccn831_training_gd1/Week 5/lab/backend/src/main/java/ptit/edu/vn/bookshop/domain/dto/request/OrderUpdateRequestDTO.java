package ptit.edu.vn.bookshop.domain.dto.request;

import lombok.Getter;

@Getter
public class OrderUpdateRequestDTO {
    private String receiverName;
    private String receiverPhone;
    private String city;
    private String district;
    private String ward;
    private String street;
    private String note;
}
