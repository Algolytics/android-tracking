package com.algolytics.tracker.api.aspect

import android.view.View
import android.widget.*
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before


@Aspect
class ClickAspect {

    @Before("execution(* android.view.View.OnClickListener.onClick(android.view.View)) && args(view)")
    fun trackClick(view: View?) {
        Toast.makeText(view!!.context, "trackClick", Toast.LENGTH_SHORT).show();
        onClickOrTouchInteracted(view, EVENT_NAME)
    }

    @Before("execution(* android.view.View.OnLongClickListener.onLongClick(android.view.View)) && args(view)")
    fun trackLongClick(view: View?) {
        Toast.makeText(view!!.context, "trackLongClick", Toast.LENGTH_SHORT).show();

    }

    companion object {
        private val EVENT_NAME = "click"
        private val LABEL_INTERACTION_LONG_CLICK = "longClick"
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
    }

}
