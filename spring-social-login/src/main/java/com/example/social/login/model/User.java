package com.example.social.login.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @Column(insertable = true)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String imgUrl;

    public User() {
    }

    public User(String id, String email, String name, String imgUrl) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
