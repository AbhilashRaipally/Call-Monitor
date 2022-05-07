package com.example.callmonitor

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.util.*

class CallReceiver : PhoneCallReceiver() {
    override fun onIncomingCallStarted(ctx: Context, number: String?, start: Date?) {
        val msg = "start incoming call: $number at $start"

        Log.d("###", msg)
        Toast.makeText(ctx.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date?) {
        val msg = "start outgoing call: $number at $start"

        Log.d("###", msg)
        Toast.makeText(ctx.applicationContext, msg, Toast.LENGTH_SHORT).show()

    }

    override fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        val msg = "end incoming call: $number at $end"

        Log.d("###", msg)
        Toast.makeText(ctx.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        val msg = "end outgoing call: $number at $end"

        Log.d("###", msg)
        Toast.makeText(ctx.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onMissedCall(ctx: Context, number: String?, missed: Date?) {
        val msg = "missed call: $number at $missed"

        Log.d("###", msg)
        Toast.makeText(ctx.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}