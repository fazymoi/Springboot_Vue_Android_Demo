package com.example.vuetest.dao;

import com.example.vuetest.pojo.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UserDAO extends JpaRepository<User,Integer> {
    User findByUsername(String username);

    User getById(int userid);

    User getByUsernameAndPassword(String username,String password);

    @Modifying
    @Transactional
    void deleteById(int id);
}
