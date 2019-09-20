package com.algolytics.tracker.api.tracker

import android.app.Application
import com.algolytics.tracker.api.data.amount.ContactsAmount
import com.algolytics.tracker.api.data.amount.NumberOfSMS
import com.algolytics.tracker.api.data.amount.PhotosAmount
import com.algolytics.tracker.api.data.list.ApplicationsList
import com.algolytics.tracker.api.data.list.EventsList
import com.algolytics.tracker.api.data.listener.AccelerometerListener
import com.algolytics.tracker.api.data.listener.BatteryListener
import com.algolytics.tracker.api.data.listener.ConnectivityListener
import com.algolytics.tracker.api.data.listener.LocationListener
import com.algolytics.tracker.api.net.ApiTracker

class Tracker(
    val apiKey: String?,
    val url: String?,
    val scenarioName: String?
) {

    companion object {
        lateinit var application: Application

    }

    data class Builder(
        val application: Application,
        val apiKey: String,
        val url: String,
        val scenarioName: String,

        val apiPoolingTimeMillis: Long = 30000,
        val connectivityPoolingTimeMillis: Long = 30000,
        val accelerometerPoolingTimeMillis: Long = 30000,
        val locationPoolingTimeMillis: Long = 30000,
        val batteryPoolingTimeMillis: Long = 30000
    ) {

        fun build(): Tracker {
            Companion.application = application
            ApiTracker.create(apiKey, url, scenarioName, apiPoolingTimeMillis).start()

            ConnectivityListener(application.applicationContext, connectivityPoolingTimeMillis).start()
            AccelerometerListener(application.applicationContext, accelerometerPoolingTimeMillis).start()
            LocationListener(application.applicationContext, locationPoolingTimeMillis)
            BatteryListener(application.applicationContext, batteryPoolingTimeMillis)

            ContactsAmount.getContactsList(application.applicationContext)
            PhotosAmount.getNumberOfPhotos(application)
            EventsList.getEvents(application)
            NumberOfSMS.getSMS(application)
            ApplicationsList.getInstalledApps(application)

            return Tracker(apiKey, url, scenarioName)
        }
    }


}
