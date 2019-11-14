package com.algolytics.tracker.api.androidadapters

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.algolytics.tracker.api.model.Event
import com.algolytics.tracker.api.net.ApiTracker

class DefaultSpinnerListener(val spinner: Spinner, val name: String) : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        spinner.selectedItem.toString()
        ApiTracker.getInstance().addToList(
            Event(
                mapOf(
                    "name" to name,
                    "value" to spinner.selectedItem.toString()
                ), "SPINNER_PICKED"
            )
        )
    }

}

