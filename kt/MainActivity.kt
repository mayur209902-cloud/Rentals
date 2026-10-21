
package com.example.rentals

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = findViewById<EditText>(R.id.etEmailMobile)
        val ownerId = findViewById<EditText>(R.id.etOwnerId)
        val password = findViewById<EditText>(R.id.etPassword)

        val showPassword =
            findViewById<CheckBox>(R.id.cbShowPassword)

        val login = findViewById<Button>(R.id.btnLogin)
        val verifyOTP = findViewById<Button>(R.id.btnVerifyOTP)

        val forgot = findViewById<TextView>(R.id.tvForgotPassword)
        val register = findViewById<TextView>(R.id.tvCreateAccount)

        // Show / Hide Password
        showPassword.setOnCheckedChangeListener { _, checked ->
            password.inputType = if (checked) {
                InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            password.setSelection(password.text.length)
        }

        // Login and open Home Page
        login.setOnClickListener {
            when {
                email.text.toString().trim().isEmpty() -> {
                    email.error = "Enter email or mobile number"
                }

                ownerId.text.toString().trim().isEmpty() -> {
                    ownerId.error = "Enter Owner ID"
                }

                password.text.toString().isEmpty() -> {
                    password.error = "Enter password"
                }

                else -> {
                    val intent = Intent(
                        this,
                        HomeActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                }
            }
        }

        // Forgot Password
        forgot.setOnClickListener {
            startActivity(
                Intent(this, ForgotPassActivity::class.java)
            )
        }

        // Verify OTP page
        verifyOTP.setOnClickListener {
            val contact = email.text.toString().trim()

            if (contact.isEmpty()) {
                email.error = "Enter email or mobile number"
            } else {
                val intent = Intent(
                    this,
                    LoginVerificationActivity::class.java
                )
                intent.putExtra("contact", contact)
                startActivity(intent)
            }
        }

        // Create Account
        register.setOnClickListener {
            startActivity(
                Intent(this, AccountCreateActivity::class.java)
            )
        }
    }
}