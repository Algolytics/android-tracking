package com.algolytics.tracker.api.aspect

import android.text.Editable
import com.algolytics.tracker.api.androidadapters.DefaultTextWatcher
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class TextAspect {
    @Before("execution(* com.algolytics.tracker.api.androidadapters.DefaultTextWatcher.afterTextChanged(android.text.Editable)) && args(e)")
    fun trackEditText(e: Editable, joinPoint: JoinPoint) {
        val textWatcher = joinPoint.`this` as DefaultTextWatcher
        ApiTracker.getInstance().addToList(
            Event(
                mapOf(
                    "name" to textWatcher.editText.resources.getResourceEntryName(textWatcher.editText.id),
                    "input" to e.toString()
                ), "TEXT_INSERTED"
            )
        )
    }


}