package com.lastaoutdoor.lasta.utils

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

enum class ErrorType {
    ERROR_SIGN_IN,
    ERROR_SIGN_OUT,
    ERROR_UPDATE_USER,

}


class ErrorToast(private val context: Context) {

    private val DEFAULT_DURATION = Toast.LENGTH_SHORT

    fun showToast(errorType: ErrorType, duration: Int = DEFAULT_DURATION) {
        Toast.makeText(context, getErrorMessage(errorType), duration).show()
    }

    private fun getErrorMessage(errorType: ErrorType): String {
        /*TODO*/
        return "Error"
    }
}



