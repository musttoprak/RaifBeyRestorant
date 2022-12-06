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

class AdminPurchaseAdapter(var purchases:ArrayList<Purchase>) :
    RecyclerView.Adapter<AdminPurchaseAdapter.ModelViewHolder>(){
    class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val displayName: TextView = view.findViewById(R.id.tvPurchaseDisplayName)
        val productsName: TextView = view.findViewById(R.id.tvPurchaseProductsName)
        val productsPrice: TextView = view.findViewById(R.id.tvPurchasePrice)
        val confirmation: Button = view.findViewById(R.id.btnConfirmation)
        val cardView:CardView=view.findViewById(R.id.cvAdminPurchasedConfirmation)
        fun bindItems(item: Purchase) {
            displayName.setText(item.getDisplayName())
            productsName.setText(item.getProducts())
            productsPrice.setText(item.getProductsPrice())
            if (item.confirmation){
                confirmation.setText("Onaylandı")
                confirmation.setBackgroundResource(R.color.red)
                cardView.setCardBackgroundColor(Color.parseColor("#F47070"))
            }else{
                confirmation.setText("Onayla")
                confirmation.setBackgroundResource(R.color.green)
                cardView.setCardBackgroundColor(Color.parseColor("#ADD6B8"))
            }

            confirmation.setOnClickListener {
                if (item.confirmation==false){
                    item.confirmation=true
                    confirmation.setText("Onaylandı")
                    confirmation.setBackgroundResource(R.color.red)
                    cardView.setCardBackgroundColor(Color.parseColor("#FD5C5C"))
                    PurchasingServices.confirmPurchase(item.getId())
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.purchase_confirmation_design, parent, false)
        return ModelViewHolder(view)
    }


    override fun onBindViewHolder(holder: AdminPurchaseAdapter.ModelViewHolder, position: Int) {
        holder.bindItems(purchases.get(position))
    }

    override fun getItemCount(): Int {
        return purchases.size
    }
}