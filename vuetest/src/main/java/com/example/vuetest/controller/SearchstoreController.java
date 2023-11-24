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
public class SearchstoreController {

    @Autowired
    StoreService storeService;

    @Autowired
    FoodService foodService;

    @CrossOrigin
    @PostMapping("/api/searchstores")
    @ResponseBody
    public List<Store> searchlist(@RequestParam("searchstore") String searchstore){
        Store temp;
        List<Store> stores = storeService.search_stores(searchstore);
        for (int i = 0; i < stores.size(); i++)
        {
            temp= stores.get(i);
//            System.out.println(temp.getFoodname());
//            System.out.println(temp.getCover());
        }
        return stores;
    }

    @CrossOrigin
    @PostMapping("/api/setstoreid")
    @ResponseBody
    public Result storepage(@RequestParam("storeid") int storeid, HttpSession session){
//        System.out.println("********"+storeid);

        session.setAttribute("storeid", storeid);
        int storeid1= (int) session.getAttribute("storeid");
//        System.out.println("00000!"+storeid1);
        return ResultFactory.buildSuccessResult(null);
    }

    @CrossOrigin
    @PostMapping("/api/setstoreid2")
    @ResponseBody
    public Result storepage2(@RequestParam("storeid") String storeid, HttpSession session){
//        System.out.println("********"+storeid);
        int id=Integer.parseInt(storeid);
        session.setAttribute("storeid", id);
        int storeid1= (int) session.getAttribute("storeid");
//        System.out.println("00000!"+storeid1);
        return ResultFactory.buildSuccessResult(null);
    }



    @CrossOrigin
    @PostMapping("/api/showstore")
    @ResponseBody
    public Store showstore(@RequestParam("storeid") String storeid, HttpSession session){
        int storeid1= (int) session.getAttribute("storeid");
//        System.out.println("00000!"+storeid1);
        Store store;
        store=storeService.findByStoreid(storeid1);
        return store;
    }
    @CrossOrigin
    @PostMapping("/api/allstores")
    @ResponseBody
    public List<Store> alllist(@RequestParam("searchstore") String searchstore){
        Store temp;
        List<Store> stores = storeService.allstores();
        for (int i = 0; i < stores.size(); i++)
        {
            temp= stores.get(i);
//            System.out.println(temp.getFoodname());
//            System.out.println(temp.getCover());
        }
        return stores;
    }

    @CrossOrigin
    @PostMapping("/api/store_foods")
    @ResponseBody
    public List<Food> store_foods(@RequestParam("storeid") String storeid, HttpSession session){
        int storeid1= (int) session.getAttribute("storeid");
        System.out.println("00000!"+storeid1);

        Food temp;
        List<Food> foods = foodService.link_foods(storeid1);
        for (int i = 0; i < foods.size(); i++)
        {
            temp= foods.get(i);
//            System.out.println(temp.getFoodname());
        }
        return foods;
    }

}
