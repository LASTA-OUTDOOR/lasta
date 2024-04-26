package com.lastaoutdoor.lasta.utils

/**
 * A generic class that is used to wrap around data that is received from the network or local
 * database.
 *
 * @param T the type of the data that is being wrapped
 */
sealed class Response<out T> {

  /** Represents a response which is currently not available yet and/or is still loading. */
  object Loading : Response<Nothing>()

  /**
   * Represents a successful response with the data that was requested.
   *
   * @param data the data that was requested
   */
  data class Success<out T>(val data: T?) : Response<T>()

  /**
   * Represents a failed response with an error that occurred.
   *
   * @param e [Throwable] the error that occurred
   */
  data class Failure(val e: Throwable) : Response<Nothing>()
}
