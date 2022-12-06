package com.example.raifbeyrestaurant.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.*
import com.example.raifbeyrestaurant.adapter.ProductAdapter
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.services.ProductServices
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity(), ProductAdapter.ClickListener {
    private var productLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        supportActionBar?.hide()
        ivFavoritesToBack.setOnClickListener{finish()}
        loadData()

    }

    private fun loadData() {
        productLayoutManager = GridLayoutManager(this, 2)
        val recyclerViewProduct: RecyclerView? = findViewById(R.id.gvFavorites)
        val productAdapter = ProductAdapter(favoritesProducts, this, this)
        recyclerViewProduct?.adapter = productAdapter
        recyclerViewProduct?.layoutManager = productLayoutManager
    }

    override fun onClick(product: Product) {
        val intent = Intent(this, ProductDetails::class.java)
        intent.putExtra("productId", product.get_id())
        intent.putExtra("productName", product.get_name())
        intent.putExtra("productDescription", product.get_description())
        intent.putExtra("productPrice", product.get_price())
        intent.putExtra("productStorageName", product.get_storageName())
        startActivity(intent)
    }

    override fun addCart(product: Product) {
        println(cartProducts.size)
    }

    override fun addFavorites(product: Product) {
        println(favoritesProducts.size)
    }
}