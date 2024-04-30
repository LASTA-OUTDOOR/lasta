package com.lastaoutdoor.lasta.data.api.osm

import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.models.api.OSMData

/**
 * This is a generic class to parse the response from the OSM API. It contains a list of elements of
 * type [T]. It uses GSON annotations to parse the JSON response.
 */
data class APIResponse<T : OSMData>(@SerializedName("elements") val elements: List<T>)
