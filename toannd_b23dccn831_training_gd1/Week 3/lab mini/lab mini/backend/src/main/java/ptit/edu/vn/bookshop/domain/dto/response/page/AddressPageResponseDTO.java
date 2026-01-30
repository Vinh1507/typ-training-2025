package ptit.edu.vn.bookshop.domain.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.dto.response.AddressResponseDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressPageResponseDTO {
    private Integer total;
    private List<AddressResponseDTO> addresses;
}
