package com.screenplay.myscreenplay.Model;

public class Item_screen {
    private int id;
    private String title,image,created_at;

    public Item_screen(String title, String image, String created_at) {
        this.title = title;
        this.image = image;
        this.created_at = created_at;
    }

    public Item_screen() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
