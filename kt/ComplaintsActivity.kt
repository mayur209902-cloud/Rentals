
package com.example.rentals

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class ComplaintsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaints)

        // Bottom Navigation
        setupBottomNavigation()

        // New Complaint
        findViewById<TextView>(R.id.btnNewComplaint)
            .setOnClickListener {
                startActivity(
                    Intent(this, NewComplaintActivity::class.java)
                )
            }

        // My Complaints
        findViewById<TextView>(R.id.btnMyComplaints)
            .setOnClickListener {
                startActivity(
                    Intent(this, MyComplaintsActivity::class.java)
                )
            }

        // Pending Complaints
        findViewById<TextView>(R.id.btnPendingComplaints)
            .setOnClickListener {
                showMessage("Pending Complaints")
            }

        // Resolved Complaints
        findViewById<TextView>(R.id.btnResolvedComplaints)
            .setOnClickListener {
                showMessage("Resolved Complaints")
            }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
