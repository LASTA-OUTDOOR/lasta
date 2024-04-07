package com.lastaoutdoor.lasta.data.api
// WIP, should be used to handle asychronous API calls, not used yet
sealed class ApiResponse<T> {
  /** Represents a successful response. Contains data */
  data class Success<T>(val data: T) : ApiResponse<T>()

  /** Represents a failed response. Has information on the error as a message */
  data class Error<T>(val errorMessage: String) : ApiResponse<T>()

  /** Represents a loading response. It may have data but it's optional */
  data class Loading<T>(val data: T? = null) : ApiResponse<T>()
}
