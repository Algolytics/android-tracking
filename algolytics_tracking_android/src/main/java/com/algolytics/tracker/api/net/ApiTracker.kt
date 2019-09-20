package com.algolytics.tracker.api.net

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.util.DeviceInfo
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.locks.ReentrantLock


class ApiTracker private constructor(
    private val trackerEndpoint: TrackerEndpoint,
    private val apiKey: String,
    private val scenarioName: String,
    private val delay: Long
) : Callback<Any> {

    private var handler: Handler? = null
    private var lock = ReentrantLock()
    var eventList = mutableListOf<Event<in Any>>()

    companion object {
        private var INSTANCE: ApiTracker? = null

        fun getInstance(): ApiTracker {
            return INSTANCE ?: throw IllegalStateException()
        }

        fun create(apiKey: String, url: String, scenarioName: String, delay: Long): ApiTracker {
            check(INSTANCE == null) { "There is an instance of ApiTracker" }

            val gson = GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val trackerEndpoint = retrofit.create(TrackerEndpoint::class.java)

            val api = ApiTracker(trackerEndpoint, apiKey, scenarioName, delay)
            INSTANCE = api

            return api
        }
    }

    fun start() {
        val handlerThread = HandlerThread(ApiTracker::class.java.simpleName)
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        handler?.postDelayed(SendPeriodicEvent(), 0)
    }


    fun addToList(event: Event<Any>) {
        lock.lock()
        try {
            eventList.add(event)
        } finally {
            lock.unlock()
        }
    }

    data class myEvent(val eventList: List<Event<in Any>>) {
        val phoneInformation: Map<String, String?> = mapOf(
            "version" to DeviceInfo.appVersion,
            "deviceId" to DeviceInfo.deviceId,
            "deviceModel" to DeviceInfo.MODEL,
            "deviceManufacturer" to DeviceInfo.MANUFACTURER,
            "deviceResolution" to "x:" + DeviceInfo.screenSize.x.toString() + ", y:" + DeviceInfo.screenSize.y.toString(),
            "deviceType" to "SMARTPHONE",
            "os" to "android",
            "osVersion" to DeviceInfo.OS_VERSION,
            "osLanguage" to DeviceInfo.OS_LANGUAGE
        )
    }

    fun sendList(list: List<Event<in Any>>) {
        trackerEndpoint.sendEvent(scenarioName, apiKey, myEvent(list)).enqueue(this)
    }

    override fun onFailure(call: Call<Any>, t: Throwable) {
        Log.e(
            "TrackerApi",
            "Error during connection with tracker api destination server with error.",
            t
        )
    }

    override fun onResponse(call: Call<Any>, response: Response<Any>) {

    }

    inner class SendPeriodicEvent : Runnable {

        override fun run() {
            lock.lock()
            try {
                val list = eventList.toList()
                eventList.clear()
                sendList(list)
            } finally {
                lock.unlock()
            }
            handler?.postDelayed(this, delay)
        }
    }

}