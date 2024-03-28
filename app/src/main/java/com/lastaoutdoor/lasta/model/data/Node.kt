package com.lastaoutdoor.lasta.model.data

import com.google.gson.annotations.SerializedName

data class Node/*<T : ActivityType>*/(
    @SerializedName("type")
    val type : String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("lat")
    val lat : Float,
    @SerializedName("lon")
    val lon: Float
){
    override fun toString(): String {
        return (" type: $type id: $id lat: $lat lon: $lon \n")
    }
}