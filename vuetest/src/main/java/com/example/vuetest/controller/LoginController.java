package com.example.vuetest.controller;

import com.example.vuetest.pojo.Admin;
import com.example.vuetest.pojo.User;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import com.example.vuetest.service.AdminService;
import com.example.vuetest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;


import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;

    @CrossOrigin
    @PostMapping(value = "/api/login")
    @ResponseBody //requestbody only for post
    public Result login(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("identity") String identity,HttpSession session) {
          System.out.println("!!!login!!!");
          System.out.println(session.getId());
//        System.out.println(identity);

        username = HtmlUtils.htmlEscape(username);
        password = HtmlUtils.htmlEscape(password);
        identity = HtmlUtils.htmlEscape(identity);

        String username1= (String) session.getAttribute("username");
//        System.out.println("1:"+username1);
        String identity1= (String) session.getAttribute("identity");
//        System.out.println("2:"+identity1);

        if(identity.equals("普通用户"))
        {
            User user = userService.get(username, password);
            int userid =user.getId();
            session.setAttribute("userid", userid);
            System.out.println(session.getAttribute("userid"));
           System.out.println("4:"+user);
            if (null == user) {
                return ResultFactory.buildFailResult("密码错误");
            }
            else {
                return ResultFactory.buildSuccessResult(null);
            }
        }
        else
        {
            Admin admin = adminService.get(username, password);
//            System.out.println("4:"+admin);
            if (null == admin) {
                return ResultFactory.buildFailResult("密码错误");
            }
            else {
                return ResultFactory.buildSuccessResult(null);
            }
        }
    }


    @CrossOrigin
    @PostMapping("/api/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        int status = userService.register(user);
        switch (status) {
            case 0:
                return ResultFactory.buildFailResult("用户名和密码不能为空");
            case 1:
                return ResultFactory.buildSuccessResult("注册成功");
            case 2:
                return ResultFactory.buildFailResult("用户已存在");
        }
        return ResultFactory.buildFailResult("未知错误");
    }




    @CrossOrigin
    @PostMapping("/api/index")
    @ResponseBody
    public Result index(@RequestBody User user,HttpSession session) {
        String username= (String) session.getAttribute("username");
        System.out.println(username);
        return ResultFactory.buildSuccessResult(username);
    }
}
