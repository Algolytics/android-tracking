package com.algolytics.tracker.api.data.amount

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker

class NumberOfSMS {
    companion object {
        private const val inboxUri = "content://sms/inbox"
        private const val sentUri = "content://sms/sent"

        data class NumberOfSMS(val sent: Int, val lastSendMessage: String?, val inbox: Int, val lastInboxMessage: String?)

        fun getSMS(application: Application) {
            if (ContextCompat.checkSelfPermission(
                    application.applicationContext,
                    Manifest.permission.READ_SMS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val inboxPair = application.contentResolver.query(Uri.parse(inboxUri), null, null, null, null)
                .use { extractDataFromCursor(it) }

            val sentPair = application.contentResolver.query(Uri.parse(sentUri), null, null, null, null)
                .use { extractDataFromCursor(it) }

            ApiTracker.getInstance().addToList(
                Event(
                    NumberOfSMS(
                            inboxPair.second,
                            inboxPair.first,
                            sentPair.second,
                            sentPair.first
                        ), "SMS"
                )
            )
        }
    }

}