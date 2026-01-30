package ptit.edu.vn.bookshop.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {
    private String fileName;
    private String urlFile;
    private Instant uploadAt;
}
