package com.algolytics.tracker.api.net

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface TrackerEndpoint {

    @POST("/api/scenario/code/remote/score")
    fun sendEvent(
        @Query("name") scenarioCodeName: String,
        @Query("key") apiKey: String,
        @Body event: Any
    ): Call<Any>
}