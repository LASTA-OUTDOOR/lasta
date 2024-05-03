package com.lastaoutdoor.lasta.models.activity

import junit.framework.TestCase.assertEquals
import org.junit.Test

class ClimbingStyleTest {
  @Test
  fun climbing_toString() {
    assertEquals("Outdoor", ClimbingStyle.OUTDOOR.toString())
    assertEquals("Indoor", ClimbingStyle.INDOOR.toString())
  }
}
