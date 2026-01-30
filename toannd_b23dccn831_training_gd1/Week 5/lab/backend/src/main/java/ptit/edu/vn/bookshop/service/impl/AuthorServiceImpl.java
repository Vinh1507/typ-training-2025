package ptit.edu.vn.bookshop.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.AuthorRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.AuthorResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.AuthorPageResponseDTO;
import ptit.edu.vn.bookshop.repository.specification.AuthorSpecificationBuilder;
import ptit.edu.vn.bookshop.domain.entity.Author;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.repository.AuthorRepository;
import ptit.edu.vn.bookshop.service.AuthorService;
import ptit.edu.vn.bookshop.domain.dto.mapper.AuthorMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorResponseDTO createAuthor(AuthorRequestDTO authorRequestDTO) {
        Author author = this.authorMapper.toEntity(authorRequestDTO);
        return this.authorMapper.toResponseDTO(this.authorRepository.save(author));
    }

    @Override
    public AuthorResponseDTO updateAuthor(AuthorRequestDTO authorRequestDTO, Long id) {
        Optional<Author> authorOptional = this.authorRepository.findById(id);
        if (!authorOptional.isPresent()) {
            throw new IdInvalidException("Author id not found");
        }
        Author author = authorOptional.get();
        if (authorRequestDTO.getName() != null) author.setName(authorRequestDTO.getName());
        if (authorRequestDTO.getDateOfBirth() != null) author.setDateOfBirth(authorRequestDTO.getDateOfBirth());
        if (authorRequestDTO.getGender() != null) author.setGender(authorRequestDTO.getGender());
        if (authorRequestDTO.getCountry() != null) author.setCountry(authorRequestDTO.getCountry());
        if (authorRequestDTO.getBiography() != null) author.setBiography(authorRequestDTO.getBiography());
        if (authorRequestDTO.getStatus() != null) author.setStatus(authorRequestDTO.getStatus());
        return this.authorMapper.toResponseDTO(this.authorRepository.save(author));
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = this.authorRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Author does not exist"));
        author.setStatus(StatusEnum.DELETED);
        this.authorRepository.save(author);
    }

    @Override
    public AuthorResponseDTO fetchAuthor(Long id, boolean isAdmin) {
        Optional<Author> authorOptional = this.authorRepository.findById(id);
        if (!authorOptional.isPresent()) {
            throw new IdInvalidException("Author id not found");
        }
        Author author = authorOptional.get();
        if(!isAdmin && author.getStatus().equals(StatusEnum.DELETED)){
                throw new IllegalStateException("Author does not exist");
        }
        return this.authorMapper.toResponseDTO(author);
    }

    @Override
    public AuthorPageResponseDTO fetchAllAuthors(Pageable pageable, String[] author, boolean isAdmin) {
        AuthorSpecificationBuilder builder = new AuthorSpecificationBuilder();
        if (author != null && author.length > 0) {
            for (String a : author) {
                Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
                Matcher matcher = pattern.matcher(a);
                if (matcher.find()) {
                    builder.with(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5));
                }
            }
        }
        if (!isAdmin) {
            builder.with("status", ":", StatusEnum.ACTIVE.name(), "", "");
        }
        Page<Author> authorPage = this.authorRepository.findAll(builder.build(), pageable);
        AuthorPageResponseDTO responseDTO = new AuthorPageResponseDTO();
        responseDTO.setPage(pageable.getPageNumber() + 1);
        responseDTO.setPageSize(pageable.getPageSize());
        responseDTO.setTotal(authorPage.getTotalElements());
        responseDTO.setPages(authorPage.getTotalPages());
        responseDTO.setAuthors(authorPage.stream().map(authorMapper::toResponseDTO).collect(Collectors.toList()));
        return responseDTO;
    }
}
