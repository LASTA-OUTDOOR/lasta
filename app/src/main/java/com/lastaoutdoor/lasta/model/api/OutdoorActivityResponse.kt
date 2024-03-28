
package com.lastaoutdoor.lasta.model.api

import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.model.data.Node


data class OutdoorActivityResponse(
    @SerializedName("version")
    val version : Float,
    @SerializedName("elements")
    val elements : List<Node>
)