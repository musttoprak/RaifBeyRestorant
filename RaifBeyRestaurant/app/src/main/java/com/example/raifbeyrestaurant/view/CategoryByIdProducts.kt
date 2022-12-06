package com.example.raifbeyrestaurant.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.adapter.ProductAdapter
import com.example.raifbeyrestaurant.cartProducts
import com.example.raifbeyrestaurant.favoritesProducts
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.products
import com.example.raifbeyrestaurant.services.ProductServices
import kotlinx.android.synthetic.main.activity_category_by_id_products.*

class CategoryByIdProducts : AppCompatActivity(), ProductAdapter.ClickListener {
    var productsByCategoryId = ArrayList<Product>()
    private var productLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_by_id_products)
        supportActionBar?.hide()
        ivCategoryByIdProductsToBack.setOnClickListener { finish() }
        val categoryId = intent.getStringExtra("categoryId")
        tvCategoryName.text = intent.getStringExtra("categoryName") + " Çeşitleri"
        // productsByCategoryId!!.addAll(ProductServices.getByCategoryIdProducts(categoryId))
        loadData(categoryId!!)
    }

    private fun loadData(categoryId: String) {
        for (product in products) {
            if (product.categoryId == categoryId) {
                productsByCategoryId.add(product)
            }
        }
        productLayoutManager = GridLayoutManager(this, 2)
        val recyclerViewProduct: RecyclerView? = findViewById(R.id.gvByCategoryId)
        val productAdapter = ProductAdapter(productsByCategoryId, this, this)
        recyclerViewProduct?.adapter = productAdapter
        recyclerViewProduct?.layoutManager = productLayoutManager

        println(productsByCategoryId.size)


    }

    override fun onClick(product: Product) {
        val intent = Intent(this, ProductDetails::class.java)
        intent.putExtra("productId", product.get_id())
        intent.putExtra("productName", product.get_name())
        intent.putExtra("productDescription", product.get_description())
        intent.putExtra("productPrice", product.get_price())
        intent.putExtra("productCategoryId", product.categoryId)
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