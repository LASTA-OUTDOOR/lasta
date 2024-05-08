package com.lastaoutdoor.lasta.models

import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import org.junit.Test

class DataClassesTest {

  @Test
  fun climStyle() {
    val c2 = ClimbingStyle.INDOOR
    val c3 = ClimbingStyle.OUTDOOR
    assert(c3 != c2)
  }

  @Test
  fun diffToString() {
    assert(Difficulty.HARD.toString() == "Hard")
  }
}
