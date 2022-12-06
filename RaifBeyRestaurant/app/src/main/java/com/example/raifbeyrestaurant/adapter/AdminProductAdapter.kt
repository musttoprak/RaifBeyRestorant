package com.example.raifbeyrestaurant.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.raifbeyrestaurant.*
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.services.CategoryServices
import com.example.raifbeyrestaurant.view.ProductDetails
import kotlinx.android.synthetic.main.products_design.*


class AdminProductAdapter(
    var adminProducts: ArrayList<Product>,
    val context: Context,
    private val clickListener: ClickListener
) :
    RecyclerView.Adapter<AdminProductAdapter.ModelViewHolder>() {
    interface ClickListener {
        fun onClick(product: Product)
    }

    class ModelViewHolder(view: View, clickListener: ClickListener) :
        RecyclerView.ViewHolder(view) {

        val productName: TextView = view.findViewById(R.id.productName)
        val prouctPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val productImage: ImageView = view.findViewById(R.id.ivProductImage)
        val productDescription: TextView = view.findViewById(R.id.productDescription)
        val productCategoryName: TextView = view.findViewById(R.id.productCategoryName)
        val productDetailsBtn: Button = view.findViewById(R.id.btnDetail)
        val clickListener: ClickListener = clickListener
        fun bindItems(item: Product) {
            productName.text = item.get_name()
            productDescription.text = item.get_description()
            prouctPrice.text = item.get_price()
            productCategoryName.text=CategoryServices.findYourCategoryNameById(item.get_id())
            productDetailsBtn.setOnClickListener { clickListener.onClick(item) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.products_design_admin, parent, false)
        return ModelViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return adminProducts.size
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        Glide.with(context).load(adminProducts.get(position).get_storageName()).into(holder.productImage)
        holder.bindItems(adminProducts.get(position))
    }
}





