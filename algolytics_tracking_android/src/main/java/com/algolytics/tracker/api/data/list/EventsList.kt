package com.algolytics.tracker.api.data.list

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker

class EventsList {

    data class Event(
        val title: String?,
        val description: String?,
        val eventLocation: String?,
        val dtstart: String?,
        val dtend: String?
    )

    companion object {


        fun getEvents(application: Application) {
            if (ContextCompat.checkSelfPermission(
                    application.applicationContext,
                    Manifest.permission.READ_SMS
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                val cursor: Cursor? = application.getContentResolver().query(
                    Uri.parse("content://com.android.calendar/events"),
                    arrayOf(
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DESCRIPTION,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND
                    ), null, null, null
                )
                var eventList: MutableList<Event> = mutableListOf<Event>()
                cursor.use {
                    while (cursor?.moveToNext() == true) {
                        val eventTitle = cursor.getString(
                            cursor.getColumnIndex(
                                CalendarContract.Events.TITLE
                            )
                        )
                        val eventDescription = cursor.getString(
                            cursor.getColumnIndex(
                                CalendarContract.Events.DESCRIPTION
                            )
                        )
                        val eventLocation = cursor.getString(
                            cursor.getColumnIndex(
                                CalendarContract.Events.EVENT_LOCATION
                            )
                        )
                        val eventDtstart = cursor.getString(
                            cursor.getColumnIndex(
                                CalendarContract.Events.DTSTART
                            )
                        )
                        val eventDtend = cursor.getString(
                            cursor.getColumnIndex(
                                CalendarContract.Events.DTEND
                            )
                        )

                        eventList.add(
                            Event(
                                eventTitle,
                                eventDescription,
                                eventLocation,
                                eventDtstart,
                                eventDtend
                            )
                        )

                    }
                }
                ApiTracker.getInstance().addToList(Event(mapOf("eventList" to eventList), "EVENTS"))
            }
        }
    }


}
