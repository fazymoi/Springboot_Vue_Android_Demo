package com.example.vuetest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


@Entity
@Table(name = "store")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storeid")
    int storeid;

    @Column(name = "storename")
    String storename;
    @Column(name = "storecode")
    String storecode;
    @Column(name = "storeaddr")
    String storeaddr;
    @Column(name = "openinghours")
    String openinghours;
    @Column(name = "contact")
    String contact;
    @Column(name = "status")
    String status;
    @Column(name = "introduction")
    String introduction;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Column(name = "cover")
    String cover;

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }
//    @Override
//    public String toString() {
//        return "user(name:" + username + ",password:" + password + ")";
//    }


    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }

    public String getStorecode() {
        return storecode;
    }

    public void setStorecode(String storecode) {
        this.storecode = storecode;
    }

    public String getStoreaddr() {
        return storeaddr;
    }

    public void setStoreaddr(String storeaddr) {
        this.storeaddr = storeaddr;
    }

    public String getOpeninghours() {
        return openinghours;
    }

    public void setOpeninghours(String openinghours) {
        this.openinghours = openinghours;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}

