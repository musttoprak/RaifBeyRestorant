package com.example.raifbeyrestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.models.Purchase
import com.example.raifbeyrestaurant.models.User
import com.example.raifbeyrestaurant.services.PurchasingServices

class AdminUsersAdapter(val users: ArrayList<User>) :
    RecyclerView.Adapter<AdminUsersAdapter.ModelViewHolder>() {
    class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val displayName: TextView = view.findViewById(R.id.tvDisplayName)
        val email: TextView = view.findViewById(R.id.tvEmail)
        val uId: TextView = view.findViewById(R.id.tvUId)
        fun bindItems(item: User) {
            displayName.setText(item.getDisplayName())
            email.setText(item.getEmail())
            uId.setText(item.getUid())
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminUsersAdapter.ModelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_design_admin, parent, false)
        return AdminUsersAdapter.ModelViewHolder(view)
    }


    override fun onBindViewHolder(holder: AdminUsersAdapter.ModelViewHolder, position: Int) {
        holder.bindItems(users.get(position))
    }

    override fun getItemCount(): Int {
        return users.size
    }


}