package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.models.api.RadarSuggestion
import com.lastaoutdoor.lasta.repository.api.RadarRepository
import com.lastaoutdoor.lasta.utils.Response

class FakeRadarRepository : RadarRepository {

  // Triggers different situations in the test
  private var shouldWork = true

  override suspend fun getSuggestions(query: String): Response<List<RadarSuggestion>> {
    return if (shouldWork) {
      Response.Success(
          listOf(
              RadarSuggestion("Suggestion 1", 1.0, 1.0),
              RadarSuggestion("Suggestion 2", 2.0, 2.0),
              RadarSuggestion("Suggestion 3", 3.0, 3.0),
              RadarSuggestion("Suggestion 4", 4.0, 4.0),
          ))
    } else {
      Response.Failure(Error("Failed to get suggestions"))
    }
  }

  fun shouldWork(shouldWork: Boolean) {
    this.shouldWork = shouldWork
  }
}
