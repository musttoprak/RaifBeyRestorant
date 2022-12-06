package com.example.raifbeyrestaurant.services;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.raifbeyrestaurant.models.Purchase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PurchasingServices {
    private static final CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("buyingProcesses");

    public static ArrayList<Purchase> allPurchase() {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>() {
        };

        firebaseFirestore.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String uid = document.getString("uid");
                            String displayName = document.getString("displayName");
                            String products = document.getString("productsName");
                            String productsPrice = document.getString("productsPrice");
                            Boolean confirmation = document.getBoolean("confirmation");
                            purchases.add(new Purchase(id, uid, displayName, products, productsPrice, confirmation));
                        }
                    } else {
                        Log.d(TAG, "allPurchase: HATA");
                    }
                });
        return purchases;

    }

    public static ArrayList<Purchase> getByUserIdPurchased(String uid){
        ArrayList<Purchase> purchases = new ArrayList<Purchase>() {
        };

        firebaseFirestore
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String id = document.getString("id");
                                String displayName = document.getString("displayName");
                                String productsName = document.getString("productsName");
                                String productsPrice = document.getString("productsPrice");
                                Boolean confirmation = document.getBoolean("confirmation");
                                purchases.add(new Purchase(id,uid,displayName,productsName,productsPrice,confirmation));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return purchases;
    }

    public static void buyProducts(String uid, String displayName, String productsName, String productsPrice, Boolean confirmation) {
        UUID id = UUID.randomUUID();
        Log.d(TAG, "buyProducts: satın alınan ürünün idsi" + id);
        Map<String, Object> data = new HashMap<>();
        data.put("id", id.toString());
        data.put("uid", uid);
        data.put("displayName", displayName);
        data.put("productsName", productsName);
        data.put("productsPrice", productsPrice);
        data.put("confirmation", confirmation);

        firebaseFirestore.document(id.toString()).set(data);
    }

    //uid değil ürünün idsi
    public static void confirmPurchase(String id) {
        Map<String, Object> data = new HashMap<>();
        data.put("confirmation", true);

        firebaseFirestore.document(id).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Satın alma işlemini onayladınız");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess: Satın alma işleminde hata oluştu");
                    }
                });
    }
}
