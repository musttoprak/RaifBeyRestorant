package com.example.raifbeyrestaurant.view

import android.R.attr.src
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.categorys
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.services.ProductServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_admin_product_details.*


class AdminProductDetails : AppCompatActivity() {
    var selectedCategory = -1
    var selectedPicture: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_product_details)
        val storageName = intent.getStringExtra("productStorageName")
        val id = intent.getStringExtra("productId")
        println(id)
        val name = intent.getStringExtra("productName")
        val description = intent.getStringExtra("productDescription")
        val price = intent.getStringExtra("productPrice")
        val categoryId = intent.getStringExtra("productCategoryId")
        etDetailName.setText(name)
        etDetailDescription.setText(description.toString())
        etDetailPrice.setText(price.toString())
        var categorysName = ArrayList<String>()

        for (category in categorys!!) {
            categorysName.add(category.get_name())
        }

        if (spnDetailCategorys != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, categorysName
            )
            spnDetailCategorys.adapter = adapter

            spnDetailCategorys.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selectedCategory = spnDetailCategorys.selectedItemPosition + 1
                    Toast.makeText(
                        this@AdminProductDetails,
                        "spn position" + spnDetailCategorys.selectedItem, Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        btnUpdate.setOnClickListener {
            if (!etDetailDescription.text.isNullOrEmpty() || !etDetailName.text.isNullOrEmpty() || !etDetailPrice.text.isNullOrEmpty() || selectedCategory != 1) {
                updateProduct(
                    name!!,
                    etDetailDescription.text.toString(),
                    etDetailPrice.text.toString(),
                    selectedCategory.toString(),
                    id!!
                )
            }
        }
        btnRemove.setOnClickListener {
            ProductServices.deleteProduct(id,name, this)
            finish()
        }
    }

    private fun updateProduct(
        name: String,
        description: String,
        price: String,
        categoryId: String,
        id: String,

    ) {
        ProductServices.updateProduct(
            Product.ProductById(
                id,
                name,
                description,
                price,
                categoryId
            ), this
        )
        finish()

    }


}
