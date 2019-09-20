package com.algolytics.tracker.api.data.list

import android.Manifest
import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker


class ApplicationsList {
    companion object {

        data class ApplicationList(val applicationList: List<String>)

        fun getInstalledApps(application: Application) {
            if (ContextCompat.checkSelfPermission(
                    application.applicationContext,
                    Manifest.permission.READ_SMS
                )
                == PackageManager.PERMISSION_GRANTED
            ) {

                val applicationsList = application.packageManager.getInstalledPackages(0)
                    .filter { isSystemPackage(it) }
                    .map { it.applicationInfo.loadLabel(application.packageManager).toString() }
                    .toList()
                ApiTracker.getInstance().addToList(
                    Event(
                        ApplicationList(applicationsList),
                        "APPLICATIONS_LIST"
                    )
                )
            }
        }

        private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
            return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }

    }


}