package com.algolytics.tracker.api.data.listener

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import androidx.core.content.ContextCompat
import com.algolytics.tracker.api.data.listener.model.connectivity.ConnectivityType
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker


class ConnectivityListener(private val context: Context, private val delay: Long) {

    companion object {
        const val EVENT_NAME = "CONNECTION_INFO"
    }

    private var handler: Handler? = null

    fun start() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val handlerThread = HandlerThread(ConnectivityListener::class.java.simpleName)
            handlerThread.start()
            handler = Handler(handlerThread.looper)
            handler?.postDelayed(GetConnectionInfo(), 0)
        }
    }

    inner class GetConnectionInfo : Runnable {

        private fun getConnectionType(): ConnectivityType {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        return when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectivityType.WIFI
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectivityType.MOBILE
                            else -> ConnectivityType.NO_CONNECTIVITY
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        return when (type) {
                            ConnectivityManager.TYPE_WIFI -> ConnectivityType.WIFI
                            ConnectivityManager.TYPE_MOBILE -> ConnectivityType.MOBILE
                            else -> ConnectivityType.NO_CONNECTIVITY

                        }
                    }
                }
            }
            return ConnectivityType.NO_CONNECTIVITY
        }

        override fun run() {
            val type = getConnectionType()
            ApiTracker.getInstance()
                .addToList(Event(type, EVENT_NAME))

            handler?.postDelayed(this, delay)
        }
    }
}