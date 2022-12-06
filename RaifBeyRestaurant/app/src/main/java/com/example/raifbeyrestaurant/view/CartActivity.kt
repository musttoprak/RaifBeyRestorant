package com.example.raifbeyrestaurant.view


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.*
import com.example.raifbeyrestaurant.*
import com.example.raifbeyrestaurant.adapter.ProductAdapter
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.services.PurchasingServices
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cart.*


class CartActivity : AppCompatActivity(), ProductAdapter.ClickListener {
    private var productLayoutManager: RecyclerView.LayoutManager? = null
    var purchasesProductsName=""
    val auth=FirebaseAuth.getInstance()
    val uid =auth.currentUser!!.uid
    val displayName=auth.currentUser!!.displayName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.hide()
        ivCartToBack.setOnClickListener { finish() }

        loadData()

        tvBuy.setOnClickListener {
            if(cartProducts.size != 0) {
                for (product in cartProducts) {
                    var adet= " X${product.piece}"
                    purchasesProductsName+="\n•"+product.get_name()+adet
                }
                openPurchaseConfirmation()

            }else {
                Toast.makeText(
                    applicationContext,
                    "Lütfen sepete ürün ekleyiniz", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openPurchaseConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SATIN ALMA İŞLEMİNİ ONAYLIYOR MUSUNUZ")
        builder.setMessage(purchasesProductsName)

        builder.setPositiveButton("Onayla") { dialog, which ->
            PurchasingServices.buyProducts(uid,displayName,purchasesProductsName, cartPrice.toString(),false)
            Toast.makeText(
                applicationContext,
                "Satın alma işleminiz onaylandı", Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(
                applicationContext,
               "İptal edildi", Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNeutralButton("Sonra") { dialog, which ->
            Toast.makeText(
                applicationContext,
                "Maybe", Toast.LENGTH_SHORT
            ).show()
        }
        builder.show()
    }

    private fun loadData() {
        productLayoutManager = GridLayoutManager(this, 2)
        val recyclerViewProduct: RecyclerView? = findViewById(R.id.gvCart)
        val productAdapter = ProductAdapter(cartProducts, this, this)
        recyclerViewProduct?.adapter = productAdapter
        recyclerViewProduct?.layoutManager = productLayoutManager
        if (kurDurum) {
            tvCartPrice.text = cartPrice.toString() + " ₺"
        } else {
            var price = (cartPrice / kurDegeri)
            tvCartPrice.text = "$ " + String.format("%.2f", price)
        }

    }

    override fun onClick(product: Product) {
        val intent = Intent(this, ProductDetails::class.java)
        intent.putExtra("productId", product.get_id())
        intent.putExtra("productName", product.get_name())
        intent.putExtra("productDescription", product.get_description())
        intent.putExtra("productPrice", product.get_price())
        intent.putExtra("productCategoryId", product.categoryId)
        intent.putExtra("productStorageName", product.get_storageName())
        startActivity(intent)
    }

    override fun addCart(product: Product) {
        println(cartProducts.size)
    }

    override fun addFavorites(product: Product) {
        println(favoritesProducts.size)
    }

}


