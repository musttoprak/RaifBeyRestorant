package com.example.raifbeyrestaurant.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.services.ProductServices
import com.example.raifbeyrestaurant.services.PurchasingServices
import com.example.raifbeyrestaurant.services.UserServices
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.item_add.*
import kotlinx.android.synthetic.main.item_image.*
import kotlinx.android.synthetic.main.item_info.*
import java.net.Inet4Address

class ProfileActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        ivProfileToBack.setOnClickListener { finish() }
        purchased = PurchasingServices.getByUserIdPurchased(firebaseAuth.uid)
        cvLogaut.setOnClickListener {
            firebaseAuth.updateCurrentUser(firebaseAuth.currentUser!!)
            firebaseAuth.signOut()

            checkUser()
        }
        cvUpdate.setOnClickListener {
            if (etProfileName.text.trim() != null || etProfileAdress.text.trim() != null || etPhoneNumber.text.trim() != null) {
                UserServices.userUpdateProfile(
                    firebaseAuth.uid,
                    etProfileName.text.toString(),
                    etProfileAdress.text.toString(),
                    etPhoneNumber.text.toString(),
                    this
                )
            } else {
                Toast.makeText(this, "Lütfen boş yerleri doldurunuz", Toast.LENGTH_SHORT).show()

            }
        }
        cvPurchasing.setOnClickListener {

            startActivity(
                Intent(
                    this@ProfileActivity, PurchasedActivity::class.java
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        checkUser()
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, VerificationActivity::class.java))
            finish()
        } else {
            val name = firebaseUser.displayName
            val address=UserServices.getUserİnfo(firebaseUser.uid,"address")
            val phoneNumber=UserServices.getUserİnfo(firebaseUser.uid,"phoneNumber")
            println(address+"-----------"+phoneNumber)
            etProfileName.setText(name)
            etProfileAdress.setText(address)
            etPhoneNumber.setText(phoneNumber)
        }
    }
}