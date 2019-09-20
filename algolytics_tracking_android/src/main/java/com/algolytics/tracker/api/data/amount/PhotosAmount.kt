package com.algolytics.tracker.api.data.amount

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker
import java.util.*


class PhotosAmount {
    companion object {

        private val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        private val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        data class Photos(val numberOfPhotos: Int, val lastPhotoDate: String?)

        fun getNumberOfPhotos(application: Application) {
            if (ContextCompat.checkSelfPermission(
                    application.applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

//            val photosPair = application.contentResolver.query(uri, projection, null, null, null)
//                .use { extractDataFromCursor(it) }

//
//            ApiTracker.getInstance().addToList(
//                Event(
//                    Photos(photosPair.second, photosPair.first),
//                    "PHOTOS"
//                )
//            )
        }
    }
}