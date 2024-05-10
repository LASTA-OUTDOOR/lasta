package com.lastaoutdoor.lasta.data.db

import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.user.Converters
import com.lastaoutdoor.lasta.models.user.UserModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {

  private val converters = Converters()

  @Test
  fun testFromClimbingStyle() {
    val climbingStyle = ClimbingStyle.OUTDOOR
    val result = converters.fromClimbingStyle(climbingStyle)
    assertEquals("Outdoor", result)
  }

  @Test
  fun testToClimbingStyle() {
    val result = converters.toClimbingSyle("Outdoor")
    assertEquals(ClimbingStyle.OUTDOOR, result)
  }
  @Test
  fun testToClimbingStyle2() {
    val result = converters.toClimbingSyle("Indoor")
    assertEquals(ClimbingStyle.INDOOR, result)
  }

  @Test
  fun testFromDifficulty() {
    val difficulty = Difficulty.EASY
    val result = converters.fromDifficulty(difficulty)
    assertEquals("Easy", result)
  }

  @Test
  fun testToDifficulty() {
    val result = converters.toDifficulty("Easy")
    assertEquals(Difficulty.EASY, result)
  }
  @Test
  fun testToDifficulty2() {
    val result = converters.toDifficulty("Normal")
    assertEquals(Difficulty.NORMAL, result)
  }
  @Test
  fun testToDifficulty3() {
    val result = converters.toDifficulty("Hard")
    assertEquals(Difficulty.HARD, result)
  }

  @Test
  fun testFromActivityType() {
    val activityType = ActivityType.BIKING
    val result = converters.fromActivityType(activityType)
    assertEquals("BIKING", result)
  }

  @Test
  fun testToActivity() {
    val result = converters.toActivity("BIKING")
    assertEquals(ActivityType.BIKING, result)
  }
  @Test
  fun testToActivity2() {
    val result = converters.toActivity("HIKING")
    assertEquals(ActivityType.HIKING, result)
  }
  @Test
  fun testToActivity3() {
    val result = converters.toActivity("CLIMBING")
    assertEquals(ActivityType.CLIMBING, result)
  }


  @Test
  fun testFromListR() {
    val ratings = listOf(Rating("", "", ""), Rating("", "", ""))
    val result = converters.fromListR(ratings)
    val expected =
        "[{\"userId\":\"\",\"comment\":\"\",\"rating\":\"\"},{\"userId\":\"\",\"comment\":\"\",\"rating\":\"\"}]"
    assertEquals(expected, result)
  }

  @Test
  fun testToListS() {
    val input = "[one, two, three]"
    val result = converters.toListS(input)
    assertEquals(3, result.size)
    assertEquals("one", result[0])
    assertEquals("two", result[1])
    assertEquals("three", result[2])
  }

  @Test
  fun testFromListS() {
    val input = listOf("one", "two", "three")
    val result = converters.fromListS(input)
    val expected = "one, two, three"
    assertEquals(expected, result)
  }

  @Test
  fun testToUserModel() {
    val expected =
        "{\"userId\":\"Alice\",\"userName\":\"\",\"email\":\"\",\"profilePictureUrl\":\"\",\"description\":\"\",\"language\":\"ENGLISH\",\"prefActivity\":\"CLIMBING\",\"levels\":{\"climbingLevel\":\"BEGINNER\",\"hikingLevel\":\"BEGINNER\",\"bikingLevel\":\"BEGINNER\"},\"friends\":[],\"friendRequests\":[],\"favorites\":[]}"
    val result = converters.toUserModel(expected)
    assertEquals(result.userId, "Alice")
  }

  @Test
  fun testFromModel() {
    val userModel = UserModel("Alice")
    val result = converters.fromModel(userModel)
    val expected =
        "{\"userId\":\"Alice\",\"userName\":\"\",\"email\":\"\",\"profilePictureUrl\":\"\",\"description\":\"\",\"language\":\"ENGLISH\",\"prefActivity\":\"CLIMBING\",\"levels\":{\"climbingLevel\":\"BEGINNER\",\"hikingLevel\":\"BEGINNER\",\"bikingLevel\":\"BEGINNER\"},\"friends\":[],\"friendRequests\":[],\"favorites\":[]}"
    assertEquals(expected, result)
  }
}
