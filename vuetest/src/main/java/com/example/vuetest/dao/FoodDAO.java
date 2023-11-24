package com.example.vuetest.dao;

import com.example.vuetest.pojo.Food;
import com.example.vuetest.pojo.Link;
import com.example.vuetest.pojo.Store;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodDAO  extends JpaRepository<Food,Integer> {
    Sort sort = null;
    List<Food> findByStoreidIs(int storeid, Sort sort);

    @Query("From Food order by score desc ")
    List<Food> findfoods();
    @Query("From Food where foodname like :searchfood or tag like :searchfood order by score desc ")
    List<Food> findByQuery(@Param("searchfood") String searchfood);

    Food findByStoreidAndFoodname(int storeid,String foodname);

    Food findByFoodid(int foodid);


    @Modifying
    @Transactional
    void deleteByStoreidAndFoodid(int storeid,int foodid);
}
