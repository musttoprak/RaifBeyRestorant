package com.example.raifbeyrestaurant.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.raifbeyrestaurant.*
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.services.ProductServices
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.coroutines.*

class ProductDetails : AppCompatActivity() {
    var favoritesActive: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        supportActionBar?.hide()

        Picasso.get().load(intent.getStringExtra("productStorageName")).into(ivDetailsImage)
        //Glide.with(this).load(intent.getStringExtra("productStorageName")).into(ivDetailsImage)
        ivProductDetailsToBack.setOnClickListener { finish() }
        tvDetailsName.text = intent.getStringExtra("productName")
        tvDetailsDescription.text = intent.getStringExtra("productDescription")
        val productPrice = intent.getStringExtra("productPrice")
        val productCategoryId: String = intent.getStringExtra("productCategoryId").toString()
        val productId: String = intent.getStringExtra("productId").toString()
        val product = Product.ProductById(
            productId,
            intent.getStringExtra("productName"),
            intent.getStringExtra("productDescription"),
            productPrice,
            productCategoryId
        )
        if (kurDurum) {
            tvDetailsPrice.text = productPrice.toString() + " ₺"
        } else {
            var price = (productPrice!!.toInt() / kurDegeri)
            tvDetailsPrice.text = "$ " + String.format("%.2f", price)
        }

        if (favorites.contains(productId)) {
            ivAddFavorites.setImageResource(R.drawable.ic_heart_red)
            favoritesActive = true
        } else {
            ivAddFavorites.setImageResource(R.drawable.ic_heart)
            favoritesActive = false
        }

        if (cartProducts.contains(product)){
            println(cartProducts.size)
            btnAddCart.text="Sepetten Çıkar"
        }else{
            println(cartProducts.size)
            btnAddCart.text="Sepete Ekle"
        }
        ivAddFavorites.setOnClickListener {
            if (favoritesActive == true) {
                favorites.remove(productId)
                favoritesProducts.remove(
                    Product.ProductById(
                        productId,
                        intent.getStringExtra("productName"),
                        intent.getStringExtra("productDescription"),
                        intent.getStringExtra("productPrice"),
                        productCategoryId
                    )
                )
                ivAddFavorites.setImageResource(R.drawable.ic_heart)
                favoritesActive = false
                println(favorites.size)
            } else {
                favorites.add(productId)
                favoritesProducts.add(
                    Product.ProductById(
                        productId,
                        intent.getStringExtra("productName"),
                        intent.getStringExtra("productDescription"),
                        intent.getStringExtra("productPrice"),
                        productCategoryId
                    )
                )
                ivAddFavorites.setImageResource(R.drawable.ic_heart_red)
                favoritesActive = true
                println(favorites.size)
            }
        }

        btnAddCart.setOnClickListener {
            if (cartProducts.contains(product)) {
                cartProducts.remove(product)
                println(cartProducts.size)
                cartPrice -= (product.get_price().toInt())
                btnAddCart.text="Sepete Ekle"
            } else {
                cartProducts.add(product)
                println(cartProducts.size)
                cartPrice += (product.get_price().toInt())
                btnAddCart.text="Sepetten Çıkar"
            }
        }
    }


}