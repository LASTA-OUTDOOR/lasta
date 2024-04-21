package com.lastaoutdoor.lasta.data.api

import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.data.model.activity.Activity

// class used by gson to convert api responses to KotlinClasses
data class OutdoorActivityResponse<T : Activity>(
    @SerializedName("version") val version: Float,
    @SerializedName("elements") val elements: List<T>
)
