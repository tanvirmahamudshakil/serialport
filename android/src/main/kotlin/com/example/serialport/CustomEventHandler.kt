package com.example.serialport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink


internal class CustomEventHandler : BroadcastReceiver(), EventChannel.StreamHandler {
    override fun onListen(arguments: Any, events: EventSink) {
        Companion.events = events
    }

    override fun onCancel(arguments: Any) {
        events = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (events != null) {
            events!!.success("Hello")
        }
    }

    companion object {
        var events: EventSink? = null
        val mainHandler: Handler = Handler(Looper.getMainLooper())


        fun sendEvent(response: String) {
            val runnable = Runnable {
                events!!.success(
                    response
                )
            }
            mainHandler.post(runnable)
        }
    }
}