package com.example.androiddemo;

public class Food {
    int foodid;

    String foodname;
    int storeid;

    float price;

    String ingredient;


    String tag;

    String cover;
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
