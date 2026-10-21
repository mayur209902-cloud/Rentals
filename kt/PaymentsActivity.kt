
package com.example.rentals

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class PaymentsActivity : BaseActivity() {

    private lateinit var amountInput: EditText
    private lateinit var upiInput: EditText
    private lateinit var qrImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)
        setupBottomNavigation()

        amountInput = findViewById(R.id.etRentAmount)
        upiInput = findViewById(R.id.etUpiId)
        qrImage = findViewById(R.id.imgPaymentQr)

        findViewById<Button>(R.id.btnGenerateQr)
            .setOnClickListener {
                generateQr()
            }

        findViewById<Button>(R.id.btnPayUpi)
            .setOnClickListener {
                payWithUpi()
            }
    }

    private fun paymentUri(): Uri? {
        val amount = amountInput.text.toString().trim()
        val upiId = upiInput.text.toString().trim()

        if (amount.toDoubleOrNull() == null ||
            amount.toDouble() <= 0
        ) {
            amountInput.error = "Enter a valid amount"
            return null
        }

        if (upiId.isBlank() || !upiId.contains("@")) {
            upiInput.error = "Enter landlord UPI ID"
            return null
        }

        return Uri.parse(
            "upi://pay?pa=${Uri.encode(upiId)}" +
                    "&pn=${Uri.encode("Rent Payment")}" +
                    "&am=${Uri.encode(amount)}" +
                    "&cu=INR" +
                    "&tn=${Uri.encode("Rental payment")}"
        )
    }

    private fun generateQr() {
        val uri = paymentUri() ?: return

        try {
            val matrix = QRCodeWriter().encode(
                uri.toString(),
                BarcodeFormat.QR_CODE,
                600,
                600
            )

            val bitmap = Bitmap.createBitmap(
                600, 600, Bitmap.Config.RGB_565
            )

            for (x in 0 until 600) {
                for (y in 0 until 600) {
                    bitmap.setPixel(
                        x, y,
                        if (matrix[x, y]) Color.BLACK
                        else Color.WHITE
                    )
                }
            }

            qrImage.setImageBitmap(bitmap)
            qrImage.visibility = View.VISIBLE

        } catch (_: Exception) {
            Toast.makeText(
                this, "QR generation failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun payWithUpi() {
        val uri = paymentUri() ?: return

        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity(
                Intent.createChooser(intent, "Pay rent using")
            )
        } catch (_: Exception) {
            Toast.makeText(
                this, "No compatible UPI app found",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
