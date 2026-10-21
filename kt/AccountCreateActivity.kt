
package com.example.rentals

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AccountCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_create)

        val name = findViewById<EditText>(R.id.etFullName)
        val contact = findViewById<EditText>(R.id.etRegisterContact)
        val ownerId = findViewById<EditText>(R.id.etRegisterOwnerId)
        val password = findViewById<EditText>(R.id.etRegisterPassword)
        val confirm = findViewById<EditText>(R.id.etConfirmPassword)

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            when {
                name.text.toString().trim().isEmpty() ->
                    name.error = "Enter full name"

                contact.text.toString().trim().isEmpty() ->
                    contact.error = "Enter email or mobile number"

                ownerId.text.toString().trim().isEmpty() ->
                    ownerId.error = "Create Owner ID"

                password.text.toString().length < 8 ->
                    password.error = "Password must be at least 8 characters"

                password.text.toString() != confirm.text.toString() ->
                    confirm.error = "Passwords do not match"

                else -> Toast.makeText(
                    this,
                    "Connect backend to register account",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        findViewById<TextView>(R.id.tvRegisterBackToLogin).setOnClickListener {
            finish()
        }
    }
}