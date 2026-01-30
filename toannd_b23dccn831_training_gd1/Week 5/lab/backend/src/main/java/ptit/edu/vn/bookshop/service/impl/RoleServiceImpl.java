package ptit.edu.vn.bookshop.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestClient;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.RoleCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.RoleUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.RolePageResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.RoleResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Permission;
import ptit.edu.vn.bookshop.domain.entity.Role;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.domain.dto.mapper.RoleMapper;
import ptit.edu.vn.bookshop.repository.PermissionRepository;
import ptit.edu.vn.bookshop.repository.RoleRepository;
import ptit.edu.vn.bookshop.repository.specification.RoleSpecificationBuilder;
import ptit.edu.vn.bookshop.service.RoleService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper, RestClient.Builder builder) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleResponseDTO createRole(RoleCreateRequestDTO roleCreateRequestDTO) {
        if (this.roleRepository.existsByName(roleCreateRequestDTO.getName())) {
            throw new DataIntegrityViolationException("Role already exists with same name!");
        }
        Role role = this.roleMapper.toEntity(roleCreateRequestDTO);
        role.setName(roleCreateRequestDTO.getName().toUpperCase());
        if (roleCreateRequestDTO.getPermissions() != null && !roleCreateRequestDTO.getPermissions().isEmpty()) {
            List<Long> permissionIds = roleCreateRequestDTO
                    .getPermissions()
                    .stream()
                    .map(RoleCreateRequestDTO.RolePermissionRequestDTO::getId)
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = permissionRepository.findByIdIn(permissionIds);
            role.setPermissions(dbPermissions);
        }
        return this.roleMapper.toResponseDto(this.roleRepository.save(role));
    }

    @Override
    public RoleResponseDTO updateRole(RoleUpdateRequestDTO roleRequestDTO, Long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (!roleOptional.isPresent()) {
            throw new IdInvalidException("Role does not exist!");
        }
        Role role = roleOptional.get();
        if (roleRequestDTO.getDescription() != null) role.setDescription(roleRequestDTO.getDescription());
        if (roleRequestDTO.getStatus() != null) role.setStatus(roleRequestDTO.getStatus());
        if (roleRequestDTO.getPermissions() != null) {
            List<Long> permissions = roleRequestDTO.getPermissions()
                    .stream()
                    .map(RoleCreateRequestDTO.RolePermissionRequestDTO::getId)
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = permissionRepository.findByIdIn(permissions);
            role.setPermissions(dbPermissions);
        }
        return this.roleMapper.toResponseDto(this.roleRepository.save(role));
    }

    @Override
    public RoleResponseDTO fetchRole(Long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (!roleOptional.isPresent()) {
            throw new IdInvalidException("Role does not exist!");
        }
        Role role = roleOptional.get();
        return this.roleMapper.toResponseDto(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = this.roleRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Role does not exist!"));
        role.setStatus(StatusEnum.DELETED);
        this.roleRepository.save(role);
    }

    @Override
    public RolePageResponseDTO fetchAllRoles(Pageable pageable, String[] role) {
        Page<Role> rolePage;
        if (role != null && role.length > 0) {
            RoleSpecificationBuilder builder = new RoleSpecificationBuilder();
            for (String r : role) {
                Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
                Matcher matcher = pattern.matcher(r);
                if (matcher.find()) {
                    builder.with(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5));
                }
            }
            rolePage = this.roleRepository.findAll(builder.build(), pageable);
        } else {
            rolePage = this.roleRepository.findAll(pageable);
        }
        RolePageResponseDTO response = new RolePageResponseDTO();
        response.setPage(rolePage.getNumber() + 1);
        response.setPageSize(rolePage.getSize());
        response.setPages(rolePage.getTotalPages());
        response.setTotal(rolePage.getTotalElements());
        response.setRoles(rolePage.getContent().stream().map(roleMapper::toResponseDto).collect(Collectors.toList()));
        return response;
    }

}
