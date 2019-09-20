package com.algolytics.tracker.api.data.amount

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker

class ContactsAmount {
    companion object {

        data class ContactsNumber(val contactsNumber: Int)

        fun getContactsList(context: Context) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val cursor =
                context.contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )

            val contactsNumber = cursor?.use {
                (1..it.count)
                    .map {
                        cursor.moveToNext()
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    }
                    .filter { num -> num > 0 }
                    .count()
            } ?: 0

            ApiTracker.getInstance().addToList(
                Event(
                    ContactsNumber(
                        contactsNumber
                    ),
                    "CONTACT_NUMBER"
                )
            )
            cursor?.close()
        }
    }
}