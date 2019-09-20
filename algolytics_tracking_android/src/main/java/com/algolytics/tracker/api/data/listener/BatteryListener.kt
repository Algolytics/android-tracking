package com.algolytics.tracker.api.data.listener

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Handler
import android.os.HandlerThread
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker


class BatteryListener(context: Context, private val delay: Long) {

    private var handler: Handler? = null
    private var handlerThread: HandlerThread? = null

    init {
        this.handlerThread = HandlerThread(BatteryListener::class.java.simpleName)
        this.handlerThread?.start()
        handler = Handler(this.handlerThread?.looper)
        handler?.postDelayed(GetBatteryLevel(context), 0)
    }

    data class BatteryInfo(
        val batteryLevel: Int,
        val isUsbCharging: Boolean,
        val isAcCharging: Boolean
    )

    inner class GetBatteryLevel(context: Context) : Runnable {

        var context: Context = context


        private fun getBatteryStatus(): Intent? {
            return IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                this.context.registerReceiver(null, ifilter)
            }
        }

        private fun getBatteryLevel(): Int {

            val batteryStatus: Intent? = getBatteryStatus()
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

            val batteryPct = level / scale.toFloat()

            return (batteryPct * 100).toInt()
        }

        private fun isUsbCharging(): Boolean {

            val batteryStatus: Intent? = getBatteryStatus()
            val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1

            return chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        }

        private fun isAcCharging(): Boolean {

            val batteryStatus: Intent? = getBatteryStatus()
            val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1

            return chargePlug == BatteryManager.BATTERY_PLUGGED_AC
        }

        private fun sendBatteryInfo() {
            ApiTracker.getInstance().addToList(
                Event(
                    mapOf(
                        "BatteryInfo" to BatteryInfo(
                            getBatteryLevel(),
                            isUsbCharging(),
                            isAcCharging()
                        )
                    ), "BATTERY"
                )
            )
        }

        override fun run() {
            sendBatteryInfo()
            handler?.postDelayed(this, delay)
        }
    }
}