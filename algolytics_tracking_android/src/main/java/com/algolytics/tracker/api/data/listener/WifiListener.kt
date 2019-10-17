package com.algolytics.tracker.api.data.listener

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.core.content.ContextCompat
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker
import java.util.concurrent.TimeUnit

class WifiListener (private val context: Context, private val delay: Long)  {

    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null

    val wifiScanReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }

    private fun scanSuccess() {
        val results = wifiManager.scanResults
    }

    private fun scanFailure() {
        val results = wifiManager.scanResults
    }


    fun start() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE)
            == PackageManager.PERMISSION_GRANTED
        ){
            handlerThread = HandlerThread(WifiListener::class.java.simpleName)
            handlerThread?.start()
            handler = Handler(this.handlerThread?.looper)

            handler?.postDelayed(GetWifiList(), 0)
        }

    }


    inner class GetWifiList : Runnable {
        override fun run() {
            val intentFilter = IntentFilter()

            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            context.registerReceiver(wifiScanReceiver, intentFilter)

            val success = wifiManager.startScan()
            if (!success) {
                scanFailure()
            }
            val results = wifiManager.scanResults

            ApiTracker.getInstance().addToList(
                Event(results
                    , "SCANNED_WIFI"
                )
            )

            handler?.postDelayed(this, delay)
        }
    }

}