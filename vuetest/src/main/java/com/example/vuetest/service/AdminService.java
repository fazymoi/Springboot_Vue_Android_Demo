package com.example.vuetest.service;

import com.example.vuetest.dao.AdminDAO;
import com.example.vuetest.pojo.Admin;
import com.example.vuetest.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminDAO adminDAO;

    public boolean isExist(String username) {
        Admin admin = getByName(username);
        return null!=admin;
    }


    public Admin getByName(String username) {
        return adminDAO.findByUsername(username);
    }

    public Admin get(String username, String password){
        return adminDAO.getByUsernameAndPassword(username, password);
    }


    public void add(Admin admin) {
        adminDAO.save(admin);
    }
}
