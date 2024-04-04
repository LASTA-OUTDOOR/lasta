package com.lastaoutdoor.lasta.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Position(
    @SerializedName("lat") @Expose val lat: Float,
    @SerializedName("lon") @Expose val lon: Float
)
