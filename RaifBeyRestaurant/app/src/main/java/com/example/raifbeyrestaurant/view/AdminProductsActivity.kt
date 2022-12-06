package com.example.raifbeyrestaurant.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.adapter.AdminProductAdapter
import com.example.raifbeyrestaurant.adapter.ProductAdapter
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.products
import kotlinx.android.synthetic.main.activity_admin_products.*

class AdminProductsActivity : AppCompatActivity(), AdminProductAdapter.ClickListener {
    private var productLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products)
        ivAdminProductsToBack.setOnClickListener { finish() }
        loadData()
    }

    private fun loadData() {
        productLayoutManager = GridLayoutManager(this, 2)
        val recyclerViewProduct: RecyclerView? = findViewById<RecyclerView>(R.id.gvAdminProducts)
        val productAdapter = AdminProductAdapter(products!!, this, this)
        recyclerViewProduct?.adapter = productAdapter
        recyclerViewProduct?.layoutManager = productLayoutManager
    }


    override fun onClick(product: Product) {
        var intent = Intent(this, AdminProductDetails::class.java)
        intent.putExtra("productId", product.get_id())
        intent.putExtra("productName", product.get_name())
        intent.putExtra("productDescription", product.get_description())
        intent.putExtra("productPrice", product.get_price())
        intent.putExtra("productStorageName", product.get_storageName())
        startActivity(intent)
    }
}