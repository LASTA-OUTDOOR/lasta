package com.lastaoutdoor.lasta.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Relation(
    @SerializedName("type") @Expose val type: String,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("tags") @Expose val tags: Tags,
    @SerializedName("members") @Expose val ways: List<SimpleWay>,
    @SerializedName("bounds") @Expose val bounds: Bounds,
    activityType: ActivityType,
    difficulty: Int,
    length: Float,
    duration: String,
    locationName: String
) : OutdoorActivity(activityType, difficulty, length, duration, locationName) {

  override fun toString(): String {
    return (" type: $type id: $id  activityType: ${getActivityType()} name: ${tags.name} bounds: $bounds\n")
  }
}

data class Bounds(
    @SerializedName("minlat") @Expose val minlat: Float,
    @SerializedName("minlon") @Expose val minlon: Float,
    @SerializedName("maxlat") @Expose val maxlat: Float,
    @SerializedName("maxlon") @Expose val maxlon: Float
)
