package com.lastaoutdoor.lasta.repository.api

import com.lastaoutdoor.lasta.data.api.autocomplete.RadarApiResponse
import com.lastaoutdoor.lasta.models.api.RadarSuggestion
import com.lastaoutdoor.lasta.utils.Response

/*
 * This interface represents the repository for the radar API.
 * It contains the methods to interact with the API.
 */
interface RadarRepository {

    /**
     * This method fetches the suggestions from the Radar API.
     *
     * @param query The query to search for suggestions (the start of a location).
     * @return A [Response] object with the list of suggestions as [RadarSuggestion] objects.
     */
    suspend fun getSuggestions(query: String): Response<List<RadarSuggestion>>

}