package com.example.vuetest.controller;

import com.example.vuetest.pojo.Food;
import com.example.vuetest.pojo.Store;
import com.example.vuetest.result.Result;
import com.example.vuetest.result.ResultFactory;
import com.example.vuetest.service.FoodService;
import com.example.vuetest.service.StoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
public class SearchfoodController {

    @Autowired
    FoodService foodService;
    @Autowired
    StoreService storeService;

    @CrossOrigin
    @PostMapping("/api/searchfoods")
    @ResponseBody
    public List<Food> searchlist(@RequestParam("searchfood") String searchfood){
        Food temp;
        List<Food> foods = foodService.search_foods(searchfood);
        for (int i = 0; i < foods.size(); i++)
        {
            temp= foods.get(i);
//            System.out.println(temp.getFoodname());
//            System.out.println(temp.getCover());
        }
        return foods;
    }

    @CrossOrigin
    @PostMapping("/api/setfoodid")
    @ResponseBody
    public Result foodpage(@RequestParam("foodid") int foodid,HttpSession session){
       System.out.println("********"+foodid);

        session.setAttribute("foodid", foodid);
        int foodid1= (int) session.getAttribute("foodid");
        System.out.println("22222!"+foodid1);
        return ResultFactory.buildSuccessResult(null);
    }

    @CrossOrigin
    @PostMapping("/api/setfoodid2")
    @ResponseBody
    public Result foodpage2(@RequestParam("foodid") String foodid,HttpSession session){
        System.out.println(session.getId());
        int id=Integer.parseInt(foodid);
        session.setAttribute("foodid", id);
        int foodid1= (int) session.getAttribute("foodid");
        System.out.println("22222!"+foodid1);
        return ResultFactory.buildSuccessResult(null);
    }

    @CrossOrigin
    @PostMapping("/api/allfoods")
    @ResponseBody
    public List<Food> alllist(@RequestParam("searchfood") String searchfood){
        System.out.println("allfoods");
        Food temp;
        List<Food> foods = foodService.allfoods();
        for (int i = 0; i < foods.size(); i++)
        {
            temp= foods.get(i);
//            System.out.println(temp.getFoodname());
//            System.out.println(temp.getCover());
        }
        return foods;
    }

    @CrossOrigin
    @PostMapping("/api/showfood")
    @ResponseBody
    public Food showfood(@RequestParam("storeid") String storeid, HttpSession session){
        System.out.println("!!!showfodd!!!");
        int foodid= (int) session.getAttribute("foodid");
//        System.out.println("00000!"+storeid1);
        Food food;
        food=foodService.findByFoodid(foodid);
        return food;
    }

    @CrossOrigin
    @PostMapping("/api/fromfoodtostore")
    @ResponseBody
    public void fromfoodtostore(@RequestParam("foodid") int foodid, HttpSession session){
        System.out.println("!!!food_to_store!!!");
        Food food=foodService.findByFoodid(foodid);
        int storeid=food.getStoreid();
      session.setAttribute("storeid", storeid);
    }
}
