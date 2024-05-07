package com.lastaoutdoor.lasta.models.api

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/*
 * This class represents a location suggestion from the radar API. (used in the discovery screen)
 */
class RadarSuggestion(
    @SerializedName("addressLabel") @Expose val label: String,
    @SerializedName("latitude") @Expose val latitude: Double,
    @SerializedName("longitude") @Expose val longitude: Double,
){

    //returns the label of the suggestion
    fun getSuggestion(): String {
        return label
    }

    //returns the position of the suggestion
    fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

}