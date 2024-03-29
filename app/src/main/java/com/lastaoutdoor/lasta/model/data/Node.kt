package com.lastaoutdoor.lasta.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// represents OSM node
open class Node(
    @SerializedName("type") @Expose val type: String,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("lat") @Expose val lat: Float,
    @SerializedName("lon") @Expose val lon: Float,
    open val activityType: ActivityType = ActivityType.NULL
) : OutdoorActivity() {
  override fun toString(): String {
    return (" type: $type id: $id lat: $lat lon: $lon activityType: $activityType\n")
  }
}

/*

class ClimbingNode(

    type : String,
    id: Long,
    lat : Float,
    lon: Float,
    override val activityType: ActivityType = ActivityType.CLIMBING

): Node(

    type,
    id, lat, lon, activityType
){
    override fun toString(): String {
        return (" type: $type id: $id lat: $lat lon: $lon activityType: $activityType\n")
    }
}
*/
