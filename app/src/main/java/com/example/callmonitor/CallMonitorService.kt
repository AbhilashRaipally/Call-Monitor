package com.example.callmonitor

import android.net.Uri
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.widget.Toast
import java.util.*

class CallMonitorService : CallScreeningService() {

    /**
     * Called when a new incoming or outgoing call is added which is not in the user's contact list.
     *
     * https://issuetracker.google.com/issues/130081372
     */
    override fun onScreenCall(callDetails: Call.Details) {
        Log.d("###", callDetails.formattedPhoneNumber)
        displayToast("call from : ${callDetails.formattedPhoneNumber}")
        //displayToast(getPhoneNumber(callDetails))
        respondToCall(callDetails, CallResponse.Builder().build())
    }


    private val Call.Details.formattedPhoneNumber: String get() {
        return when (val phoneNumber = handle?.schemeSpecificPart) {
            else -> PhoneNumberUtils.formatNumber(
                phoneNumber,
                Locale.getDefault().country
            )
        }
    }

    private fun displayToast(message: String) {
        Toast.makeText(this.applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun getPhoneNumber(callDetails: Call.Details): String {
        return callDetails.handle.toString().removeTelPrefix().parseCountryCode()
    }

    private fun String.removeTelPrefix() = this.replace(TEL_PREFIX, "")
    private fun String.parseCountryCode(): String = Uri.decode(this)

    companion object {
        const val TEL_PREFIX = "tel:"
    }
}