package com.lastaoutdoor.lasta.models.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.BikingActivity
import com.lastaoutdoor.lasta.models.activity.ClimbingActivity
import com.lastaoutdoor.lasta.models.activity.HikingActivity

data class Tags(
    @SerializedName("name") val name: String,
    @SerializedName("sport") val sport: String = "",
    @SerializedName("route") val route: String = "",
    @SerializedName("from") val from: String = "",
    @SerializedName("to") val to: String = "",
    @SerializedName("distance") val distance: String = ""
) {
  override fun toString(): String {
    // TODO update
    return "name: $name, sport: $sport"
  }
}

data class Position(
    @SerializedName("lat") @Expose val lat: Double,
    @SerializedName("lon") @Expose val lon: Double
)

abstract class OSMData {
  abstract fun getPosition(): Position

  abstract fun getActivityFromData(): Activity
}

class NodeWay(
    @SerializedName("type") @Expose val type: String,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("lat") @Expose val lat: Double?,
    @SerializedName("lon") @Expose val lon: Double?,
    @SerializedName("center") @Expose val center: Position?,
    @SerializedName("tags") @Expose val tags: Tags,
) : OSMData() {
  override fun getPosition(): Position {
    return if (type == "node" && lat != null && lon != null) Position(lat, lon)
    else if (type == "way" && center != null) center
    else Position(0.0, 0.0)
  }

  override fun getActivityFromData(): Activity {
    return ClimbingActivity(
        activityId = "", osmId = id, name = tags.name, startPosition = getPosition())
  }
}

// Used to store position within OSM Relations (i.e. list of Ways) without having to store all of
// the informations of every Way
class SimpleWay(@SerializedName("geometry") @Expose val nodes: List<Position>?) {
  override fun toString(): String {
    return "Simple Way : $nodes\n"
  }
}

class Relation(
    @SerializedName("type") @Expose val type: String,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("tags") @Expose val tags: Tags,
    @SerializedName("members") @Expose val ways: List<SimpleWay>?,
) : OSMData() {
  override fun getPosition(): Position {
    if (ways != null && ways[0].nodes != null) return ways[0].nodes!![0]
    return Position(0.0, 0.0)
  }

  override fun getActivityFromData(): Activity {
    return if (tags.route == "hiking") {
      HikingActivity(
          activityId = "",
          osmId = id,
          name = tags.name,
          startPosition = getPosition(),
          from = tags.from,
          to = tags.to)
    } else {
      BikingActivity(
          activityId = "",
          osmId = id,
          name = tags.name,
          startPosition = getPosition(),
          from = tags.from,
          to = tags.to,
          distance = tags.distance.toFloat())
    }
  }
}
