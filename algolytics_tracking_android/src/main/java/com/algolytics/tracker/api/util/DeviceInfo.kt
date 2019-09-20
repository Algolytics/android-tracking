package com.algolytics.tracker.api.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import com.algolytics.tracker.api.tracker.Tracker
import java.util.*

class DeviceInfo {
    companion object {

        val SDK_VERSION = "3.3.17"
        val SDK_VERSION_CODE = "53"
        val MODEL = Build.MODEL
        val MANUFACTURER = Build.MANUFACTURER
        val TYPE = "SMARTPHONE"
        val OS = "android"
        val OS_VERSION = Build.VERSION.RELEASE
        val OS_LANGUAGE = Locale.getDefault().language

        val deviceId: String
            @SuppressLint("HardwareIds")
            get() = Settings.Secure.getString(
                Tracker.application!!.contentResolver,
                "android_id"
            )

        val appVersion: String?
            get() {
                return "" + Tracker.application?.packageManager?.getPackageInfo(
                    Tracker.application!!.packageName, 0
                )?.versionName

            }

        val appVersionCode: String?
            get() {
                return "" + Tracker.application?.packageManager?.getPackageInfo(
                    Tracker.application!!.packageName,
                    0
                )?.versionCode

            }

        val screenSize: Point
            get() {
                val displaySize = Point()
                val wm =
                    Tracker.application!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                if (wm != null) {
                    val display = wm.defaultDisplay
                    display?.getSize(displaySize)
                }

                return displaySize
            }
    }


}