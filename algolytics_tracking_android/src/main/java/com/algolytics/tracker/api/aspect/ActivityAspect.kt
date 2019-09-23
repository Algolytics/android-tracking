package com.algolytics.tracker.api.aspect

import android.app.Activity
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker
import com.algolytics.tracker.api.tracker.Tracker
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
open class ActivityAspect {

    @Before("execution(* android.app.Activity.onCreate(..))")
    fun trackTouch(joinPoint: JoinPoint) {
        if (!Tracker.aspectConfig.activities) {
            return
        }
        val activity = joinPoint.`this` as Activity
        ApiTracker.getInstance().addToList(
            Event(
                activity::class.java.simpleName
                , "ACTIVITY_CHANGED"
            )
        )
    }

}