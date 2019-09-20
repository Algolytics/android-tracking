package com.algolytics.tracker.api.data.listener

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker
import java.util.concurrent.TimeUnit

class AccelerometerListener(context: Context, private val delay: Long) : SensorEventListener {

    private val sensorManager: SensorManager? =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
    private var accelerometerSensor: Sensor? = null

    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null

    private var sensorList = mutableListOf<Accelerometer>()

    fun start() {
        accelerometerSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        handlerThread = HandlerThread(AccelerometerListener::class.java.simpleName)
        handlerThread?.start()
        handler = Handler(this.handlerThread?.looper)

        sensorManager?.registerListener(
            this,
            accelerometerSensor,
            TimeUnit.SECONDS.toMicros(1).toInt(),
            TimeUnit.SECONDS.toMicros(1).toInt(),
            handler
        )

        handler?.postDelayed(GetAccelerometerRegister(), 0)
    }


    override fun onAccuracyChanged(event: Sensor?, p1: Int) {
    }

    data class Accelerometer(val Xaxis: Float, val Yaxis: Float, val Zaxis: Float)

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            sensorList.add(Accelerometer(event.values[0], event.values[1], event.values[2]))
        }
    }

    data class AccelerometerRegister(val accelerometerValues: List<Accelerometer>)

    inner class GetAccelerometerRegister : Runnable {
        override fun run() {
            ApiTracker.getInstance().addToList(
                Event(
                    mapOf(
                        "AccelerometerRegister" to AccelerometerRegister(
                            sensorList.toList()
                        )
                    ), "ACCELEROMETER"
                )
            )
            sensorList.clear()
            handler?.postDelayed(this, delay)

        }
    }

}