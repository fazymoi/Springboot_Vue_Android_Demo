package com.example.vuetest.dao;

import com.example.vuetest.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDAO extends JpaRepository<Admin,Integer> {
    Admin findByUsername(String username);

    Admin getByUsernameAndPassword(String username,String password);
}
