package com.algolytics.tracker.api.model

import java.util.*

data class Event<T>(
    private val value: T,
    val eventType: String,
    val time: Date = Date()

)