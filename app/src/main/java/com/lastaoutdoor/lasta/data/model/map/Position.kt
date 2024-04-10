package com.lastaoutdoor.lasta.data.model.map

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Position(
    @SerializedName("lat") @Expose val lat: Double,
    @SerializedName("lon") @Expose val lon: Double
)
