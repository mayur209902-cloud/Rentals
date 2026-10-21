
package com.example.rentals

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewComplaintActivity : AppCompatActivity() {

    private lateinit var tenantName: EditText
    private lateinit var tenantMobile: EditText
    private lateinit var property: Spinner
    private lateinit var roomNumber: EditText
    private lateinit var category: Spinner
    private lateinit var complaintTitle: EditText
    private lateinit var description: EditText
    private lateinit var complaintDate: EditText
    private lateinit var location: EditText
    private lateinit var priority: Spinner
    private lateinit var selectedMedia: TextView

    private var mediaUri: Uri? = null

    private val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )

    companion object {
        private const val PICK_MEDIA = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_complaint)

        tenantName = findViewById(R.id.etTenantName)
        tenantMobile = findViewById(R.id.etTenantMobile)
        property = findViewById(R.id.spProperty)
        roomNumber = findViewById(R.id.etRoomNumber)
        category = findViewById(R.id.spComplaintCategory)
        complaintTitle = findViewById(R.id.etComplaintTitle)
        description = findViewById(R.id.etComplaintDescription)
        complaintDate = findViewById(R.id.etComplaintDate)
        location = findViewById(R.id.etComplaintLocation)
        priority = findViewById(R.id.spPriority)
        selectedMedia = findViewById(R.id.tvSelectedMedia)

        complaintDate.setText(
            dateFormat.format(Calendar.getInstance().time)
        )

        complaintDate.setOnClickListener {
            showCalendar()
        }

        findViewById<Button>(R.id.btnUploadMedia)
            .setOnClickListener {
                openMediaPicker()
            }

        findViewById<Button>(R.id.btnSubmitComplaint)
            .setOnClickListener {
                submitComplaint()
            }
    }

    private fun showCalendar() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)

                complaintDate.setText(
                    dateFormat.format(selectedCalendar.time)
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun openMediaPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/*", "video/*")
            )
        }

        try {
            startActivityForResult(intent, PICK_MEDIA)
        } catch (_: Exception) {
            Toast.makeText(
                this,
                "Unable to open file picker",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @Deprecated("Use Activity Result APIs in new implementations")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_MEDIA && resultCode == RESULT_OK) {
            mediaUri = data?.data

            selectedMedia.text = if (mediaUri != null) {
                "Selected: ${mediaUri?.lastPathSegment ?: "File"}"
            } else {
                "No file selected"
            }
        }
    }

    private fun submitComplaint() {
        val name = tenantName.text.toString().trim()
        val mobile = tenantMobile.text.toString().trim()
        val room = roomNumber.text.toString().trim()
        val title = complaintTitle.text.toString().trim()
        val details = description.text.toString().trim()
        val complaintLocation = location.text.toString().trim()
        val date = complaintDate.text.toString().trim()

        if (name.isEmpty()) {
            tenantName.error = "Enter tenant name"
            tenantName.requestFocus()
            return
        }

        if (!mobile.matches(Regex("^[6-9][0-9]{9}$"))) {
            tenantMobile.error = "Enter valid 10-digit mobile number"
            tenantMobile.requestFocus()
            return
        }

        if (property.selectedItemPosition == 0) {
            Toast.makeText(
                this, "Select property", Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (room.isEmpty()) {
            roomNumber.error = "Enter room or flat number"
            roomNumber.requestFocus()
            return
        }

        if (category.selectedItemPosition == 0) {
            Toast.makeText(
                this, "Select complaint category", Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (title.isEmpty()) {
            complaintTitle.error = "Enter complaint title"
            complaintTitle.requestFocus()
            return
        }

        if (details.isEmpty()) {
            description.error = "Enter complaint description"
            description.requestFocus()
            return
        }

        if (date.isEmpty()) {
            Toast.makeText(
                this, "Select complaint date", Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Prepare complaint data
        val complaint = JSONObject().apply {
            put("name", name)
            put("mobile", mobile)
            put("property", property.selectedItem.toString())
            put("room", room)
            put("category", category.selectedItem.toString())
            put("title", title)
            put("description", details)
            put("date", date)
            put("location", complaintLocation)
            put("priority", priority.selectedItem.toString())
            put("status", "Pending")
            put("media", mediaUri?.toString() ?: "")
        }

        // Save to phone's local storage
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

        complaints.put(complaint)

        val saved = preferences.edit()
            .putString("complaints_list", complaints.toString())
            .commit()

        if (saved) {
            Toast.makeText(
                this,
                "Complaint submitted successfully!",
                Toast.LENGTH_LONG
            ).show()

            finish()
        } else {
            Toast.makeText(
                this,
                "Could not save complaint. Please try again.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
