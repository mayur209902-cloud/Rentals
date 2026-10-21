
package com.example.rentals

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONArray
import androidx.appcompat.app.AppCompatActivity

class MyComplaintsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_complaints)

        loadComplaints()
    }

    override fun onResume() {
        super.onResume()

        // Refresh list when returning to this page
        if (::complaintsList.isInitialized) {
            loadComplaints()
        }
    }

    private lateinit var complaintsList: LinearLayout
    private lateinit var emptyState: LinearLayout

    private fun loadComplaints() {
        complaintsList =
            findViewById(R.id.complaintsList)

        emptyState =
            findViewById(R.id.emptyState)

        complaintsList.removeAllViews()

        val preferences = getSharedPreferences(
            "RentalsComplaints",
            MODE_PRIVATE
        )

        val savedJson = preferences.getString(
            "complaints_list",
            "[]"
        )

        val complaints = try {
            JSONArray(savedJson)
        } catch (_: Exception) {
            JSONArray()
        }

        if (complaints.length() == 0) {
            emptyState.visibility = View.VISIBLE
            return
        }

        emptyState.visibility = View.GONE

        // Show newest complaint first
        for (i in complaints.length() - 1 downTo 0) {
            val item = complaints.optJSONObject(i) ?: continue

            addComplaintCard(
                title = item.optString("title", "Complaint"),
                category = item.optString("category", "Other"),
                date = item.optString("date", ""),
                property = item.optString("property", ""),
                room = item.optString("room", ""),
                description = item.optString("description", ""),
                priority = item.optString("priority", "Medium"),
                status = item.optString("status", "Pending")
            )
        }

        if (complaintsList.childCount == 0) {
            emptyState.visibility = View.VISIBLE
        }
    }

    private fun addComplaintCard(
        title: String,
        category: String,
        date: String,
        property: String,
        room: String,
        description: String,
        priority: String,
        status: String
    ) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 16, 18, 16)

            background = GradientDrawable().apply {
                setColor(Color.rgb(25, 25, 25))
                cornerRadius = 18f
                setStroke(1, Color.rgb(50, 50, 50))
            }
        }

        fun addText(
            text: String,
            size: Float,
            color: Int,
            bold: Boolean = false
        ) {
            card.addView(TextView(this).apply {
                this.text = text
                textSize = size
                setTextColor(color)

                if (bold) {
                    setTypeface(null, Typeface.BOLD)
                }

                setPadding(0, 4, 0, 6)
            })
        }

        addText(title, 19f, Color.WHITE, true)
        addText("Category: $category", 14f, Color.LTGRAY)
        addText("Property: $property", 14f, Color.LTGRAY)
        addText("Room / Flat: $room", 14f, Color.LTGRAY)
        addText("Date: $date", 14f, Color.LTGRAY)
        addText("Priority: $priority", 14f, Color.LTGRAY)
        addText("Description: $description", 14f, Color.LTGRAY)

        val statusColor = when (status.lowercase()) {
            "resolved" -> Color.rgb(80, 210, 120)
            "in progress" -> Color.rgb(255, 190, 60)
            else -> Color.rgb(255, 100, 100)
        }

        addText("Status: $status", 14f, statusColor, true)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 14
        }

        complaintsList.addView(card, params)
    }
}
