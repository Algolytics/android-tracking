package com.algolytics.tracker.api.data.amount

import android.database.Cursor

fun extractDataFromCursor(cursor: Cursor?): Pair<String?, Int> {
    var sentCounter1 = 0
    var lastSendMessageDate: String? = null
    if (cursor?.moveToFirst() == true) {
        do {
            if (sentCounter1 == 0) {
                lastSendMessageDate = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            }
            sentCounter1++
        } while (cursor.moveToNext())
    }
    return Pair(lastSendMessageDate, sentCounter1)
}
