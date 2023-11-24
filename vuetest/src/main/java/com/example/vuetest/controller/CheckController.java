package com.example.vuetest.controller;
import com.example.vuetest.pojo.Check;
import com.example.vuetest.pojo.Link;
import com.example.vuetest.pojo.Store;
import com.example.vuetest.pojo.User;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import com.example.vuetest.service.CheckService;
import com.example.vuetest.service.LinkService;
import com.example.vuetest.service.StoreService;
import com.example.vuetest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class CheckController {
    @Autowired
    StoreService storeService;
    @Autowired
    CheckService checkService;

    @CrossOrigin
    @PostMapping("/api/checkdata")
    @ResponseBody
    public List<Check> list(@RequestBody User user, HttpSession session){
        System.out.println("!!!check!!!");
        System.out.println(session.getId());
        List<Check> checklist=checkService.findall();
        return checklist;
    }

    @CrossOrigin
    @PostMapping("/api/getstore")
    @ResponseBody
    public Store getstore(@RequestParam("storeid")int storeid, HttpSession session){
        System.out.println("!!!getstore!!!");
        System.out.println(session.getId());
        Store store=storeService.findByStoreid(storeid);
        return store;
    }

    @CrossOrigin
    @PostMapping("/api/passstore")
    @ResponseBody
    public Result passstore(@RequestParam("storeid")int storeid, @RequestParam("checkid")int checkid,HttpSession session){
        System.out.println("!!!passstore!!!");
        System.out.println(storeid);
        Store store=storeService.findByStoreid(storeid);
        storeService.passStore(store);
        checkService.deleteByCheckid(checkid);
        return ResultFactory.buildResult(200,"审核成功",null);
    }
    @PostMapping("/api/unpassstore")
    @ResponseBody
    public Result unpassstore(@RequestParam("storeid")int storeid,
                              @RequestParam("checkid")int checkid,
                              @RequestParam("status")String status,
                              HttpSession session){
        System.out.println("!!!unpassstore!!!");
        System.out.println(storeid);
        Store store=storeService.findByStoreid(storeid);
        if(status=="")
            return ResultFactory.buildFailResult("必须填写理由");
        else {
            storeService.unpassStore(store, status);
            checkService.deleteByCheckid(checkid);
            return ResultFactory.buildResult(200, "审核成功", null);
        }
    }

}
