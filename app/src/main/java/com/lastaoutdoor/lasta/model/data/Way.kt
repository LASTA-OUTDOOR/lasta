package com.lastaoutdoor.lasta.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Way(
    @SerializedName("type") @Expose val type: String,
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("tags") @Expose val tags: Tags,
    @SerializedName("geometry") @Expose val nodes: List<Position>,
    activityType: ActivityType
) : OutdoorActivity(activityType) {

    override fun toString(): String {
        return (" type: $type id: $id  activityType: ${getActivityType()} name: ${tags.name} nodes: $nodes\n")
    }
}
//Used to store position within OSM Relations (i.e. list of Ways) without having to store all of the informations of every Way
class SimpleWay(
    @SerializedName("geometry") @Expose val nodes: List<Position>
){
    override fun toString(): String {
        return "Simple Way : $nodes\n"
    }
}