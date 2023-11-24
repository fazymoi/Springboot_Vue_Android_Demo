package com.example.vuetest.controller;

import com.example.vuetest.dao.UserDAO;
import com.example.vuetest.pojo.User;
import com.example.vuetest.result.Result;
import com.example.vuetest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import org.springframework.web.util.HtmlUtils;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

@Controller
public class PersonalController {
    @Autowired
    UserService userService;

    @Autowired
    UserDAO userDAO;

    @CrossOrigin
    @PostMapping(value = "/api/information")
    @ResponseBody //requestbody only for post
    public User information(HttpSession session) throws Exception
    {
        int userid= (int) session.getAttribute("userid");
//        System.out.println(userid);
        User user=userDAO.getById(userid);
//        System.out.println(user);
        return user;
    }

    @CrossOrigin
    @PostMapping(value = "/api/updateinfo")
    @ResponseBody //requestbody only for post
    public User updateinfo(@RequestParam("nickname") String nickname,@RequestParam("eaddress") String eaddress,HttpSession session) throws Exception
    {
        nickname = HtmlUtils.htmlEscape(nickname);
        eaddress = HtmlUtils.htmlEscape(eaddress);
        System.out.println(nickname);
        System.out.println(eaddress);

        int userid= (int) session.getAttribute("userid");
        User user=userService.updateinfo(userid,nickname,eaddress);
        System.out.println(user);
        return user;
    }
    @CrossOrigin
    @PostMapping(value = "/api/updateinfo2")
    @ResponseBody //requestbody only for post
    public User updateinfo2(
            @RequestParam("nickname") String nickname,
            @RequestParam("eaddress") String eaddress,
            @RequestParam("cover") String cover,
            HttpSession session) throws Exception
    {
        nickname = HtmlUtils.htmlEscape(nickname);
        eaddress = HtmlUtils.htmlEscape(eaddress);
        System.out.println(nickname);
        System.out.println(eaddress);
        if(nickname==""||eaddress=="")
            return null;
        int userid= (int) session.getAttribute("userid");
        User user=userService.updateinfo(userid,nickname,eaddress,cover);
        System.out.println(user);
        return user;
    }

    @CrossOrigin
    @PostMapping(value = "/api/deleteinfo")
    @ResponseBody //requestbody only for post
    public Result deleteinfo(HttpSession session) throws Exception
    {
        int userid= (int) session.getAttribute("userid");
        userService.deleteUser(userid);
        return ResultFactory.buildResult(200,"注销成功",null);
    }

    @CrossOrigin
    @PostMapping(value = "/api/personal")
    @ResponseBody //requestbody only for post
    public User personal(HttpSession session) throws Exception
    {
        int userid= (int) session.getAttribute("userid");
//        System.out.println(userid);
        User user=userDAO.getById(userid);
//        System.out.println(user);
        return user;
    }


    @CrossOrigin
    @PostMapping("/api/password")
    @ResponseBody
    public Result register(@RequestParam("password1") String password1,@RequestParam("password2") String password2,HttpSession session) {

        int userid= (int) session.getAttribute("userid");
        System.out.println(userid);
        System.out.println(password1);
        userService.setpassword(userid,password1);

        return ResultFactory.buildSuccessResult(null);
    }

    @CrossOrigin
    @PostMapping("/api/updateimg")
    @ResponseBody
    public Result updateimg(@RequestParam("username") String username,@RequestParam("cover") String cover){


        System.out.println(username);
        System.out.println(cover);

        userService.updateimg(username,cover);
        return ResultFactory.buildResult(200,"修改头像成功",null);
    }

}
