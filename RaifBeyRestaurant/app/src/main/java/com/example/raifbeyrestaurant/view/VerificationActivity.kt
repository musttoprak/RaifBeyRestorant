package com.example.raifbeyrestaurant.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.raifbeyrestaurant.MainActivity
import com.example.raifbeyrestaurant.R
import com.example.raifbeyrestaurant.models.User
import com.example.raifbeyrestaurant.services.UserServices
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_verification.*


class VerificationActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    val adminUIUtil = "LIIRJdu3MkZl51zV02SZY2GQygf1"

    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)


        var googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        googleSignInClient.signOut()
        auth = FirebaseAuth.getInstance()

        //checkUser()
        googleSıgnInBtn.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

         supportActionBar?.hide()
         editNumber.requestFocus()
         continueBtn.setOnClickListener {
             val intent = Intent(this, OtpActivity::class.java)
             intent.putExtra("phoneNumber", editNumber.text.toString())
             startActivity(intent)
         }
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            var uid = firebaseUser.uid
            if (uid != adminUIUtil) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential).addOnSuccessListener { authResult ->
            val firebaseUser = auth.currentUser
            val uid = firebaseUser!!.uid
            val displayName = firebaseUser.displayName
            val email = firebaseUser!!.email
            Log.d(TAG, "firebaseAuthWithGoogleAccount: uid= ${uid}")
            Log.d(TAG, "firebaseAuthWithGoogleAccount: email= ${email}")
            if (authResult.additionalUserInfo!!.isNewUser) {
                UserServices.addUsers(User(uid, displayName, email))
                Log.d(TAG, "firebaseAuthWithGoogleAccount: account create...\n$email")
                Toast.makeText(this, "account created...\n$email", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "firebaseAuthWithGoogleAccount: existing... \n$email")
                Toast.makeText(this, "loggedın...\n$email", Toast.LENGTH_SHORT).show()
            }

            if (uid != adminUIUtil) {

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }
        }
            .addOnFailureListener { e ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: login failed ${e.message}")
                Toast.makeText(this, "login failed ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}