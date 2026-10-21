
package com.example.rentals

import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.json.JSONArray
import org.json.JSONObject

class NotificationsActivity : BaseActivity() {

    private lateinit var container: LinearLayout
    private lateinit var tvUnreadCount: TextView
    private lateinit var btnAll: Button
    private lateinit var btnUnread: Button
    private lateinit var btnMarkAllRead: TextView
    private lateinit var btnClearAll: TextView

    private val prefs by lazy {
        getSharedPreferences("RentalNotifications", MODE_PRIVATE)
    }

    private data class AppNotification(
        val id: Long,
        val title: String,
        val message: String,
        val time: String,
        val type: String,
        var isRead: Boolean
    )

    private val notifications = mutableListOf<AppNotification>()
    private var showUnreadOnly = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        container = findViewById(R.id.notificationContainer)
        tvUnreadCount = findViewById(R.id.tvUnreadCount)
        btnAll = findViewById(R.id.btnAllNotifications)
        btnUnread = findViewById(R.id.btnUnreadNotifications)
        btnMarkAllRead = findViewById(R.id.btnMarkAllRead)
        btnClearAll = findViewById(R.id.btnClearAll)

        setupBottomNavigation()

        loadNotifications()

        btnAll.setOnClickListener {
            showUnreadOnly = false
            renderNotifications()
        }

        btnUnread.setOnClickListener {
            showUnreadOnly = true
            renderNotifications()
        }

        btnMarkAllRead.setOnClickListener {
            notifications.forEach { it.isRead = true }
            saveNotifications()
            renderNotifications()
            Toast.makeText(
                this, "All notifications marked as read",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnClearAll.setOnClickListener {
            if (notifications.isEmpty()) {
                Toast.makeText(
                    this, "No notifications to clear",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Clear notifications?")
                    .setMessage("All notification history will be deleted.")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Clear All") { _, _ ->
                        notifications.clear()
                        saveNotifications()
                        renderNotifications()
                    }
                    .show()
            }
        }
    }

    private fun loadNotifications() {
        notifications.clear()

        val saved = prefs.getString("items", null)

        if (saved != null) {
            try {
                val array = JSONArray(saved)

                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)

                    notifications.add(
                        AppNotification(
                            id = obj.optLong("id"),
                            title = obj.optString("title"),
                            message = obj.optString("message"),
                            time = obj.optString("time"),
                            type = obj.optString("type"),
                            isRead = obj.optBoolean("isRead", false)
                        )
                    )
                }
            } catch (_: Exception) {
                notifications.clear()
            }
        } else {
            // Sample data for the first preview.
            notifications.addAll(
                listOf(
                    AppNotification(
                        1L, "Rent Due Reminder",
                        "Your rent payment is due soon.",
                        "Today", "rent", false
                    ),
                    AppNotification(
                        2L, "Payment Received",
                        "Your rent payment has been recorded.",
                        "Yesterday", "payment", false
                    ),
                    AppNotification(
                        3L, "Complaint Update",
                        "Check the latest status of your complaint.",
                        "2 days ago", "complaint", true
                    ),
                    AppNotification(
                        4L, "Owner Announcement",
                        "Please check for property maintenance updates.",
                        "3 days ago", "announcement", true
                    )
                )
            )
            saveNotifications()
        }

        renderNotifications()
    }

    private fun saveNotifications() {
        val array = JSONArray()

        notifications.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("title", item.title)
                put("message", item.message)
                put("time", item.time)
                put("type", item.type)
                put("isRead", item.isRead)
            }
            array.put(obj)
        }

        prefs.edit().putString("items", array.toString()).apply()
    }

    private fun renderNotifications() {
        container.removeAllViews()

        val filtered = if (showUnreadOnly) {
            notifications.filter { !it.isRead }
        } else {
            notifications
        }

        tvUnreadCount.text =
            notifications.count { !it.isRead }.toString()

        btnAll.alpha = if (!showUnreadOnly) 1f else 0.65f
        btnUnread.alpha = if (showUnreadOnly) 1f else 0.65f

        if (filtered.isEmpty()) {
            val empty = TextView(this).apply {
                text = if (showUnreadOnly) {
                    "You're all caught up!\nNo unread notifications."
                } else {
                    "No notifications yet."
                }
                textSize = 15f
                setTextColor(Color.GRAY)
                gravity = Gravity.CENTER
                setPadding(16, 60, 16, 60)
            }
            container.addView(empty)
            return
        }

        filtered.forEach { item ->
            container.addView(createNotificationCard(item))
        }
    }

    private fun createNotificationCard(
        item: AppNotification
    ): View {

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(14, 14, 14, 14)

            background = GradientDrawable().apply {
                setColor(
                    if (item.isRead) Color.WHITE
                    else Color.rgb(232, 240, 255)
                )
                cornerRadius = 18f
                setStroke(1, Color.rgb(225, 232, 241))
            }

            isClickable = true
            isFocusable = true
        }

        val icon = TextView(this).apply {
            text = when (item.type) {
                "rent" -> "📅"
                "payment" -> "₹"
                "complaint" -> "⚠"
                "announcement" -> "🏠"
                else -> "🔔"
            }
            textSize = 22f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(36, 90, 194))
            background = GradientDrawable().apply {
                setColor(Color.rgb(220, 232, 255))
                cornerRadius = 14f
            }
            layoutParams = LinearLayout.LayoutParams(48, 48)
        }

        val details = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            )
        }

        val title = TextView(this).apply {
            text = item.title
            textSize = 15f
            setTextColor(Color.rgb(23, 43, 77))
            setTypeface(null, Typeface.BOLD)
        }

        val message = TextView(this).apply {
            text = item.message
            textSize = 13f
            setTextColor(Color.rgb(90, 105, 125))
            setPadding(0, 5, 0, 5)
        }

        val time = TextView(this).apply {
            text = item.time
            textSize = 11f
            setTextColor(Color.GRAY)
        }

        details.addView(title)
        details.addView(message)
        details.addView(time)

        val status = TextView(this).apply {
            text = if (item.isRead) "" else "●"
            textSize = 12f
            setTextColor(Color.rgb(36, 90, 194))
        }

        card.addView(icon)
        card.addView(details)
        card.addView(status)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = 12

        card.layoutParams = params

        card.setOnClickListener {
            item.isRead = true
            saveNotifications()
            renderNotifications()

            Toast.makeText(
                this,
                "${item.title} marked as read",
                Toast.LENGTH_SHORT
            ).show()

            // You can navigate to the related page here.
        }

        return card
    }
}
