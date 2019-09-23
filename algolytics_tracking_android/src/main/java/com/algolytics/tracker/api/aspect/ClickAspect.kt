package com.algolytics.tracker.api.aspect

import android.view.View
import android.widget.*
import com.algolytics.tracker.api.aspect.model.ClickEvent
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker
import com.algolytics.tracker.api.tracker.Tracker
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before


@Aspect
class ClickAspect {

    @Before("execution(* android.view.View.OnClickListener.onClick(android.view.View)) && args(view)")
    fun trackClick(view: View?) {
        if (!Tracker.aspectConfig.clicks) {
            return
        }
        view?.run { onClickOrTouchInteracted(view, EVENT_NAME) }
    }



    companion object {
        private val EVENT_NAME = "click"
    }


    private fun onClickOrTouchInteracted(view: View, labelInteraction: String) {
        val id = if (view.id == View.NO_ID) "no-id" else view.resources.getResourceName(view.id)
        val map = when (view) {
            is SeekBar -> mapOf("progress" to view.progress)
            is RatingBar -> mapOf("rating" to view.rating)
            is ProgressBar -> mapOf("progress" to view.progress)
            is DatePicker -> mapOf("date" to view.year.toString() + "-" + (view.month + 1) + "-" + view.dayOfMonth)
            is TimePicker -> mapOf("hour" to view.currentHour, "minute" to view.currentMinute)
            is NumberPicker -> mapOf("value" to view.value)
            is Spinner -> {
                when (val item = view.selectedItem) {
                    is String -> mapOf("values" to item)
                    is TextView -> mapOf("values" to item.text.toString())
                    else -> mapOf()
                }
            }
            is CompoundButton -> mapOf("checked" to view.isChecked)

            else -> mapOf()
        }

        ApiTracker.getInstance().addToList(Event(ClickEvent(id, map), labelInteraction))
    }

}
