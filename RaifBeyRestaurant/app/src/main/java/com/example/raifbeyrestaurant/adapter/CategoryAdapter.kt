package com.example.raifbeyrestaurant.adapter

import android.nfc.tech.MifareClassic.get
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.categorys
import com.example.raifbeyrestaurant.models.Category
import com.example.raifbeyrestaurant.models.Product
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class CategoryAdapter(var categorys: ArrayList<Category>, private val clickListener: CategoryAdapter.ClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ModelViewHolder>() {
    interface ClickListener {
        fun onClick(category: Category)
    }
    class ModelViewHolder(view: View,clickListener: CategoryAdapter.ClickListener) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.categoryName)
        val categoryImageView: ImageView = view.findViewById(R.id.ivCategoryImage)
        val clickListener: CategoryAdapter.ClickListener =clickListener
        val category: RelativeLayout=view.findViewById(R.id.rlCategory)
        fun bindItems(item: Category) {
            category.setOnClickListener{
                clickListener.onClick(item)
            }
            if (item.image==null){
                Picasso.get().load(item.get_imageUrl()).into(categoryImageView)
            }else{
                categoryImageView.setImageBitmap(item.image)
            }

            categoryName.setText(item._name)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categorys, parent, false)
        return ModelViewHolder(view,clickListener)
    }

    override fun getItemCount(): Int {
        return categorys.size
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bindItems(categorys.get(position))
    }
}

