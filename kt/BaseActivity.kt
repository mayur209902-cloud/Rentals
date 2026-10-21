
package com.example.rentals

import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    protected fun setupBottomNavigation() {
        findViewById<View>(R.id.navHome)?.setOnClickListener {
            openPage(HomeActivity::class.java)
        }

        findViewById<View>(R.id.navPayments)?.setOnClickListener {
            openPage(PaymentsActivity::class.java)
        }

        findViewById<View>(R.id.navCalendar)?.setOnClickListener {
            openPage(CalendarReminderActivity::class.java)
        }

        findViewById<View>(R.id.navProfile)?.setOnClickListener {
            openPage(TenantProfileActivity::class.java)
        }
    }

    private fun openPage(destination: Class<*>) {
        if (this::class.java == destination) return

        startActivity(
            Intent(this, destination).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
        )
    }
}
