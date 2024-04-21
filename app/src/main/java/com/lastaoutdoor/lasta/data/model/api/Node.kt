package com.lastaoutdoor.lasta.data.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.data.model.activity.Activity
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.Difficulty

// represents OSM node
open class Node(
    @SerializedName("type") @Expose val type: String?,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("lat") @Expose val lat: Double,
    @SerializedName("lon") @Expose val lon: Double,
    @SerializedName("tags") @Expose val tags: Tags,
    activityType: ActivityType,
    difficulty: Difficulty,
    length: Float,
    duration: String?,
    locationName: String?
) : Activity(activityType, difficulty, length, duration, "") {
  override fun toString(): String {
    return (" type: $type id: $id lat: $lat lon: $lon activityType: $activityType name: ${tags.name}\n")
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
