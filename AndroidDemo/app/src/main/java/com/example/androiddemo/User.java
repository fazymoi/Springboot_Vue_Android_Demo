package com.example.androiddemo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class User {
    int id;

    String username;
    String password;
    String nickname;

    String eaddress;

    String cover;

    public User(String name, String password) {
        this.username=name;
        this.password=password;
    }

    public User() {
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() { return nickname;}

    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getEaddress() { return eaddress;}

    public void setEaddress(String eaddress) { this.eaddress = eaddress; }


}


