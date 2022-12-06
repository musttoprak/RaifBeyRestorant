package com.example.raifbeyrestaurant.services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.raifbeyrestaurant.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductServices {

    private static CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("products");
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    public static ArrayList<Product> allProducts() {
        ArrayList<Product> products = new ArrayList<Product>() {
        };

        firebaseFirestore.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, String.valueOf(document.getData()));
                                String id = document.getId();
                                String name = document.getString("name");
                                String description = document.getString("description");
                                String price = document.getString("price");
                                String categoryId = document.getString("categoryId");
                                //System.out.println(id + name + imageUrl);
                                products.add(Product.ProductById(id, name, description, price, categoryId));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents---------mmmmmm------: ", task.getException());
                        }
                    }
                });
        return products;

    }

    public static ArrayList<Product> getByCategoryIdProducts(String categoryId) {
        ArrayList<Product> products = new ArrayList<>();
        firebaseFirestore
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                products.add(Product.ProductById(document.getId(), document.getString("name"), document.getString("description"), document.getString("price"), document.getString("categoryId")));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return products;

    }

    public static Product getByIdProduct(String productId) {
        final String[] id = new String[1];
        final String[] name = new String[1];
        final String[] description = new String[1];
        final String[] price = new String[1];
        final String[] categoryId = new String[1];
        firebaseFirestore.document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        id[0] = document.getId();
                        name[0] = document.getString("name");
                        description[0] = document.getString("description");
                        price[0] = document.getString("price");
                        categoryId[0] = document.getString("categoryId");
                        //System.out.println(id + name + imageUrl);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return new Product(name[0], description[0], price[0], categoryId[0]);
    }

    public static void updateProduct(Product product, Context context) {
        firebaseFirestore.document(product.get_id()).update(productToMap(product))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, product.get_name() + "Güncelleme işlemi başarıllı", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Güncelleme işlemi başarısız -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void addProduct(Product product) {

        firebaseFirestore.document(product.get_id()).set(productToMap(product));

    }

    private static Map<String, Object> productToMap(Product product) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", product.get_id());
        data.put("name", product.get_name());
        data.put("description", product.get_description());
        data.put("price", product.get_price());
        data.put("categoryId", product.categoryId);

        return data;

    }

    public static Uri getProductImage(String storageName) {

        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(storageName);
        final Uri[] Uri = new Uri[1];

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Uri[0] = uri;
                System.out.println("uriiiiiiiiiiiiiiii-----" + Uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("ürün image indirilirken hata oluştu" + exception);
            }
        });
        return Uri[0];
    }


    public static void deleteProduct(String id,String name, Context context) {
        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child("productImages/"+name+".jpg");
        firebaseFirestore.document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Toast.makeText(context, id + " Id'li product silindi", Toast.LENGTH_SHORT).show();
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, id + " Id'li product storage dosyası silindi", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(context, id + " Id'li product storage silme işlemi başarısız -> " + exception, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, id + " Id'li product silme işlemi başarısız -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
