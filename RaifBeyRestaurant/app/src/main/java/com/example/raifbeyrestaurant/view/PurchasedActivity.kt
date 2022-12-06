package com.example.raifbeyrestaurant.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.adapter.AdminPurchaseAdapter
import com.example.raifbeyrestaurant.adapter.PurchasedAdapter
import com.example.raifbeyrestaurant.models.Purchase
import kotlinx.android.synthetic.main.activity_purchased.*

var purchased=ArrayList<Purchase>()
class PurchasedActivity : AppCompatActivity() {
    private var purchaseLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchased)
        ivPurhasedToBack.setOnClickListener { finish() }
        loadData()
    }

    private fun loadData() {
        purchaseLayoutManager = LinearLayoutManager(this)
        val recyclerViewProduct: RecyclerView? =
            findViewById<RecyclerView>(R.id.rvPurchased)
        val productAdapter = PurchasedAdapter(purchased)
        recyclerViewProduct?.adapter = productAdapter
        recyclerViewProduct?.layoutManager = purchaseLayoutManager

    }
}