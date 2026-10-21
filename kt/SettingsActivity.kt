
package com.example.rentals

import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class SettingsActivity : BaseActivity() {

    private val settingsPrefs by lazy {
        getSharedPreferences("RentalAppSettings", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupBottomNavigation()

        val btnAccount = findViewById<TextView>(R.id.btnAccountSettings)
        val btnPrivacy = findViewById<TextView>(R.id.btnPrivacySecurity)
        val btnHelp = findViewById<TextView>(R.id.btnHelpSupport)
        val btnAbout = findViewById<TextView>(R.id.btnAboutApp)
        val btnLogout = findViewById<TextView>(R.id.btnLogout)

        val switchNotifications =
            findViewById<Switch>(R.id.switchNotifications)

        val switchRentReminders =
            findViewById<Switch>(R.id.switchRentReminders)

        // Restore saved settings
        switchNotifications.isChecked =
            settingsPrefs.getBoolean("notifications_enabled", true)

        switchRentReminders.isChecked =
            settingsPrefs.getBoolean("rent_reminders_enabled", true)

        // Save notification preference
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            settingsPrefs.edit()
                .putBoolean("notifications_enabled", isChecked)
                .apply()

            Toast.makeText(
                this,
                if (isChecked) "Notifications enabled"
                else "Notifications disabled",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Save rent reminder preference
        switchRentReminders.setOnCheckedChangeListener { _, isChecked ->
            settingsPrefs.edit()
                .putBoolean("rent_reminders_enabled", isChecked)
                .apply()

            Toast.makeText(
                this,
                if (isChecked) "Rent reminder preference enabled"
                else "Rent reminder preference disabled",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Open saved tenant profile
        btnAccount.setOnClickListener {
            startActivity(
                Intent(this, TenantProfileActivity::class.java)
            )
        }

        btnPrivacy.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Privacy & Security")
                .setMessage(
                    "Keep your account details private. " +
                            "Do not share your password or OTP with anyone."
                )
                .setPositiveButton("OK", null)
                .show()
        }

        btnHelp.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Help & Support")
                .setMessage(
                    "For rental, payment or complaint problems, " +
                            "please contact your property owner."
                )
                .setPositiveButton("OK", null)
                .show()
        }

        btnAbout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("About Rentals")
                .setMessage(
                    "Rentals App\nVersion 1.0\n\n" +
                            "Manage your rental details, payments, " +
                            "calendar and complaints in one place."
                )
                .setPositiveButton("OK", null)
                .show()
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Logout") { _, _ ->

                // Clear login status only if your app uses this key.
                // Rental details and payment data are not deleted.

                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            }
            .show()
    }
}
