package com.lastaoutdoor.lasta.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Position(
    @SerializedName("lat") @Expose val lat: Float,
    @SerializedName("lon") @Expose val lon: Float
)
