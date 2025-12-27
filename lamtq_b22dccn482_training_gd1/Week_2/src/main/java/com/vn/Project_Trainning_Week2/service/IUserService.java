package com.vn.Project_Trainning_Week2.service;

import com.vn.Project_Trainning_Week2.dto.request.RequestDTO;
import com.vn.Project_Trainning_Week2.entity.User;

import java.util.List;

public interface IUserService {
    List<User> getListUser();
    User getUserDetail(Long id);
    User addNewUser(RequestDTO requestUserDTO);
    User updateUser(RequestDTO requestUserDTO, long id);
    void deleteUser(Long id);
}
