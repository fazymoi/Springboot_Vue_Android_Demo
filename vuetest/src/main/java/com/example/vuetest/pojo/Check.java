package com.example.vuetest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "checktable")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Check {
    public Check(int userid, int storeid, String localTime) {
        this.storeid=storeid;
        this.userid=userid;
        this.submittime=localTime;
    }

    public Check() {

    }

    public int getCheckid() {
        return checkid;
    }

    public void setCheckid(int checkid) {
        this.checkid = checkid;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkid")
    int checkid;

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }
    @Column(name = "storeid")
    int storeid;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    @Column(name = "userid")
    int userid;

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }

    @Column(name = "submittime")
    String submittime;
}
