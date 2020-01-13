package com.algolytics.tracker.api.tracker

import android.app.Application
import com.algolytics.tracker.api.aspect.model.AspectConfig
import com.algolytics.tracker.api.data.amount.ContactsAmount
import com.algolytics.tracker.api.data.amount.NumberOfSMS
import com.algolytics.tracker.api.data.amount.PhotosAmount
import com.algolytics.tracker.api.data.list.ApplicationsList
import com.algolytics.tracker.api.data.list.EventsList
import com.algolytics.tracker.api.data.listener.*
import com.algolytics.tracker.api.net.ApiTracker

class Tracker(
    val apiKey: String?,
    val url: String?,
    val scenarioName: String?
) {

    companion object {
        lateinit var application: Application
        lateinit var aspectConfig: AspectConfig

    }

    data class Builder(
        val application: Application,
        val apiKey: String,
        val url: String,
        val scenarioName: String
    ) {
        private var aspectConfig: AspectConfig = AspectConfig()
        private var connectivity: Boolean = false
        private var accelerometer: Boolean = false
        private var location: Boolean = false
        private var battery: Boolean = false
        private var wifi: Boolean = false
        private var contacts: Boolean = false
        private var photos: Boolean = false
        private var calendar: Boolean = false
        private var applications: Boolean = false
        private var apiPoolingTimeMillis: Long = 30000
        private var connectivityPoolingTimeMillis: Long = 30000
        private var accelerometerPoolingTimeMillis: Long = 30000
        private var locationPoolingTimeMillis: Long = 30000
        private var batteryPoolingTimeMillis: Long = 30000
        private var wifiPoolingTimeMillis: Long = 30000


        fun enableConnectivity() = apply { this.connectivity = true }
        fun enableAccelerometer() = apply { this.accelerometer = true }
        fun enableLocation() = apply { this.location = true }
        fun enableBattery() = apply { this.battery = true }
        fun enableWifi() = apply { this.wifi = true }
        fun enableContacts() = apply { this.contacts = true }
        fun enablePhotos() = apply { this.photos = true }
        fun enableCalendar() = apply { this.calendar = true }
        fun enableApplications() = apply { this.applications = true }

        fun aspectConfig(aspectConfig: AspectConfig) = apply { this.aspectConfig = aspectConfig }

        fun apiPoolingTimeMillis(time: Long) = apply {
            if (time < 1000) return@apply
            else this.apiPoolingTimeMillis = time
        }

        fun connectivityPoolingTimeMillis(time: Long) = apply {
            if (time < 1000) return@apply
            else this.connectivityPoolingTimeMillis = time
        }

        fun accelerometerPoolingTimeMillis(time: Long) = apply {
            if (time < 1000) return@apply
            else this.accelerometerPoolingTimeMillis = time
        }

        fun locationPoolingTimeMillis(time: Long) = apply {
            if (time < 1000) return@apply
            else this.locationPoolingTimeMillis = time
        }

        fun batteryPoolingTimeMillis(time: Long) = apply {
            if (time < 1000) return@apply
            else this.batteryPoolingTimeMillis = time
        }

        fun wifiPoolingTimeMillis(time: Long) = apply {
            if (time < 1000) return@apply
            else this.wifiPoolingTimeMillis = time
        }


        fun build(): Tracker {
            Companion.application = application
            Companion.aspectConfig = aspectConfig
            ApiTracker.create(apiKey, url, scenarioName, apiPoolingTimeMillis).start()

            if (connectivity) {
                ConnectivityListener(
                    application.applicationContext,
                    connectivityPoolingTimeMillis
                ).start()
            }
            if (accelerometer) {
                AccelerometerListener(
                    application.applicationContext,
                    accelerometerPoolingTimeMillis
                ).start()
            }
            if (location) {
                LocationListener(application.applicationContext, locationPoolingTimeMillis)
            }
            if (battery) {
                BatteryListener(application.applicationContext, batteryPoolingTimeMillis)
            }
            if (wifi) {
                WifiListener(
                    application.applicationContext,
                    wifiPoolingTimeMillis
                ).start()
            }
            if (contacts) {
                ContactsAmount.getContactsList(application.applicationContext)
            }
            if (photos) {
                PhotosAmount.getNumberOfPhotos(application)
            }
            if (calendar) {
                EventsList.getEvents(application)
            }

            if (applications) {
                ApplicationsList.getInstalledApps(application)
            }

            return Tracker(apiKey, url, scenarioName)
        }
    }


}
