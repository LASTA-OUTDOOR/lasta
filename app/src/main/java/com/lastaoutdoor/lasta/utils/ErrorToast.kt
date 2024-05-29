package com.lastaoutdoor.lasta.utils

import android.content.Context
import android.widget.Toast
import com.lastaoutdoor.lasta.R

enum class ErrorType {
  ERROR_UNEXPECTED,
  ERROR_SIGN_IN,
  ERROR_SIGN_OUT,
  ERROR_DATABASE,
  ERROR_DAO,
  ERROR_MESSAGING,
  ERROR_RADAR_API,
  ERROR_OSM_API,
  ERROR_WEATHER
}

class ErrorToast(private val context: Context) {

  private val DEFAULT_DURATION = Toast.LENGTH_SHORT

  fun showToast(errorType: ErrorType, duration: Int = DEFAULT_DURATION) {
    // Toast.makeText(context, getErrorMessage(errorType), duration).show()
  }

  private fun getErrorMessage(errorType: ErrorType): String {
    return when (errorType) {
      ErrorType.ERROR_UNEXPECTED -> context.getString(R.string.error_unexpected)
      ErrorType.ERROR_SIGN_IN -> context.getString(R.string.error_sign_in)
      ErrorType.ERROR_SIGN_OUT -> context.getString(R.string.error_sign_out)
      ErrorType.ERROR_DATABASE -> context.getString(R.string.error_database)
      ErrorType.ERROR_DAO -> context.getString(R.string.error_dao)
      ErrorType.ERROR_MESSAGING -> context.getString(R.string.error_messaging)
      ErrorType.ERROR_RADAR_API -> context.getString(R.string.error_radar_api)
      ErrorType.ERROR_OSM_API -> context.getString(R.string.error_osm_api)
      ErrorType.ERROR_WEATHER -> context.getString(R.string.error_weather)
    }
  }
}
