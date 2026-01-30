package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.PublisherRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.PublisherResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.PublisherPageResponseDTO;
import ptit.edu.vn.bookshop.service.PublisherService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/publishers")
    @ApiMessage("Publisher created successfully")
    public ResponseEntity<PublisherResponseDTO> createPublisher(@Valid @RequestBody PublisherRequestDTO publisherRequestDTO) {
        PublisherResponseDTO publisherResponseDTO = this.publisherService.createPublisher(publisherRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherResponseDTO);
    }

    @PutMapping("/publishers/{id}")
    @ApiMessage("Publisher updated successfully")
    public ResponseEntity<PublisherResponseDTO> updatePublisher(@Valid @RequestBody PublisherRequestDTO publisherRequestDTO, @PathVariable Long id) {
        PublisherResponseDTO publisherResponseDTO = this.publisherService.updatePublisher(publisherRequestDTO, id);
        return ResponseEntity.ok().body(publisherResponseDTO);
    }

    @DeleteMapping("/publishers/{id}")
    @ApiMessage("Publisher deleted successfully")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        this.publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publishers/{id}")
    @ApiMessage("Publisher retrieved successfully")
    public ResponseEntity<PublisherResponseDTO> getPublisher(@PathVariable Long id) {
        PublisherResponseDTO publisherResponseDTO = this.publisherService.fetchPublisher(id, false);
        return ResponseEntity.ok().body(publisherResponseDTO);
    }

    @GetMapping("/publishers")
    @ApiMessage("Publishers retrieved successfully")
    public ResponseEntity<PublisherPageResponseDTO> getAllPublishers(Pageable pageable,
                                                                     @RequestParam(required = false) String[] publisher) {
        PublisherPageResponseDTO responseDTO = this.publisherService.fetchAllPublishers(pageable, publisher, false);
        return ResponseEntity.ok().body(responseDTO);
    }

    // Admin endpoints
    @GetMapping("/admin/publishers/{id}")
    @ApiMessage("Publisher details retrieved by admin")
    public ResponseEntity<PublisherResponseDTO> getPublisherByAdmin(@PathVariable Long id) {
        PublisherResponseDTO publisherResponseDTO = this.publisherService.fetchPublisher(id, true);
        return ResponseEntity.ok().body(publisherResponseDTO);
    }

    @GetMapping("/admin/publishers")
    @ApiMessage("Publishers list retrieved by admin")
    public ResponseEntity<PublisherPageResponseDTO> getAllPublishersByAdmin(Pageable pageable,
                                                                            @RequestParam(required = false) String[] publisher) {
        PublisherPageResponseDTO responseDTO = this.publisherService.fetchAllPublishers(pageable, publisher, true);
        return ResponseEntity.ok().body(responseDTO);
    }
}