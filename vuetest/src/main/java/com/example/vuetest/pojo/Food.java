package com.example.vuetest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "food")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "foodid")
    int foodid;

    @Column(name = "foodname")
    String foodname;

    @Column(name = "storeid")
    int storeid;

    @Column(name = "price")
    float price;

    @Column(name = "ingredient")
    String ingredient;

    @Column(name = "tag")
    String tag;



    @Column(name = "cover")
    String cover;

    @Column(name = "score")
    int score;

    public int getScore() {return score;}

    public void setScore(int score) {
        this.score = score;
    }



    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public Food(int storeid) {
        this.storeid=storeid;

    }

    public Food() {

    }


    public int getFoodid() {return foodid;}

    public void setFoodid(int foodid) {
        this.foodid = foodid;
    }


    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }


    public int getStoreid() {return storeid;}

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }


    public float getPrice() {return price;}

    public void setPrice(float price) {
        this.price = price;
    }


    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
