package com.lastaoutdoor.lasta.data.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.data.model.activity.Activity
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.Difficulty

class Relation(
    @SerializedName("type") @Expose val type: String,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("tags") @Expose val tags: Tags,
    @SerializedName("members") @Expose val ways: List<SimpleWay>?,
    @SerializedName("bounds") @Expose val bounds: Bounds,
    activityType: ActivityType,
    difficulty: Difficulty,
    length: Float,
    duration: String?,
    locationName: String?
) : Activity(activityType, difficulty, length, duration ?: "not given", locationName ?: "unnamed") {

  override fun toString(): String {
    return (" type: $type id: $id  activityType: $activityType name: ${tags.name} bounds: $bounds\n")
  }
}

data class Bounds(
    @SerializedName("minlat") @Expose val minlat: Double,
    @SerializedName("minlon") @Expose val minlon: Double,
    @SerializedName("maxlat") @Expose val maxlat: Double,
    @SerializedName("maxlon") @Expose val maxlon: Double
)
