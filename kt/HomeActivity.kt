
package com.example.rentals

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()

        // Properties
        findViewById<Button>(R.id.btnProperties).setOnClickListener {
            Toast.makeText(this, "Properties page coming soon", Toast.LENGTH_SHORT).show()
        }

        // Complaints
        findViewById<Button>(R.id.btnComplains).setOnClickListener {
            startActivity(Intent(this, ComplaintsActivity::class.java))
        }

        // Tenant details
        findViewById<Button>(R.id.btnTenants).setOnClickListener {
            startActivity(Intent(this, TenantDetailsActivity::class.java))
        }

        // Payment history
        findViewById<Button>(R.id.btnPayments).setOnClickListener {
            startActivity(Intent(this, PaymentsActivity::class.java))
        }

        // Reports
        findViewById<Button>(R.id.btnReports).setOnClickListener {
            Toast.makeText(this, "Reports page coming soon", Toast.LENGTH_SHORT).show()
        }

        // Profile
        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, TenantProfileActivity::class.java))
        }

        // Notifications
        findViewById<ImageButton>(R.id.btnNotifications).setOnClickListener {
            Toast.makeText(this, "Notifications page coming soon", Toast.LENGTH_SHORT).show()
        }


        findViewById<android.view.View>(R.id.btnNotifications)
            .setOnClickListener {
                startActivity(
                    Intent(this, NotificationsActivity::class.java)
                )
            }



        val btnSettings = findViewById<android.view.View>(R.id.btnSettings)

        btnSettings.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }


        // Pay rent
        findViewById<Button>(R.id.btnPayRent).setOnClickListener {
            startActivity(Intent(this, PaymentsActivity::class.java))
        }

        // Logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Bottom navigation
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            // Already on Home
        }

        findViewById<LinearLayout>(R.id.navPayments).setOnClickListener {
            startActivity(Intent(this, PaymentsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navCalendar).setOnClickListener {
            startActivity(Intent(this, CalendarReminderActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, TenantProfileActivity::class.java))
        }
    }
}
