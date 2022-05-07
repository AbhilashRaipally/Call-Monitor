package com.example.callmonitor

import android.Manifest.permission.*
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission

class MainActivity : AppCompatActivity() {
    companion object {
        private const val READ_CALL_LOG_REQUEST_CODE = 0
        private const val READ_PHONE_STATE_REQUEST_CODE = 1
        private const val PROCESS_OUTGOING_CALL_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestCallLogPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_CALL_LOG_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    // permission granted!
                    Log.d("###", "READ_CALL_LOG granted!")
                    // check READ_PHONE_STATE permission only when READ_CALL_LOG is granted

                    Log.d("###", "requesting Phone state")
                    requestPhoneStatePermission()
                } else {
                    // permission denied or has been cancelled
                    Log.d("###", "READ_CALL_LOG denied!")
                    Toast.makeText(applicationContext, "missing READ_CALL_LOG", Toast.LENGTH_LONG)
                        .show()
                }
            }
            READ_PHONE_STATE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    // permission granted!
                    Log.d("###", "READ_PHONE_STATE granted!")
                    // check PROCESS_OUTGOING_CALLS permission only when READ_PHONE_STATE is granted
                    requestProcessOutGoingCallPermission()
                } else {
                    // permission denied or has been cancelled
                    Log.d("###", "READ_PHONE_STATE denied!")
                    Toast.makeText(
                        applicationContext,
                        "missing READ_PHONE_STATE",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            PROCESS_OUTGOING_CALL_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    // permission granted!
                    Log.d("###", "PROCESS_OUTGOING_CALLS granted!")
                } else {
                    // permission denied or has been cancelled
                    Log.d("###", "PROCESS_OUTGOING_CALLS denied!")
                    Toast.makeText(
                        applicationContext,
                        "missing PROCESS_OUTGOING_CALLS",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestProcessOutGoingCallPermission() {
        if (checkSelfPermission(this, PROCESS_OUTGOING_CALLS) != PERMISSION_GRANTED) {
            // We do not have this permission. Let's ask the user
            requestPermissions(
                this,
                arrayOf(PROCESS_OUTGOING_CALLS),
                PROCESS_OUTGOING_CALL_REQUEST_CODE
            )
        }
    }

    private fun requestPhoneStatePermission() {
        if (checkSelfPermission(this, READ_PHONE_STATE) != PERMISSION_GRANTED) {
            // We do not have this permission. Let's ask the user
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE)) {
                requestPermissions(
                    this,
                    arrayOf(READ_PHONE_STATE),
                    READ_PHONE_STATE_REQUEST_CODE
                )
            } else {
                requestPermissions(
                    this,
                    arrayOf(READ_PHONE_STATE),
                    READ_PHONE_STATE_REQUEST_CODE
                )
            }
        }
    }

    private fun requestCallLogPermission() {
        if (checkSelfPermission(this, READ_CALL_LOG) != PERMISSION_GRANTED) {
            // We do not have this permission. Let's ask the user
            requestPermissions(this, arrayOf(READ_CALL_LOG), READ_CALL_LOG_REQUEST_CODE)
        }
    }
}