package com.example.raifbeyrestaurant.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Category {

    private String id;

    private String name;

    private String imageUrl;

    public Bitmap image=null;


    public String get_id() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_imageUrl() {
        return imageUrl;
    }

    public void set_imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category(String id, String name, String imageUrl) {
        set_id(id);
        set_name(name);
        set_imageUrl(imageUrl);
    }




    public static ArrayList<Category> createCategorysList(int num) {
        ArrayList<Category> categorys = new ArrayList<Category>();

        for (int i = 1; i <= num; i++) {
            categorys.add(new Category("1", i + ". kategori", "123"));
        }

        return categorys;
    }
}
