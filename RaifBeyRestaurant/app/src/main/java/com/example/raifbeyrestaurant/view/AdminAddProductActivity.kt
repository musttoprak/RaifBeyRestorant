package com.example.raifbeyrestaurant.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.categorys
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.services.ProductServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_admin_add_product.*
import java.io.IOException
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class AdminAddProductActivity : AppCompatActivity() {
    var selectedCategory = -1
    var selectedPicture: Uri? = null
    var selectedBitmap: Bitmap? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_product)
        addProductLauncher()
        var categorysName = ArrayList<String>()

        for (category in categorys!!) {
            categorysName.add(category.get_name())
        }
        if (spnCategorys != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, categorysName
            )
            spnCategorys.adapter = adapter

            spnCategorys.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selectedCategory = spnCategorys.selectedItemPosition + 1
                    Toast.makeText(
                        this@AdminAddProductActivity,
                        "spn position" + spnCategorys.selectedItem, Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        btnAddProduct.setOnClickListener {
            if (!etDescription.text.isNullOrEmpty() || !etName.text.isNullOrEmpty() || !etPrice.text.isNullOrEmpty() || selectedCategory != 1) {
                addProduct(
                    etName.text.toString(),
                    etDescription.text.toString(),
                    etPrice.text.toString(),
                    selectedCategory.toString()
                )
            }
        }
    }

    fun addToPhoto(view: View) {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(view, "Galeriye girme izni gerekli", Snackbar.LENGTH_INDEFINITE)
                    .setAction("İzin ver",
                        View.OnClickListener {
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)

        }

    }

    fun addProductLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(
                                this@AdminAddProductActivity.contentResolver,
                                selectedPicture!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            ivPhoto.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                this@AdminAddProductActivity.contentResolver,
                                selectedPicture
                            )
                            ivPhoto.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                //permission granted
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(this@AdminAddProductActivity, "Permisson needed!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    fun addProduct(name: String, description: String, price: String, categoryId: String) {

        val imageName = "$name.jpg"
        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference
        val imagesReference = reference.child("productImages/$imageName")

        if (selectedPicture != null) {
            imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->
                ProductServices.addProduct(Product(name, description, price, categoryId))
            }
        }
    }
}
