package com.lastaoutdoor.lasta.utils

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorToastTest {

  val errorToast = ErrorToast(mockk())

  @Test
  fun `getErrorMessage should return error message`() {

    val method = errorToast.javaClass.getDeclaredMethod("getErrorMessage", ErrorType::class.java)
    method.isAccessible = true

    every { method.invoke(errorToast, ErrorType.ERROR_WEATHER) } returns
        "A weather error has occured."
    assertEquals("A weather error has occured.", method.invoke(errorToast, ErrorType.ERROR_WEATHER))
    every { method.invoke(errorToast, ErrorType.ERROR_UNEXPECTED) } returns
        "An unexpected error has occured."
    assertEquals(
        "An unexpected error has occured.", method.invoke(errorToast, ErrorType.ERROR_UNEXPECTED))
    every { method.invoke(errorToast, ErrorType.ERROR_SIGN_IN) } returns
        "A Sign In error has occured."
    assertEquals("A Sign In error has occured.", method.invoke(errorToast, ErrorType.ERROR_SIGN_IN))
    every { method.invoke(errorToast, ErrorType.ERROR_SIGN_OUT) } returns
        "A Sign Out error has occured."
    assertEquals(
        "A Sign Out error has occured.", method.invoke(errorToast, ErrorType.ERROR_SIGN_OUT))
    every { method.invoke(errorToast, ErrorType.ERROR_DATABASE) } returns
        "A database error has occured."
    assertEquals(
        "A database error has occured.", method.invoke(errorToast, ErrorType.ERROR_DATABASE))
    every { method.invoke(errorToast, ErrorType.ERROR_DAO) } returns "A DAO error has occured."
    assertEquals("A DAO error has occured.", method.invoke(errorToast, ErrorType.ERROR_DAO))
    every { method.invoke(errorToast, ErrorType.ERROR_MESSAGING) } returns
        "A messaging error has occured."
    assertEquals(
        "A messaging error has occured.", method.invoke(errorToast, ErrorType.ERROR_MESSAGING))
    every { method.invoke(errorToast, ErrorType.ERROR_RADAR_API) } returns
        "A Radar API error has occured."
    assertEquals(
        "A Radar API error has occured.", method.invoke(errorToast, ErrorType.ERROR_RADAR_API))
    every { method.invoke(errorToast, ErrorType.ERROR_OSM_API) } returns
        "An OSM API error has occured."
    assertEquals(
        "An OSM API error has occured.", method.invoke(errorToast, ErrorType.ERROR_OSM_API))
  }
}
