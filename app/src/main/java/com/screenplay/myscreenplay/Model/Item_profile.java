package com.screenplay.myscreenplay.Model;

public class Item_profile {

    private int id;
    private int user_id;
    private String name,country,image,name_name,visibl;
     private String name_user;
    public Item_profile() {
    }

    public String getVisibl() {
        return visibl;
    }

    public void setVisibl(String visibl) {
        this.visibl = visibl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_name() {
        return name_name;
    }

    public void setName_name(String name_name) {
        this.name_name = name_name;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
