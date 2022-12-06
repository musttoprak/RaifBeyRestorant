package com.example.raifbeyrestaurant.services;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.raifbeyrestaurant.models.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CategoryServices {

    private static final CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("categorys");

    public static ArrayList<Category> allCategorys() {
        ArrayList<Category> categorys = new ArrayList<Category>() {
        };

        firebaseFirestore.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String imageUrl = document.getString("imageUrl");
                            //System.out.println(id + name + imageUrl);
                            categorys.add(new Category(id, name, imageUrl));
                        }
                    } else {
                        Log.d(TAG, "Error getting documents---------mmmmmm------: ", task.getException());
                    }
                });
        return categorys;

    }


    public static String findYourCategoryNameById(String categoryId) {
        final String[] categoryName = {"Default"};

        firebaseFirestore.document(categoryId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        categoryName[0] =document.getString("name");
                        Log.d(TAG, categoryName[0]);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
      /*
         firebaseFirestore.document(categoryId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    categoryName[0] =document.getString("name");

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });*/

        return categoryName[0];
    }
}
