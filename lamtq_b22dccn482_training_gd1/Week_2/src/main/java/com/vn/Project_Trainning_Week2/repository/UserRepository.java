package com.vn.Project_Trainning_Week2.repository;

import com.vn.Project_Trainning_Week2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    void deleteById(long id);
}
