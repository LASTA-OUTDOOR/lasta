package com.lastaoutdoor.lasta.models.activity

import junit.framework.TestCase.assertEquals
import org.junit.Test

class RatingTest {
  @Test
  fun ratingAttributes() {
    val fakeRating = Rating("a", "b", "4")
    assertEquals("a", fakeRating.userId)
    assertEquals("b", fakeRating.comment)
    assertEquals("4", fakeRating.rating)
  }
}
