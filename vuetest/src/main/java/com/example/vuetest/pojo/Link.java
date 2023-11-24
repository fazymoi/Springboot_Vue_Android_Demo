package com.example.vuetest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


@Entity
@Table(name = "link")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Link {
    public int getLinkid() {
        return linkid;
    }

    public void setLinkid(int linkid) {
        this.linkid = linkid;
    }

    @Id
    @Column(name = "linkid")
    int linkid;

    @Column(name = "userid")
    int userid;

    @Column(name = "storeid")
    int storeid;

    public Link(int userid, int storeid) {
        this.userid=userid;
        this.storeid=storeid;

    }

    public Link() {

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }




}
