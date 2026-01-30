package ptit.edu.vn.bookshop.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.PublisherRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.PublisherResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.PublisherPageResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Publisher;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.repository.PublisherRepository;
import ptit.edu.vn.bookshop.repository.specification.PublisherSpecificationBuilder;
import ptit.edu.vn.bookshop.service.PublisherService;
import org.springframework.stereotype.Service;
import ptit.edu.vn.bookshop.domain.dto.mapper.PublisherMapper;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherServiceImpl(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    @Override
    public PublisherResponseDTO createPublisher(PublisherRequestDTO publisherRequestDTO) {
        if (publisherRequestDTO.getEmail() != null && this.publisherRepository.existsByEmail(publisherRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Publisher publisher = this.publisherMapper.toEntity(publisherRequestDTO);
        return this.publisherMapper.toResponseDto(this.publisherRepository.save(publisher));
    }

    @Override
    public PublisherResponseDTO updatePublisher(PublisherRequestDTO publisherRequestDTO, Long id) {
        Optional<Publisher> publisherOptional = this.publisherRepository.findById(id);
        if (!publisherOptional.isPresent()) {
            throw new IdInvalidException("Publisher not found");
        }

        Publisher publisher = publisherOptional.get();

        if (publisherRequestDTO.getEmail() != null && !publisherRequestDTO.getEmail().equals(publisher.getEmail())
                && this.publisherRepository.existsByEmail(publisherRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (publisherRequestDTO.getName() != null) publisher.setName(publisherRequestDTO.getName());
        if (publisherRequestDTO.getAddress() != null) publisher.setAddress(publisherRequestDTO.getAddress());
        if (publisherRequestDTO.getEmail() != null) publisher.setEmail(publisherRequestDTO.getEmail());
        if (publisherRequestDTO.getPhone() != null) publisher.setPhone(publisherRequestDTO.getPhone());
        if (publisherRequestDTO.getStatus() != null) publisher.setStatus(publisherRequestDTO.getStatus());
        return this.publisherMapper.toResponseDto(this.publisherRepository.save(publisher));
    }

    @Override
    public void deletePublisher(Long id) {
        Publisher publisher = this.publisherRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Publisher not found"));
        publisher.setStatus(StatusEnum.DELETED);
        this.publisherRepository.save(publisher);
    }


    @Override
    public PublisherResponseDTO fetchPublisher(Long id, boolean isAdmin) {
        Publisher publisher = this.publisherRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Publisher not found"));
        if(!isAdmin && publisher.getStatus().equals(StatusEnum.DELETED)){
            throw new IllegalStateException("Publisher not found");
        }
        return this.publisherMapper.toResponseDto(publisher);
    }


    @Override
    public PublisherPageResponseDTO fetchAllPublishers(Pageable pageable, String[] filters, boolean isAdmin) {
        PublisherSpecificationBuilder builder = new PublisherSpecificationBuilder();
        // Build filter từ query string nếu có
        if (filters != null && filters.length > 0) {
            Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
            for (String f : filters) {
                Matcher matcher = pattern.matcher(f);
                if (matcher.find()) {
                    builder.with(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5)
                    );
                }
            }
        }
        if (!isAdmin) {
            builder.with("status", ":", StatusEnum.ACTIVE.name(), "", "");
        }
        Page<Publisher> publisherPage = this.publisherRepository.findAll(builder.build(), pageable);
        PublisherPageResponseDTO res = new PublisherPageResponseDTO();
        res.setPage(publisherPage.getNumber() + 1);
        res.setPages(publisherPage.getTotalPages());
        res.setPageSize(publisherPage.getSize());
        res.setTotal(publisherPage.getTotalElements());
        res.setPublishers(publisherPage.stream().map(publisherMapper::toResponseDto).collect(Collectors.toList()));
        return res;
    }

}
