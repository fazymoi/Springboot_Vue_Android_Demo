package com.example.vuetest.service;

import com.example.vuetest.dao.UserDAO;
import com.example.vuetest.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public boolean isExist(String username) {
        User user = getByName(username);
        return null!=user;
    }

    public User getByName(String username) {
        return userDAO.findByUsername(username);
    }

    public User get(String username, String password){
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public void setpassword(int userid,String password){
        User user=userDAO.getById(userid);
        user.setPassword(password);
        userDAO.save(user);
    }

    public User updateinfo(int userid,String nickname,String eaddress){
        User user=userDAO.getById(userid);
        user.setNickname(nickname);
        user.setEaddress(eaddress);
        userDAO.save(user);
        return user;
    }

    public void deleteUser(int id) {
        userDAO.deleteById(id);
    }

    public void updateimg(String username,String cover) {
        User user=userDAO.findByUsername(username);
        user.setCover(cover);
        userDAO.save(user);
    }

    public void add(User user) {
        userDAO.save(user);
    }
    public int register(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        if (username.equals("") || password.equals("")) {
            return 0;
        }

        boolean exist = isExist(username);

        if (exist) {
            return 2;
        }

        // 默认生成 16 位盐

        user.setPassword(password);

        userDAO.save(user);

        return 1;
    }

    public User findById(int userid) {
        return userDAO.getById(userid);
    }

    public User updateinfo(int userid, String nickname, String eaddress, String cover) {
        User user=userDAO.getById(userid);
        user.setNickname(nickname);
        user.setEaddress(eaddress);
        user.setCover(cover);
        userDAO.save(user);
        return user;
    }
}
