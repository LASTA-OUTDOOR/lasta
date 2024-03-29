package com.lastaoutdoor.lasta.model.api

import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.model.data.OutdoorActivity

// class used by gson to convert api responses to KotlinClasses
data class OutdoorActivityResponse<T : OutdoorActivity>(
    @SerializedName("version") val version: Float,
    @SerializedName("elements") val elements: List<T>
)
