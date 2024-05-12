package com.lastaoutdoor.lasta.data.api.autocomplete

import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.models.api.RadarSuggestion

/*
 * This class represents the response from the Radar API. It contains a list of suggestions.
 */
data class RadarApiResponse(
    @SerializedName("addresses") val suggestions: List<RadarSuggestion>,
)
