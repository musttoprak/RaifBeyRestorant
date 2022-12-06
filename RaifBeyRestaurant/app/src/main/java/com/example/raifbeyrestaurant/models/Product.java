package com.example.raifbeyrestaurant.models;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.UUID;


public class Product {

    private String _id;
    private String _name;
    private String _storageName;
    private String _description;
    public String categoryId;
    private String _price;
    public  Integer piece=1;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_storageName() {
        return _storageName;
    }

    public void set_storageName(String _storageName) {
        this._storageName = _storageName;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_price() {
        return _price;
    }

    public void set_price(String _price) {
        this._price = _price;
    }


    public Product(String name,String description, String price, String categoryId) {
        UUID uuid=UUID.randomUUID();
        set_id(uuid.toString());
        set_name(name);
        set_description(description);
        set_price(price);
        this.categoryId = categoryId;
        storageNameCreate(name);
    }
    public static Product ProductById(String id, String name, String description, String price, String categoryId){
       Product product =new Product(name,description,price,categoryId);
       product.set_id(id);
       return product;
    }

    private void storageNameCreate(String name) {
        FirebaseStorage.getInstance().getReference().child("productImages/"+name+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

               set_storageName(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("storagename hatasıııı");
            }
        });


    }

    public static ArrayList<Product> createProductsList(int num) {
        ArrayList<Product> products = new ArrayList<Product>();

        for (int i = 1; i <= num; i++) {
            products.add(new Product(i + ". ürün", "123", "100",  "1"));
        }

        return products;
    }

}
