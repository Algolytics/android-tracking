package com.algolytics.tracker.api.data.listener

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.core.content.ContextCompat
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class LocationListener(private var context: Context, private val delay: Long) {

    private var handler: Handler? = null
    private var handlerThread: HandlerThread? = null

    private var fusedLocationClient: FusedLocationProviderClient? = null

    data class Position(val latitude: String, val longitude: String)

    init {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            this.handlerThread = HandlerThread(LocationListener::class.java.simpleName)
            this.handlerThread?.start()
            handler = Handler(this.handlerThread?.looper)
            handler?.postDelayed(GetLastLocation(), 0)
        }
    }

    fun gotLocation(location: Location?) {
        ApiTracker.getInstance().addToList(
            Event(
                Position(
                    location?.latitude.toString(),
                    location?.longitude.toString()
                ), "POSITION"
            )
        )
    }

    internal inner class GetLastLocation : Runnable {
        override fun run() {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.INTERNET
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else {
                fusedLocationClient?.lastLocation
                    ?.addOnSuccessListener { location: Location? ->
                        gotLocation(location)
                    }

                handler?.postDelayed(this, delay)
            }
        }
    }
}