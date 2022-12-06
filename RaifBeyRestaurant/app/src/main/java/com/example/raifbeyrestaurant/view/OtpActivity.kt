package com.example.raifbeyrestaurant.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.raifbeyrestaurant.MainActivity
import com.example.raifbeyrestaurant.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    var verificationId: String? = null
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        val phoneNumber = intent.getStringExtra("phoneNumber")
        phoneLble.text = "Verify $phoneNumber"
        auth!!.firebaseAuthSettings.setAppVerificationDisabledForTesting(true);
        val options = phoneNumber?.let {
            PhoneAuthOptions.newBuilder(auth!!)
                .setPhoneNumber(it)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(object
                    : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {}

                    override fun onVerificationFailed(p0: FirebaseException) {}
                    override fun onCodeSent(
                        verifyId: String,
                        forceResendingToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verifyId, forceResendingToken)
                        verificationId = verifyId
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    }
                }).build()      // OnVerificationStateChangedCallback
        }
        PhoneAuthProvider.verifyPhoneNumber(options!!)

        code1.onFocusChangeListener
    }

    fun registerByPhone(view: View) {

        val code1 = code1.text.toString()
        val code2 = code2.text.toString()
        val code3 = code3.text.toString()
        val code4 = code4.text.toString()
        val code5 = code5.text.toString()
        val code6 = code6.text.toString()
        if (code1.trim().isEmpty()
            || code2.trim().isEmpty()
            || code3.trim().isEmpty()
            || code4.trim().isEmpty()
            || code5.trim().isEmpty()
            || code6.trim().isEmpty()
        ) {
            Toast.makeText(this, "Please Enter valid code", Toast.LENGTH_SHORT).show()
        }else{
            var otp = code1 + code2 + code3 + code4 + code5 + code6
            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
            auth!!.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        Toast.makeText(this, "Başarısız", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}

