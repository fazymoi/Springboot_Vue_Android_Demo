package com.example.vuetest.controller;

import com.example.vuetest.pojo.User;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import com.example.vuetest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class SmsController {

    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/code")
    @ResponseBody //requestbody only for post
    public Result code(@RequestParam("username") String username, @RequestParam("createPhoneCode") String createPhoneCode){
        username = HtmlUtils.htmlEscape(username);
        createPhoneCode = HtmlUtils.htmlEscape(createPhoneCode);

        System.out.println(username);
        System.out.println(createPhoneCode);

        HttpClientUtil client = HttpClientUtil.getInstance();

        String Uid = "美食推荐官";
        String Key = "12EDD661C2EBFB63FBBBE228C0045002";
        String smsMob = username;
        String smsText = "验证码："+createPhoneCode;

        //GBK发送
        int resultGbk = client.sendMsgGbk(Uid, Key, smsText, smsMob );
        if(resultGbk>0){
            System.out.println("GBK成功发送条数=="+resultGbk);
        }else{
            System.out.println(client.getErrorMsg(resultGbk));
        }

        return ResultFactory.buildSuccessResult(null);
    }


    @CrossOrigin
    @PostMapping(value = "/api/login2")
    @ResponseBody //requestbody only for post
    public Result login2(@RequestParam("username") String username,HttpSession session) {
        username = HtmlUtils.htmlEscape(username);
        System.out.println(username);

        String username1= (String) session.getAttribute("username");
//        System.out.println("1:"+username1);

        User user = userService.getByName(username);
        int userid =user.getId();
        session.setAttribute("userid", userid);
//            System.out.println("3:"+user);
        if (null == user) {
            return ResultFactory.buildFailResult("此账号还未注册");
        }
        else {
            return ResultFactory.buildSuccessResult(null);
        }
    }

}
