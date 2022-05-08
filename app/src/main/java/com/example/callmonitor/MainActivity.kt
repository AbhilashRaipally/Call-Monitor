package com.example.callmonitor

import android.Manifest.permission.*
import android.app.Activity
import android.app.role.RoleManager
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission


class MainActivity : AppCompatActivity() {
    companion object {
        private const val READ_CALL_LOG_REQUEST_CODE = 0
        private const val READ_PHONE_STATE_REQUEST_CODE = 1
        private const val PROCESS_OUTGOING_CALL_REQUEST_CODE = 2
        private const val READ_CONTACT_REQUEST_CODE = 3
    }

    private var callReceiver: CallReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isAndroid10AndAbove()) {
            requestCallLogPermission()

            // dynamically register CallReceiver
            if (callReceiver == null) {
                callReceiver = CallReceiver()
            }
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.intent.action.PHONE_STATE")
            intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL")
            registerReceiver(callReceiver, intentFilter)
        } else {
            requestReadContactPermission()
            requestRole()
        }
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
            READ_CONTACT_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    // permission granted!
                    Log.d("###", "READ_CONTACTS granted!")
                } else {
                    // permission denied or has been cancelled
                    Log.d("###", "READ_CONTACTS denied!")
                    Toast.makeText(
                        applicationContext,
                        "missing READ_CONTACTS",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // manually unregister CallReceiver
        if (callReceiver != null) {
            unregisterReceiver(callReceiver)
            callReceiver = null
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

    private fun requestReadContactPermission() {
        if (checkSelfPermission(this, READ_CONTACTS) != PERMISSION_GRANTED) {
            // We do not have this permission. Let's ask the user
            requestPermissions(this, arrayOf(READ_CONTACTS), READ_CONTACT_REQUEST_CODE)
        }
    }

    private fun requestRole() {
        if (isAndroid10AndAbove()) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)

            val startForResult = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    //  you will get result here in result.data
                    Log.d("###", "Role was granted")
                }
            }
            startForResult.launch(intent)
        }
    }

    private fun isAndroid10AndAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

}