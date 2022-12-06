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
import com.example.raifbeyrestaurant.view.ProductDetails
import kotlinx.android.synthetic.main.products_design.*


class ProductAdapter(
    var products: ArrayList<Product>,
    val context: Context,
    private val clickListener: ClickListener
) :
    RecyclerView.Adapter<ProductAdapter.ModelViewHolder>() {
    interface ClickListener {
        fun onClick(product: Product)
        fun addCart(product: Product)
        fun addFavorites(product: Product)
    }

    class ModelViewHolder(view: View, clickListener: ClickListener) :
        RecyclerView.ViewHolder(view) {

        val productName: TextView = view.findViewById(R.id.productName)
        val prouctPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val productPiece: TextView = view.findViewById(R.id.tvPiece)
        val productImage: ImageView = view.findViewById(R.id.ivProductImage)
        val productFavorites: ImageView = view.findViewById(R.id.ivProductFavorites)
        val productIncrease: TextView = view.findViewById(R.id.tvIncrease)
        val productDecrease: TextView = view.findViewById(R.id.tvDecrease)
        val productLinearLayout: RelativeLayout = view.findViewById(R.id.rlProduct)
        val productAddCart: ImageView = view.findViewById(R.id.ivProductAddCart)
        val productDescription: TextView = view.findViewById(R.id.productDescription)
        val clickListener: ClickListener = clickListener
        fun bindItems(item: Product) {
            productPiece.text = item.piece.toString()
            productName.text = item.get_name()
            if (kurDurum) {
                prouctPrice.text = (item.get_price().toInt() * item.piece).toString() + " ₺"
                productPiece.text = item.piece.toString()
            } else {
                var price = (Integer.parseInt(item.get_price()) / kurDegeri)* item.piece
                prouctPrice.text =
                    "$ " + String.format("%.2f", price)
                productPiece.text = item.piece.toString()
            }

            productDescription.text = item.get_description()
            if (cartProducts.contains(item)) {
                productAddCart.setImageResource(R.drawable.remove)
            } else {
                productAddCart.setImageResource(R.drawable.add)
            }
            productAddCart.setOnClickListener {

                if (cartProducts.contains(item)) {
                    productAddCart.setImageResource(R.drawable.add)
                    cartProducts.remove(item)
                    println(cartProducts.size)
                    cartPrice -= (item.get_price().toInt() * item.piece)

                    if (kurDurum) {
                        prouctPrice.text = (item.get_price().toInt() * item.piece).toString() + " ₺"
                        productPiece.text = item.piece.toString()
                    } else {
                        var price = (Integer.parseInt(item.get_price()) / kurDegeri)* item.piece
                        prouctPrice.text =
                            "$ " + String.format("%.2f", price)
                        productPiece.text = item.piece.toString()
                    }

                } else {
                    productAddCart.setImageResource(R.drawable.remove)
                    cartProducts.add(item)
                    println(cartProducts.size)
                    cartPrice += (item.get_price().toInt() * item.piece)
                    if (kurDurum) {
                        var deger =item.get_price().toInt() * item.piece
                        prouctPrice.text = deger.toString() + " ₺"
                        productPiece.text = item.piece.toString()
                    } else {

                        var price = (Integer.parseInt(item.get_price()) / kurDegeri)* item.piece
                        prouctPrice.text =
                            "$ " + String.format("%.2f", price)
                        productPiece.text = item.piece.toString()
                    }

                }
            }


            productLinearLayout.setOnClickListener {
                clickListener.onClick(item)

            }

            if (favoritesProducts.contains(item)) {
                productFavorites.setImageResource(R.drawable.ic_heart_red)
            } else {
                productFavorites.setImageResource(R.drawable.ic_heart)
            }
            productFavorites.setOnClickListener {
                if (favoritesProducts.contains(item)) {
                    favoritesProducts.remove(item)
                    favorites.remove(item.get_id())
                    productFavorites.setImageResource(R.drawable.ic_heart)
                } else {
                    favoritesProducts.add(item)
                    favorites.add(item.get_id())
                    productFavorites.setImageResource(R.drawable.ic_heart_red)
                }
            }

            productIncrease.setOnClickListener {
                ++item.piece


                if (kurDurum) {
                    prouctPrice.text = (item.get_price().toInt() * item.piece).toString() + " ₺"
                    productPiece.text = item.piece.toString()
                } else {
                    var price = (Integer.parseInt(item.get_price()) / kurDegeri)* item.piece
                    prouctPrice.text =
                        "$ " + String.format("%.2f", price)
                    productPiece.text = item.piece.toString()
                }

            }

            productDecrease.setOnClickListener {

                if (item.piece > 1) {
                    --item.piece
                    if (kurDurum) {
                        prouctPrice.text = (item.get_price().toInt() * item.piece).toString() + " ₺"
                        productPiece.text = item.piece.toString()
                    } else {
                        var price = (Integer.parseInt(item.get_price()) / kurDegeri)* item.piece
                        prouctPrice.text =
                            "$ " + String.format("%.2f", price)
                        productPiece.text = item.piece.toString()
                    }
                }
            }


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.products_design, parent, false)
        return ModelViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        Glide.with(context).load(products.get(position).get_storageName()).into(holder.productImage)
        holder.bindItems(products.get(position))
    }
}





