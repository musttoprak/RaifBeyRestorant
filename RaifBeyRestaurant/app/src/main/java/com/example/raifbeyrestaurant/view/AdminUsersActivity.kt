package com.example.raifbeyrestaurant.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.adapter.AdminProductAdapter
import com.example.raifbeyrestaurant.adapter.AdminUsersAdapter
import com.example.raifbeyrestaurant.models.User
import com.example.raifbeyrestaurant.products
import kotlinx.android.synthetic.main.activity_admin_products.*
import kotlinx.android.synthetic.main.activity_admin_users.*


var usersAdmin = ArrayList<User>()

class AdminUsersActivity : AppCompatActivity() {
    private var userLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users)
        ivAdminUsersToBack.setOnClickListener { finish() }
        loadData()
    }

    private fun loadData() {

        userLayoutManager = LinearLayoutManager(this)
        val recyclerViewProduct: RecyclerView? = findViewById<RecyclerView>(R.id.rcAdminUser)
        val userAdapter = AdminUsersAdapter(usersAdmin)
        recyclerViewProduct?.adapter = userAdapter
        recyclerViewProduct?.layoutManager = userLayoutManager

    }
}