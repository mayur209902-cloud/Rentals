
package com.example.rentals

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginVerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_verification)

        val contact =
            findViewById<EditText>(R.id.etVerificationContact)

        val otp =
            findViewById<EditText>(R.id.etVerificationOTP)

        contact.setText(intent.getStringExtra("contact") ?: "")

        findViewById<Button>(R.id.btnVerifyCode).setOnClickListener {
            when {
                contact.text.toString().trim().isEmpty() ->
                    contact.error = "Enter email or mobile number"

                otp.text.toString().trim().length != 6 ->
                    otp.error = "Enter 6-digit OTP"

                else -> Toast.makeText(
                    this,
                    "Connect backend to verify OTP",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        findViewById<TextView>(R.id.tvResendOTP).setOnClickListener {
            Toast.makeText(
                this,
                "Connect OTP service to resend OTP",
                Toast.LENGTH_LONG
            ).show()
        }

        findViewById<TextView>(R.id.tvBackToLogin).setOnClickListener {
            finish()
        }
    }
}