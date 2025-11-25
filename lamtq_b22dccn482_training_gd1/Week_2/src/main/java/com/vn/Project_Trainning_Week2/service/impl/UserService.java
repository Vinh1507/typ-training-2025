package com.vn.Project_Trainning_Week2.service.impl;

import com.vn.Project_Trainning_Week2.db_fake.Data;
import com.vn.Project_Trainning_Week2.dto.request.RequestDTO;
import com.vn.Project_Trainning_Week2.entity.User;
import com.vn.Project_Trainning_Week2.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Override
    public List<User> getListUser() {
        return Data.listUser;
    }

    @Override
    public User getUserDetail(Long id) {
        return Data.listUser.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    @Override
    public User addNewUser(RequestDTO requestUserDTO) {
        User user = new User(requestUserDTO.getId(),requestUserDTO.getName(),requestUserDTO.getAddress(),requestUserDTO.getGender());
        System.out.println(user);
        Data.listUser.add(user);
        return user;
    }

    @Override
    public User updateUser(RequestDTO requestUserDTO) {
        User user = getUserDetail(requestUserDTO.getId());
        user.setName(requestUserDTO.getName());
        user.setAddress(requestUserDTO.getAddress());
        user.setGender(requestUserDTO.getGender());
        int idx = Data.listUser.indexOf(user);
        Data.listUser.set(idx,user);
        return user;

    }

    @Override
    public void deleteUser(Long id) {
        int idx = Data.listUser.indexOf(getUserDetail(id));
        Data.listUser.remove(idx);
    }
    
}
