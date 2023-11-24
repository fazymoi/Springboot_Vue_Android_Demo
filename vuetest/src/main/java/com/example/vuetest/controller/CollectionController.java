package com.example.vuetest.controller;


import com.example.vuetest.pojo.*;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import com.example.vuetest.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Controller

public class CollectionController {
    @Autowired
    CollectionService collectionService;
    @Autowired
    FoodService foodService;
    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;

    @CrossOrigin
    @PostMapping("/api/mycollection")
    @ResponseBody
    public List<Collection> clist(@RequestBody User user, HttpSession session) {
        System.out.println("!!!mycollection!!!");
        System.out.println(session.getId());
        List<Collection> clist = collectionService.findByUserid((Integer) session.getAttribute("userid"));
        return clist;
    }

    @CrossOrigin
    @PostMapping("/api/iscollectionexist")
    @ResponseBody
    public Collection iscollectionexist(HttpSession session) {
        System.out.println("!!!mycollection!!!");
        System.out.println(session.getId());
        int userid = (Integer) session.getAttribute("userid");
        int foodid = (Integer) session.getAttribute("foodid");
        return collectionService.isExistUseridAndFoodid(userid, foodid);
    }

    @CrossOrigin
    @PostMapping("/api/iscollectionexist1")
    @ResponseBody
    public boolean iscollectionexist1(HttpSession session) {
        System.out.println("!!!mycollection!!!");
        System.out.println(session.getId());
        int userid = (Integer) session.getAttribute("userid");
        int foodid = (Integer) session.getAttribute("foodid");
        return (null!=collectionService.isExistUseridAndFoodid(userid, foodid));
    }

    @CrossOrigin
    @PostMapping("/api/changecollection")
    @ResponseBody
    public Result changecollection(HttpSession session) {
        System.out.println("!!!changecollection!!!");
        System.out.println(session.getId());
        int userid = (Integer) session.getAttribute("userid");
        int foodid = (Integer) session.getAttribute("foodid");
        if (collectionService.isExistUseridAndFoodid(userid, foodid) != null) {
            int collectionid = collectionService.isExistUseridAndFoodid(userid, foodid).getCollectionid();
            collectionService.deleteByCollectionid(collectionid);
            return ResultFactory.buildResult(200, "取消收藏", null);
        } else {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            String submittime = df.format(time);
            String foodname = (foodService.findByFoodid(foodid)).getFoodname();
            String storename = storeService.findByStoreid(
                    foodService.findByFoodid(foodid).getStoreid()).getStorename();
            Collection collection = new Collection(userid, foodid, submittime, foodname, storename);
            collectionService.addCollection(collection);
            return ResultFactory.buildResult(200, "收藏成功", null);
        }
    }

}

//
//    @CrossOrigin
//    @PostMapping("/api/addcollection")
//    @ResponseBody
//    public Result addcollection(
//            HttpSession session){
//        System.out.println("!!!addcollection!!!");
//        System.out.println(session.getId());
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime time = LocalDateTime.now();
//        String submittime= df.format(time);
//        int foodid= (int) session.getAttribute("foodid");
//        int userid= (int) session.getAttribute("userid");
//        String foodname=(foodService.findByFoodid(foodid)).getFoodname();
//        String storename=storeService.findByStoreid(
//                foodService.findByFoodid(foodid).getStoreid()).getStorename();
//        Collection collection = new Collection(userid,foodid,submittime,foodname,storename);
//        collectionService.addCollection(collection);
//        return ResultFactory.buildResult(200,"添加成功",null);
//
//    }
//
//    @CrossOrigin
//    @PostMapping("/api/addcollection2")
//    @ResponseBody
//    public Result addcollection2(
//            HttpSession session){
//        System.out.println("!!!addcollection!!!");
//        System.out.println(session.getId());
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime time = LocalDateTime.now();
//        String submittime= df.format(time);
//        int foodid= (int) session.getAttribute("foodid");
//        int userid= (int) session.getAttribute("userid");
//        String foodname=(foodService.findByFoodid(foodid)).getFoodname();
//        String storename=storeService.findByStoreid(
//                foodService.findByFoodid(foodid).getStoreid()).getStorename();
//        Collection collection = new Collection(userid,foodid,submittime,foodname,storename);
//        collectionService.addCollection(collection);
//        return ResultFactory.buildResult(200,"添加成功",null);
//    }
//}

