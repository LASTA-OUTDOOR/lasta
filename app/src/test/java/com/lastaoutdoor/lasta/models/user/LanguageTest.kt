package com.lastaoutdoor.lasta.models.user

import android.content.Context
import com.lastaoutdoor.lasta.R
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LanguageTest {
  private val context: Context = mockk()

  @Before
  fun setUp() {
    every { context.getString(R.string.english) } returns "English"
    every { context.getString(R.string.french) } returns "French"
    every { context.getString(R.string.german) } returns "German"
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun language_resourcesToString() {
    assertEquals("English", Language.ENGLISH.resourcesToString(context))
    assertEquals("French", Language.FRENCH.resourcesToString(context))
    assertEquals("German", Language.GERMAN.resourcesToString(context))
  }
}
