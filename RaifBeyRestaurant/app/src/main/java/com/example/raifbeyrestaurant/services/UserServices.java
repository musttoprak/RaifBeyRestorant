package com.example.raifbeyrestaurant.services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.raifbeyrestaurant.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserServices {

    private static final CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("users");

    public static ArrayList<User> allUsers() {
        ArrayList<User> users = new ArrayList<User>() {
        };

        firebaseFirestore.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String uid = document.getId();
                            String displayName = document.getString("displayName");
                            String email = document.getString("email");
                            users.add(new User(uid, displayName, email));
                        }
                    } else {
                        Log.d(TAG, "Error getting documents---------------: ", task.getException());
                    }
                });
        return users;

    }

    public static User getByUIdUser(String uid) {
        final String[] displayName = new String[1];
        final String[] email = new String[1];
        firebaseFirestore.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        displayName[0] = document.getString("displayName");
                        email[0] = document.getString("email");
                        //String address = document.getString("address");
                        //String phoneNumber = document.getString("phoneNumber");

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());

                }
            }
        });
        return new User(uid,displayName[0],email[0]);
    }

    public static void addUsers(User user) {

        firebaseFirestore.document(user.getUid()).set(userToMap(user));

    }

    private static Map<String, Object> userToMap(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", user.getUid());
        data.put("displayName", user.getDisplayName());
        data.put("email", user.getEmail());
        return data;

    }

    public static void userUpdateProfile(String uid, String displayName, String address, String phoneNumber, Context context) {
        Map<String, Object> data = new HashMap<>();
        data.put("displayName", displayName);
        data.put("address", address);
        data.put("phoneNumber", phoneNumber);
        firebaseFirestore.document(uid).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, displayName + "Güncelleme işlemi başarıllı", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Güncelleme işlemi başarısız -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public  static String getUserİnfo(String uid,String infoName){
        final String[] info = new String[1];
        firebaseFirestore.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        info[0] = document.getString(infoName);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return info[0];
    }
}
