
package com.example.rentals

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TenantDetailsActivity : AppCompatActivity() {

    private lateinit var fullName: EditText
    private lateinit var mobile: EditText
    private lateinit var secondMobile: EditText
    private lateinit var temporaryAddress: EditText
    private lateinit var permanentAddress: EditText
    private lateinit var email: EditText
    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private lateinit var documentStatus: Spinner
    private lateinit var depositFees: EditText

    private val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    ).apply {
        isLenient = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant_details)

        // Connect XML fields
        fullName = findViewById(R.id.etFullName)
        mobile = findViewById(R.id.etMobile)
        secondMobile = findViewById(R.id.etSecondMobile)
        temporaryAddress = findViewById(R.id.etTemporaryAddress)
        permanentAddress = findViewById(R.id.etPermanentAddress)
        email = findViewById(R.id.etEmail)
        startDate = findViewById(R.id.etRentalStartDate)
        endDate = findViewById(R.id.etRentalEndDate)
        documentStatus = findViewById(R.id.spDocumentStatus)
        depositFees = findViewById(R.id.etDepositFees)

        // Open calendar when date fields are tapped
        startDate.setOnClickListener {
            showDatePicker(isStartDate = true)
        }

        endDate.setOnClickListener {
            showDatePicker(isStartDate = false)
        }

        findViewById<Button>(R.id.btnSaveTenant).setOnClickListener {
            saveTenant()
        }

        findViewById<Button>(R.id.btnClearTenant).setOnClickListener {
            clearForm()
        }
    }

    private fun showDatePicker(isStartDate: Boolean) {

        val target = if (isStartDate) startDate else endDate
        val calendar = Calendar.getInstance()

        // Use previously selected date, if available
        val existingDate = target.text.toString().trim()

        if (existingDate.isNotEmpty()) {
            try {
                val parsedDate = dateFormat.parse(existingDate)
                if (parsedDate != null) {
                    calendar.time = parsedDate
                }
            } catch (_: Exception) {
                // Keep today's date if parsing fails
            }
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

                val selectedDate = dateFormat.format(selectedCalendar.time)

                if (isStartDate) {
                    startDate.setText(selectedDate)

                    // Clear end date if it is no longer after start date
                    val oldEndDate = endDate.text.toString().trim()

                    if (oldEndDate.isNotEmpty()) {
                        val end = try {
                            dateFormat.parse(oldEndDate)
                        } catch (_: Exception) {
                            null
                        }

                        if (end == null || !end.after(selectedCalendar.time)) {
                            endDate.text.clear()
                        }
                    }

                } else {
                    endDate.setText(selectedDate)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        if (isStartDate) {
            // Start date cannot be after the selected end date
            val endText = endDate.text.toString().trim()

            if (endText.isNotEmpty()) {
                try {
                    val end = dateFormat.parse(endText)
                    if (end != null) {
                        val maxCalendar = Calendar.getInstance().apply {
                            time = end
                            add(Calendar.DAY_OF_MONTH, -1)
                        }
                        dialog.datePicker.maxDate = maxCalendar.timeInMillis
                    }
                } catch (_: Exception) {
                    // Ignore invalid existing end date
                }
            }

        } else {
            // End date must be at least one day after start date
            val startText = startDate.text.toString().trim()

            if (startText.isNotEmpty()) {
                try {
                    val start = dateFormat.parse(startText)

                    if (start != null) {
                        val minCalendar = Calendar.getInstance().apply {
                            time = start
                            add(Calendar.DAY_OF_MONTH, 1)
                        }

                        dialog.datePicker.minDate = minCalendar.timeInMillis
                    }
                } catch (_: Exception) {
                    // Ignore invalid existing start date
                }
            }
        }

        dialog.show()
    }


    private fun saveTenant() {
        val name = fullName.text.toString().trim()
        val phone = mobile.text.toString().trim()
        val secondPhone = secondMobile.text.toString().trim()
        val tempAddress = temporaryAddress.text.toString().trim()
        val permAddress = permanentAddress.text.toString().trim()
        val emailText = email.text.toString().trim()
        val start = startDate.text.toString().trim()
        val end = endDate.text.toString().trim()
        val depositText = depositFees.text.toString().trim()

        if (name.isEmpty()) {
            fullName.error = "Enter full name"
            fullName.requestFocus()
            return
        }

        if (!phone.matches(Regex("^[6-9][0-9]{9}$"))) {
            mobile.error = "Enter a valid 10-digit mobile number"
            mobile.requestFocus()
            return
        }

        if (secondPhone.isNotEmpty() &&
            !secondPhone.matches(Regex("^[6-9][0-9]{9}$"))
        ) {
            secondMobile.error = "Enter a valid second mobile number"
            secondMobile.requestFocus()
            return
        }

        if (tempAddress.isEmpty()) {
            temporaryAddress.error = "Enter temporary address"
            temporaryAddress.requestFocus()
            return
        }

        if (permAddress.isEmpty()) {
            permanentAddress.error = "Enter permanent address"
            permanentAddress.requestFocus()
            return
        }

        if (emailText.isNotEmpty() &&
            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
        ) {
            email.error = "Enter a valid email address"
            email.requestFocus()
            return
        }

        if (start.isEmpty()) {
            startDate.error = "Select rental start date"
            startDate.requestFocus()
            return
        }

        if (end.isEmpty()) {
            endDate.error = "Select rental end date"
            endDate.requestFocus()
            return
        }

        val startParsed = try {
            dateFormat.parse(start)
        } catch (_: Exception) {
            null
        }

        val endParsed = try {
            dateFormat.parse(end)
        } catch (_: Exception) {
            null
        }

        if (startParsed == null || endParsed == null ||
            !endParsed.after(startParsed)
        ) {
            endDate.error = "End date must be after start date"
            endDate.requestFocus()
            return
        }

        val deposit = depositText.toDoubleOrNull()

        if (deposit == null || deposit < 0) {
            depositFees.error = "Enter a valid deposit amount"
            depositFees.requestFocus()
            return
        }

        val status = documentStatus.selectedItem.toString()

        // Save tenant details locally on the phone.
        val prefs = getSharedPreferences("TenantDetails", MODE_PRIVATE)

        val saved = prefs.edit()
            .putString("fullName", name)
            .putString("mobile", phone)
            .putString("secondMobile", secondPhone)
            .putString("temporaryAddress", tempAddress)
            .putString("permanentAddress", permAddress)
            .putString("email", emailText)
            .putString("startDate", start)
            .putString("endDate", end)
            .putString("deposit", deposit.toString())
            .putString("documentStatus", status)
            .commit()

        if (saved) {
            Toast.makeText(
                this,
                "Tenant details saved successfully",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        } else {
            Toast.makeText(
                this,
                "Unable to save details. Please try again.",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun clearForm() {
        fullName.text.clear()
        mobile.text.clear()
        secondMobile.text.clear()
        temporaryAddress.text.clear()
        permanentAddress.text.clear()
        email.text.clear()
        startDate.text.clear()
        endDate.text.clear()
        depositFees.text.clear()
        documentStatus.setSelection(0)

        Toast.makeText(
            this,
            "Form cleared",
            Toast.LENGTH_SHORT
        ).show()
    }
}