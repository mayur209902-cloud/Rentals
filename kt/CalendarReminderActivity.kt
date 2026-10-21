
package com.example.rentals

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class CalendarReminderActivity : BaseActivity() {

    private lateinit var btnStartDate: Button
    private lateinit var btnEndDate: Button
    private lateinit var tvDaysRemaining: TextView
    private lateinit var tvReminderStatus: TextView
    private lateinit var btnSetRentReminders: Button

    private val dateFormat =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var startDate: Date? = null
    private var endDate: Date? = null

    private val prefs by lazy {
        getSharedPreferences("TenantDetails", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_reminder)

        btnStartDate = findViewById(R.id.btnStartDate)
        btnEndDate = findViewById(R.id.btnEndDate)
        tvDaysRemaining = findViewById(R.id.tvDaysRemaining)
        tvReminderStatus = findViewById(R.id.tvReminderStatus)
        btnSetRentReminders = findViewById(R.id.btnSetRentReminders)

        setupBottomNavigation()

        btnStartDate.setOnClickListener {
            showDatePicker(isStart = true)
        }

        btnEndDate.setOnClickListener {
            showDatePicker(isStart = false)
        }

        btnSetRentReminders.setOnClickListener {
            Toast.makeText(
                this,
                "Reminder setup ke liye notification scheduling code add karna hoga.",
                Toast.LENGTH_LONG
            ).show()
        }

        loadSavedDates()
    }

    override fun onResume() {
        super.onResume()

        // Tenant Details se updated dates dobara load hongi
        if (::btnStartDate.isInitialized) {
            loadSavedDates()
        }
    }

    private fun loadSavedDates() {
        val savedStart = prefs.getString("startDate", "") ?: ""
        val savedEnd = prefs.getString("endDate", "") ?: ""

        startDate = parseDate(savedStart)
        endDate = parseDate(savedEnd)

        btnStartDate.text =
            if (startDate != null) dateFormat.format(startDate!!)
            else "Select Start Date"

        btnEndDate.text =
            if (endDate != null) dateFormat.format(endDate!!)
            else "Select End Date"

        updateDaysRemaining()
    }

    private fun parseDate(value: String): Date? {
        if (value.isBlank()) return null

        return try {
            dateFormat.isLenient = false
            dateFormat.parse(value)
        } catch (e: Exception) {
            null
        }
    }

    private fun showDatePicker(isStart: Boolean) {
        val calendar = Calendar.getInstance()

        val currentDate = if (isStart) startDate else endDate
        if (currentDate != null) {
            calendar.time = currentDate
        }

        val dialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val selectedDate = selectedCalendar.time

                if (isStart) {
                    if (endDate != null && !selectedDate.before(endDate)) {
                        Toast.makeText(
                            this,
                            "Start date, end date se pehle honi chahiye.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@DatePickerDialog
                    }

                    startDate = selectedDate
                    prefs.edit()
                        .putString("startDate", dateFormat.format(selectedDate))
                        .apply()

                } else {
                    if (startDate != null &&
                        !selectedDate.after(startDate)
                    ) {
                        Toast.makeText(
                            this,
                            "End date, start date ke baad honi chahiye.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@DatePickerDialog
                    }

                    endDate = selectedDate
                    prefs.edit()
                        .putString("endDate", dateFormat.format(selectedDate))
                        .apply()
                }

                loadSavedDates()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // End date must be after the selected start date.
        if (!isStart && startDate != null) {
            val minimumDate = Calendar.getInstance().apply {
                time = startDate!!
                add(Calendar.DAY_OF_YEAR, 1)
            }
            dialog.datePicker.minDate = minimumDate.timeInMillis
        }

        dialog.show()
    }

    private fun updateDaysRemaining() {
        val end = endDate

        if (end == null) {
            tvDaysRemaining.text = "Rental end date not selected"
            tvReminderStatus.text =
                "Tenant Details mein rental dates save karein."
            return
        }

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endCalendar = Calendar.getInstance().apply {
            time = end
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val difference = TimeUnit.MILLISECONDS.toDays(
            endCalendar.timeInMillis - today.timeInMillis
        )

        when {
            difference > 0 -> {
                tvDaysRemaining.text = "$difference Days Remaining"
                tvReminderStatus.text =
                    "Rent end date: ${dateFormat.format(end)}"
            }

            difference == 0L -> {
                tvDaysRemaining.text = "0 Days Remaining"
                tvReminderStatus.text = "Rental end date is today."
            }

            else -> {
                tvDaysRemaining.text = "Rental End Date Passed"
                tvReminderStatus.text =
                    "End date: ${dateFormat.format(end)}"
            }
        }
    }
}
