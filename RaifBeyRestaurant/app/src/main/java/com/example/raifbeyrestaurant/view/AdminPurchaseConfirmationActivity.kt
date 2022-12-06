package com.example.raifbeyrestaurant.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.adapter.AdminPurchaseAdapter
import com.example.raifbeyrestaurant.models.Purchase
import kotlinx.android.synthetic.main.activity_admin_products.*
import kotlinx.android.synthetic.main.activity_admin_purchase_confirmation.*

var purchaseProducts=ArrayList<Purchase>()
class AdminPurchaseConfirmationActivity : AppCompatActivity() {
    private var purchaseLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_purchase_confirmation)
        ivAdminPurchasedToBack.setOnClickListener { finish() }
        loadData()
    }

    private fun loadData() {
        purchaseLayoutManager = LinearLayoutManager(this)
        val recyclerViewProduct: RecyclerView? = findViewById<RecyclerView>(R.id.rvPurchaseConfirmation)
        val productAdapter = AdminPurchaseAdapter(purchaseProducts)
        recyclerViewProduct?.adapter = productAdapter
        recyclerViewProduct?.layoutManager = purchaseLayoutManager
    }
}