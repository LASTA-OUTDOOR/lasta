package com.lastaoutdoor.lasta.data.api

import com.google.gson.annotations.SerializedName
import com.lastaoutdoor.lasta.data.model.api.OSMData

// class used by gson to convert api responses to KotlinClasses
data class APIResponse<T : OSMData>(@SerializedName("elements") val elements: List<T>)
