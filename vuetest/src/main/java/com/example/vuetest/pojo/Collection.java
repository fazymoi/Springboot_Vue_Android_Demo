package com.example.vuetest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "collection")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collectionid")
    int collectionid;
    @Column(name = "userid")
    int userid;
    @Column(name = "foodid")
    int foodid;

    @Column(name = "submittime")
    String submittime;

    @Column(name = "foodname")
    String foodname;
    @Column(name = "storename")
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
        return collectionid;
    }

    public void setCollectionid(int collectionid) {
        this.collectionid = collectionid;
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
