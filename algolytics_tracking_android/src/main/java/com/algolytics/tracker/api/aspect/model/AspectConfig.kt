package com.algolytics.tracker.api.aspect.model

data class AspectConfig(
    var clicks: Boolean = false,
    var inputs: Boolean = false,
    var activities: Boolean = false
) {

    fun enableClicks() = apply { this.clicks = true }
    fun enableInputs() = apply { this.inputs = true }
    fun enableActivities() = apply { this.activities = true }
}