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


class LocationListener(private var context: Context, private val delay: Long) {

    private var handler: Handler? = null
    private var handlerThread: HandlerThread? = null

    private var mCurrentLocation: Location? = null

    private var lm: LocationManager? = null

    data class Position(val latitude: String, val longitude: String)

    init {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
                lm?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1.0f,
                    GetLastLocation()
                )

            }
            if (lm?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
                lm?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1.0f,
                    GetLastLocation()
                )
            }

            this.handlerThread = HandlerThread(LocationListener::class.java.simpleName)
            this.handlerThread?.start()
            handler = Handler(this.handlerThread?.looper)
            handler?.postDelayed(GetLastLocation(), 0)
        }
    }

    fun gotLocation(location: Location?) {
        ApiTracker.getInstance().addToList(
            Event(
                mapOf(
                    "Position" to
                            Position(
                                location?.latitude.toString(),
                                location?.longitude.toString()
                            )
                ), "POSITION"
            )
        )
    }

    internal inner class GetLastLocation : Runnable, LocationListener {

        override fun onLocationChanged(location: Location) {
            mCurrentLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }

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
                gotLocation(mCurrentLocation)
                handler?.postDelayed(this, delay)
            }
        }
    }
}