package com.example.androiddemo;

public class Collection {


    int Collectionid;
    int userid;
    int foodid;
    String submittime;
    String foodname;
    String storename;


    public Collection(int userid, int foodid, String submittime, String foodname, String storename) {
        this.userid=userid;
        this.foodid=foodid;
        this.submittime=submittime;
        this.foodname=foodname;
        this.storename=storename;
    }

    public Collection() {

    }

    public int getCollectionid() {
        return Collectionid;
    }

    public void setCollectionid(int Collectionid) {
        this.Collectionid = Collectionid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getFoodid() {
        return foodid;
    }

    public void setFoodid(int foodid) {
        this.foodid = foodid;
    }


    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }


    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }


}
