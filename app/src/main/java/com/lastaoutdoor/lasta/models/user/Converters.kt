package com.lastaoutdoor.lasta.models.user

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import java.util.Date

/**
 * Class containing type converters for Room database. These converters help Room database to
 * understand how to convert complex data types to and from primitive types.
 */
class Converters {

  /**
   * Converts a [ClimbingStyle] object to its String representation.
   *
   * @param c The [ClimbingStyle] object to convert.
   * @return The String representation of the [ClimbingStyle] object.
   */
  @TypeConverter
  fun fromClimbingStyle(c: ClimbingStyle): String {
    return c.toString()
  }

  /**
   * Converts a String to a [ClimbingStyle] object.
   *
   * @param s The String to convert.
   * @return The [ClimbingStyle] object represented by the String.
   */
  @TypeConverter
  fun toClimbingSyle(s: String): ClimbingStyle {
    return if (s == "Outdoor") {
      ClimbingStyle.OUTDOOR
    } else {
      ClimbingStyle.INDOOR
    }
  }

  /**
   * Converts a [Difficulty] object to its String representation.
   *
   * @param d The [Difficulty] object to convert.
   * @return The String representation of the [Difficulty] object.
   */
  @TypeConverter
  fun fromDifficulty(d: Difficulty): String {
    return d.toString()
  }

  /**
   * Converts a String to a [Difficulty] object.
   *
   * @param s The String to convert.
   * @return The [Difficulty] object represented by the String.
   */
  @TypeConverter
  fun toDifficulty(s: String): Difficulty {
    return when (s) {
      "Easy" -> Difficulty.EASY
      "Normal" -> Difficulty.NORMAL
      else -> Difficulty.HARD
    }
  }

  /**
   * Converts an [ActivityType] object to its String representation.
   *
   * @param a The [ActivityType] object to convert.
   * @return The String representation of the [ActivityType] object.
   */
  @TypeConverter
  fun fromActivityType(a: ActivityType): String {
    return a.name
  }

  /**
   * Converts a String to an [ActivityType] object.
   *
   * @param s The String to convert.
   * @return The [ActivityType] object represented by the String.
   */
  @TypeConverter
  fun toActivity(s: String): ActivityType {
    return when (s) {
      "BIKING" -> ActivityType.BIKING
      "HIKING" -> ActivityType.HIKING
      else -> ActivityType.CLIMBING
    }
  }

  /**
   * Converts a JSON String to a List of [Rating] objects.
   *
   * @param s The JSON String to convert.
   * @return The List of [Rating] objects represented by the JSON String.
   */
  @TypeConverter
  fun toListR(s: String): List<Rating> {
    val gson = Gson()
    return gson.fromJson(s, object : TypeToken<List<Rating>>() {}.type)
  }

  /**
   * Converts a List of [Rating] objects to a JSON String.
   *
   * @param l The List of [Rating] objects to convert.
   * @return The JSON String representation of the List of [Rating] objects.
   */
  @TypeConverter
  fun fromListR(l: List<Rating>): String {
    val gson = Gson()
    return gson.toJson(l)
  }

  /**
   * Converts a JSON String to a List of Strings.
   *
   * @param s The JSON String to convert.
   * @return The List of Strings represented by the JSON String.
   */
  @TypeConverter
  fun toListS(s: String): List<String> {
    val a = s.replace("[", "")
    val b = a.replace("]", "")
    return b.split(",").map { it.trim() }
  }

  /**
   * Converts a List of Strings to a JSON String.
   *
   * @param l The List of Strings to convert.
   * @return The JSON String representation of the List of Strings.
   */
  @TypeConverter
  fun fromListS(l: List<String>): String {
    return l.joinToString()
  }

  /**
   * Converts a JSON String to a [UserModel] object.
   *
   * @param s The JSON String to convert.
   * @return The [UserModel] object represented by the JSON String.
   */
  @TypeConverter
  fun toUserModel(s: String): UserModel {
    val gson = Gson()
    return gson.fromJson(s, object : TypeToken<UserModel>() {}.type)
  }

  /**
   * Converts a [UserModel] object to a JSON String.
   *
   * @param u The [UserModel] object to convert.
   * @return The JSON String representation of the [UserModel] object.
   */
  @TypeConverter
  fun fromModel(u: UserModel): String {
    val gson = Gson()
    return gson.toJson(u)
  }

  /**
   * Converts a [Date] object to a JSON String.
   *
   * @param d The [Date] object to convert.
   * @return The JSON String representation of the [Date] object.
   */
  @TypeConverter
  fun fromDate(d: Date): String {
    return Gson().toJson(d)
  }

  /**
   * Converts a JSON String to a [Date] object.
   *
   * @param s The JSON String to convert.
   * @return The [Date] object represented by the JSON String.
   */
  @TypeConverter
  fun toDate(s: String): Date {
    return Gson().fromJson(s, object : TypeToken<Date>() {}.type)
  }
}
