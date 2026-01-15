package com.vn.Project_Trainning_Week2.service.impl;


import com.vn.Project_Trainning_Week2.dto.request.RequestDTO;
import com.vn.Project_Trainning_Week2.entity.User;
import com.vn.Project_Trainning_Week2.repository.UserRepository;
import com.vn.Project_Trainning_Week2.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    UserRepository userRepository;

    @Override
    public List<User> getListUser() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserDetail(Long id) {
        Optional<User> userOptional =  this.userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public User addNewUser(RequestDTO requestUserDTO) {
        User user = new User(
                requestUserDTO.getName(),
                requestUserDTO.getAddress(),
                requestUserDTO.getGender()
        );
        return this.userRepository.save(user);
    }

    @Override
    public User updateUser(RequestDTO requestUserDTO, long id) {
        User user = getUserDetail(id);
        user.setName(requestUserDTO.getName());
        user.setAddress(requestUserDTO.getAddress());
        user.setGender(requestUserDTO.getGender());
        return this.userRepository.save(user);

    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
    
}
