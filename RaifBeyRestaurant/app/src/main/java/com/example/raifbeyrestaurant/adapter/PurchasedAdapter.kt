package com.example.raifbeyrestaurant.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.models.Purchase
import com.example.raifbeyrestaurant.services.PurchasingServices
import com.google.firebase.database.collection.LLRBNode

class PurchasedAdapter(var purchases:ArrayList<Purchase>) :
    RecyclerView.Adapter<PurchasedAdapter.ModelViewHolder>() {
    class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productsName: TextView = view.findViewById(R.id.tvPurchasedProductsName)
        val productsPrice: TextView = view.findViewById(R.id.tvPurchasedPrice)
        val confirmation: TextView = view.findViewById(R.id.tvPurchasedConfirmation)
        val cvPurchased: CardView = view.findViewById(R.id.cvPurchased)
        fun bindItems(item: Purchase) {
            productsName.setText(item.getProducts())
            productsPrice.setText(item.getProductsPrice())
            if (item.confirmation){
                confirmation.setText("Siparişiniz Onaylandı")
                confirmation.setBackgroundResource(R.color.green)
                cvPurchased.setCardBackgroundColor(Color.parseColor("#ADD6B8"))
            }else{
                confirmation.setText("Şiparişiniz Onaylanmadı")
                confirmation.setBackgroundResource(R.color.red)
                cvPurchased.setCardBackgroundColor(Color.parseColor("#FD5C5C"))
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasedAdapter.ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.purchased_design, parent, false)
        return PurchasedAdapter.ModelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return purchases.size
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bindItems(purchases.get(position))
    }
}