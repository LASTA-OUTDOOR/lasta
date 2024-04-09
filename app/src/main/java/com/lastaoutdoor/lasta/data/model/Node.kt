package com.lastaoutdoor.lasta.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// represents OSM node
open class Node(
    @SerializedName("type") @Expose val type: String,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("lat") @Expose val lat: Double,
    @SerializedName("lon") @Expose val lon: Double,
    @SerializedName("tags") @Expose val tags: Tags,
    activityType: ActivityType,
    difficulty: Int,
    length: Float,
    duration: String,
    locationName: String
) : OutdoorActivity(activityType, difficulty, length, duration, "") {
  override fun toString(): String {
    return (" type: $type id: $id lat: $lat lon: $lon activityType: ${getActivityType()} name: ${tags.name}\n")
  }
}

/*
class ClimbingNode(

    type : String,
    id: Long,
    lat : Float,
    lon: Float,
    nam
    //override val activityType: ActivityType = ActivityType.CLIMBING
    activityType: ActivityType


): Node(

    type,
    id, lat, lon, activityType
){
    override fun toString(): String {
        return (" type: $type id: $id lat: $lat lon: $lon activityType: $activityType\n")
    }
}

*/
