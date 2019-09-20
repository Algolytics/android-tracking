package com.algolytics.tracker.api.data.listener.model.connectivity

data class ConnectionInfo(
    val isConnected: Boolean,
    val isConnectedWifi: Boolean,
    val isConnectedMobile: Boolean
)