package com.example.raifbeyrestaurant.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.raifbeyrestaurant.MainActivity
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.services.PurchasingServices
import com.example.raifbeyrestaurant.services.UserServices
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        purchaseProducts=PurchasingServices.allPurchase()
        usersAdmin=UserServices.allUsers()
        addProducts.setOnClickListener {
            startActivity(Intent(this, AdminAddProductActivity::class.java))
        }
        products.setOnClickListener {
            startActivity(Intent(this, AdminProductsActivity::class.java))
        }
        purchaseConfirmation.setOnClickListener {
            startActivity(Intent(this, AdminPurchaseConfirmationActivity::class.java))
        }
        users.setOnClickListener {
            startActivity(Intent(this, AdminUsersActivity::class.java))
        }
        goToApp.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}