package com.lastaoutdoor.lasta.models.activity

import com.lastaoutdoor.lasta.models.api.Position
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ActivityTest {
  @Test
  fun climbingActivityTest() {
    val climbingActivity =
        Activity(
            "a",
            1L,
            ActivityType.CLIMBING,
            "b",
            Position(1.0, 2.0),
            3F,
            4,
            listOf(Rating("5")),
            Difficulty.NORMAL,
            "c",
            ClimbingStyle.INDOOR,
            6f)
    assertEquals("a", climbingActivity.activityId)
    assertEquals(1L, climbingActivity.osmId)
    assertEquals(Position(1.0, 2.0), climbingActivity.startPosition)
    assertEquals(3F, climbingActivity.rating)
    assertEquals(4, climbingActivity.numRatings)
    assertEquals(listOf(Rating("5")), climbingActivity.ratings)
    assertEquals(Difficulty.NORMAL, climbingActivity.difficulty)
    assertEquals("c", climbingActivity.activityImageUrl)
    assertEquals(ClimbingStyle.INDOOR, climbingActivity.climbingStyle)
    assertEquals(6f, climbingActivity.elevationTotal)
  }

  @Test
  fun activitiesAreCopied() {
    val climbingActivity =
        Activity(
            "a",
            1L,
            ActivityType.CLIMBING,
            "b",
            Position(1.0, 2.0),
            3F,
            4,
            listOf(Rating("5")),
            Difficulty.NORMAL,
            "c",
            ClimbingStyle.INDOOR,
            6f)
    val copiedActivity = climbingActivity.copy("d")
    assertEquals("d", copiedActivity.activityId)
    assertEquals(1L, copiedActivity.osmId)
    assertEquals(Position(1.0, 2.0), copiedActivity.startPosition)
    assertEquals(3F, copiedActivity.rating)
    assertEquals(4, copiedActivity.numRatings)
    assertEquals(listOf(Rating("5")), copiedActivity.ratings)
    assertEquals(Difficulty.NORMAL, copiedActivity.difficulty)
    assertEquals("c", copiedActivity.activityImageUrl)
    assertEquals(ClimbingStyle.INDOOR, copiedActivity.climbingStyle)
    assertEquals(6f, copiedActivity.elevationTotal)
  }

  @Test
  fun hikingActivityTest() {
    val hikingActivity =
        Activity(
            "a",
            1L,
            ActivityType.HIKING,
            "b",
            Position(1.0, 2.0),
            3F,
            4,
            listOf(Rating("5")),
            Difficulty.NORMAL,
            "c",
            from = "from",
            to = "to",
            distance = 8f)
    assertEquals("a", hikingActivity.activityId)
    assertEquals(1L, hikingActivity.osmId)
    assertEquals(Position(1.0, 2.0), hikingActivity.startPosition)
    assertEquals(3F, hikingActivity.rating)
    assertEquals(4, hikingActivity.numRatings)
    assertEquals(listOf(Rating("5")), hikingActivity.ratings)
    assertEquals(Difficulty.NORMAL, hikingActivity.difficulty)
    assertEquals("c", hikingActivity.activityImageUrl)
    assertEquals("from", hikingActivity.from)
    assertEquals("to", hikingActivity.to)
    assertEquals(8f, hikingActivity.distance)
  }

  @Test
  fun bikingActivityTest() {
    val bikingActivity =
        Activity(
            "a",
            1L,
            ActivityType.BIKING,
            "b",
            Position(1.0, 2.0),
            3F,
            4,
            listOf(Rating("5")),
            Difficulty.NORMAL,
            "c",
            from = "from",
            to = "to",
            distance = 8f)
    assertEquals("a", bikingActivity.activityId)
    assertEquals(1L, bikingActivity.osmId)
    assertEquals(Position(1.0, 2.0), bikingActivity.startPosition)
    assertEquals(3F, bikingActivity.rating)
    assertEquals(4, bikingActivity.numRatings)
    assertEquals(listOf(Rating("5")), bikingActivity.ratings)
    assertEquals(Difficulty.NORMAL, bikingActivity.difficulty)
    assertEquals("c", bikingActivity.activityImageUrl)
    assertEquals("from", bikingActivity.from)
    assertEquals("to", bikingActivity.to)
    assertEquals(8f, bikingActivity.distance)
  }
}
