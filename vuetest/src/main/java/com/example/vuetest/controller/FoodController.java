package com.example.vuetest.controller;

import com.example.vuetest.pojo.Food;
import com.example.vuetest.pojo.Link;
import com.example.vuetest.pojo.Store;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import com.example.vuetest.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

@Controller
public class FoodController {

    @Autowired
    FoodService foodService;

    @CrossOrigin
    @PostMapping("/api/storeid")
    @ResponseBody
    public Result storeid(@RequestParam("storeid") int storeid,HttpSession session){
//        System.out.println("********"+storeid);
        session.setAttribute("storeid", storeid);

        int storeid1= (int) session.getAttribute("storeid");
//        System.out.println("*******"+storeid1);

        return ResultFactory.buildSuccessResult(null);
    }

    @CrossOrigin
    @PostMapping("/api/foods")
    @ResponseBody
    public List<Food> list(HttpSession session){
        int storeid1= (int) session.getAttribute("storeid");
//        System.out.println("*******"+storeid1);

        Food temp;
        List<Food> foods = foodService.link_foods(storeid1);
        for (int i = 0; i < foods.size(); i++)
        {
            temp= foods.get(i);
//            System.out.println(temp.getFoodname());
        }
        return foods;
    }

    @CrossOrigin
    @PostMapping("/api/addfood")
    @ResponseBody
    public Result addfood(@RequestParam("foodname") String foodname,@RequestParam("price") String price,@RequestParam("ingredient") String ingredient,@RequestParam("tag") String tag,@RequestParam("cover") String cover,HttpSession session){
//        System.out.println(foodname);
        int storeid1= (int) session.getAttribute("storeid");
        if(foodname=="" || price=="" || ingredient =="" || tag=="" || cover=="")
            return ResultFactory.buildFailResult("信息不能为空");

        if  (foodService.isFoodnameExist(storeid1,foodname)==true)
            return ResultFactory.buildFailResult("该商家已有该美食");
        else {
            float price1=Float.parseFloat(price);
            Food food = new Food(storeid1);
            food.setFoodname(foodname);
            food.setPrice(price1);
            food.setIngredient(ingredient);
            food.setTag(tag);
            food.setCover(cover);
            foodService.AddFood(food);
            return ResultFactory.buildResult(200,"添加成功",null);
        }
    }

    @CrossOrigin
    @PostMapping("/api/deletefood")
    @ResponseBody
    public Result deletefood(@RequestParam("foodid") int foodid,HttpSession session){
//        System.out.println(foodid);

        int storeid1= (int) session.getAttribute("storeid");
        foodService.deleteByStoreidAndFoodid(storeid1,foodid);
        return ResultFactory.buildResult(200,"删除成功",null);
    }

    @CrossOrigin
    @PostMapping("/api/updatefood")
    @ResponseBody
    public Result updatefood(@RequestParam("foodid") int foodid,@RequestParam("foodname") String foodname,@RequestParam("price") String price,@RequestParam("ingredient") String ingredient,@RequestParam("tag") String tag,@RequestParam("cover") String cover,HttpSession session){
        int storeid1= (int) session.getAttribute("storeid");
        if(foodname=="" || price=="" || ingredient =="" || tag=="" || cover=="")
            return ResultFactory.buildFailResult("信息不能为空");
        if  (foodService.isFoodnameExist(storeid1,foodname)==true &&
                foodid != foodService.getsameFoodnameFoodid(storeid1,foodname))
        {
            return ResultFactory.buildFailResult("该商家已有同名美食");
        }
        else {
            float price1=Float.parseFloat(price);
            foodService.UpdataFood(foodid,storeid1,foodname,price1,ingredient,tag,cover);
            return ResultFactory.buildResult(200,"修改成功",null);
        }
    }


}
