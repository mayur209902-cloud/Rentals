
package com.example.rentals

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_pass)

        val contact = findViewById<EditText>(R.id.etForgotContact)

        findViewById<Button>(R.id.btnForgotSendOTP).setOnClickListener {
            val value = contact.text.toString().trim()

            if (value.isEmpty()) {
                contact.error = "Enter registered email or mobile number"
            } else {
                val intent = Intent(
                    this, LoginVerificationActivity::class.java
                )
                intent.putExtra("contact", value)
                startActivity(intent)

                Toast.makeText(
                    this,
                    "Connect OTP service to send real OTP",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        findViewById<TextView>(R.id.tvForgotBackToLogin).setOnClickListener {
            finish()
        }
    }
}