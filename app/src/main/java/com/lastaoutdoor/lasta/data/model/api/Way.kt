package com.lastaoutdoor.lasta.data.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.data.model.map.Position

class Way(
    @SerializedName("type") @Expose val type: String,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("tags") @Expose val tags: Tags,
    @SerializedName("geometry") @Expose val nodes: List<Position>,
    activityType: ActivityType,
    difficulty: Int,
    length: Float,
    duration: String,
    locationName: String
) : OutdoorActivity(activityType, difficulty, length, duration, locationName) {

  override fun toString(): String {
    return (" type: $type id: $id  activityType: ${getActivityType()} name: ${tags.name} nodes: $nodes\n")
  }
}
// Used to store position within OSM Relations (i.e. list of Ways) without having to store all of
// the informations of every Way
class SimpleWay(@SerializedName("geometry") @Expose val nodes: List<Position>) {
  override fun toString(): String {
    return "Simple Way : $nodes\n"
  }
}