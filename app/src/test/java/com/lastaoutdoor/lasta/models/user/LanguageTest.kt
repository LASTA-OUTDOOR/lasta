package com.lastaoutdoor.lasta.models.user

import org.junit.Assert.assertEquals
import org.junit.Test

class LanguageTest {

  @Test
  fun language_toString() {
    assertEquals("German", Language.GERMAN.toString())
    assertEquals("English", Language.ENGLISH.toString())
    assertEquals("French", Language.FRENCH.toString())
  }

  @Test
  fun language_getFrenchName() {
    assertEquals("Allemand", Language.GERMAN.getFrenchName())
    assertEquals("Anglais", Language.ENGLISH.getFrenchName())
    assertEquals("Français", Language.FRENCH.getFrenchName())
  }

  @Test
  fun language_getGermanName() {
    assertEquals("Deutsch", Language.GERMAN.getGermanName())
    assertEquals("Englisch", Language.ENGLISH.getGermanName())
    assertEquals("Französisch", Language.FRENCH.getGermanName())
  }
}
