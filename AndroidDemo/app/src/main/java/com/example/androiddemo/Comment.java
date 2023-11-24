package com.example.androiddemo;

import java.io.Serializable;

public class Comment implements Serializable {
    int commentid;

    int userid;
    int foodid;

    int score;

    String submittime;

    String content;


    String foodname;

    String storename;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    String nickname;

    public Comment(int userid, int foodid, String submittime, String content, int score, String foodname, String storename, String nickname) {
        this.userid=userid;
        this.content=content;
        this.foodid=foodid;
        this.submittime=submittime;
        this.score=score;
        this.foodname=foodname;
        this.storename=storename;
        this.nickname=nickname;
    }

    public Comment() {

    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
