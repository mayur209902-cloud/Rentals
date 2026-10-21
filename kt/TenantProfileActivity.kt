
package com.example.rentals

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TenantProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant_profile)
        setupBottomNavigation()

        findViewById<Button>(R.id.btnEditTenantProfile)
            .setOnClickListener {
                startActivity(
                    Intent(this, TenantDetailsActivity::class.java)
                )
            }
    }

    override fun onResume() {
        super.onResume()

        val prefs = getSharedPreferences(
            "TenantDetails",
            MODE_PRIVATE
        )

        fun value(key: String): String {
            val savedValue = prefs.getString(key, null)
            return if (savedValue.isNullOrBlank()) {
                "Not added"
            } else {
                savedValue
            }
        }

        val profileText = """
            PERSONAL DETAILS

            Full Name
            ${value("fullName")}

            Mobile Number
            ${value("mobile")}

            Second Mobile Number
            ${value("secondMobile")}

            Email ID
            ${value("email")}


            ADDRESS DETAILS

            Temporary Address
            ${value("temporaryAddress")}

            Permanent Address
            ${value("permanentAddress")}


            RENTAL DETAILS

            Rental Start Date
            ${value("startDate")}

            Rental End Date
            ${value("endDate")}

            Deposit Fees
            ₹${value("deposit")}


            DOCUMENT DETAILS

            Document Verification
            ${value("documentStatus")}
        """.trimIndent()

        findViewById<TextView>(R.id.tvTenantProfile).text =
            profileText
    }
}
