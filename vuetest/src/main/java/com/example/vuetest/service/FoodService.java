package com.example.vuetest.service;
import com.example.vuetest.dao.FoodDAO;
import com.example.vuetest.dao.UserDAO;
import com.example.vuetest.pojo.Link;
import com.example.vuetest.pojo.Store;
import com.example.vuetest.pojo.User;
import com.example.vuetest.pojo.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class FoodService {
    @Autowired
    FoodDAO foodDAO;

    public List<Food> link_foods(int storeid) {
        Sort sort = Sort.by(Sort.Direction.DESC, "score");
        List<Food> foods = foodDAO.findByStoreidIs(storeid, sort);
        return foods;
    }
    public List<Food> allfoods(){
        List<Food> foods = foodDAO.findfoods();
        return foods;
    }
    public List<Food> search_foods(String searchfood){
        Sort sort = Sort.by(Sort.Direction.ASC, "foodid");
        searchfood="%"+searchfood+"%";
        List<Food> foods = foodDAO.findByQuery(searchfood);
        return foods;
    }

    public boolean isFoodnameExist(int storeid,String foodname){
        Food food = foodDAO.findByStoreidAndFoodname(storeid,foodname);
        return null!=food;
    }

    public void AddFood(Food food) {
        foodDAO.save(food);
    }

    public void UpdataFood(int foodid,int nowsid,String foodname,float price1,String ingredient,String tag,String cover) {
        Food foodnew = foodDAO.findByFoodid(foodid);
        System.out.println(foodid);
        foodnew.setFoodname(foodname);
        foodnew.setStoreid(nowsid);
        foodnew.setPrice(price1);
        foodnew.setIngredient(ingredient);
        foodnew.setTag(tag);
        foodnew.setCover(cover);
        foodDAO.save(foodnew);
    }

    public void deleteByStoreidAndFoodid(int storeid,int foodid) {
        foodDAO.deleteByStoreidAndFoodid(storeid,foodid);
    }

    public int getsameFoodnameFoodid(int storeid,String foodname) {
        Food food = foodDAO.findByStoreidAndFoodname(storeid,foodname);
        return food.getFoodid();
    }

    public void updatescore(int foodid, int tempscore) {
        Food food=foodDAO.findByFoodid(foodid);
        food.setScore(tempscore);
        foodDAO.save(food);
    }

    public Food findByFoodid(int foodid) {
        return foodDAO.findByFoodid(foodid);
    }
}
