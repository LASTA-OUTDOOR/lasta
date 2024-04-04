package com.lastaoutdoor.lasta.model.data

import com.google.gson.annotations.SerializedName

data class Tags(
    @SerializedName(value = "name", alternate = ["from"]) val name: String,
    @SerializedName("sport") val sport: String
) {
  override fun toString(): String {
    return "name: $name, sport: $sport"
  }
}
