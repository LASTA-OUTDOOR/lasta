package com.lastaoutdoor.lasta.models.user

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import java.util.Date

class Converters {
  @TypeConverter
  fun fromClimbingStyle(c: ClimbingStyle): String {
    return c.toString()
  }

  @TypeConverter
  fun toClimbingSyle(s: String): ClimbingStyle {
    return if (s == "Outdoor") {
      ClimbingStyle.OUTDOOR
    } else {
      ClimbingStyle.INDOOR
    }
  }

  @TypeConverter
  fun fromDifficulty(d: Difficulty): String {
    return d.toString()
  }

  @TypeConverter
  fun toDifficulty(s: String): Difficulty {
    return if (s == "Easy") {
      Difficulty.EASY
    } else if (s == "Normal") {
      Difficulty.NORMAL
    } else {
      Difficulty.HARD
    }
  }

  @TypeConverter
  fun fromActivityType(a: ActivityType): String {
    return a.name
  }

  @TypeConverter
  fun toActivity(s: String): ActivityType {
    return if (s.equals("BIKING")) {
      ActivityType.BIKING
    } else if (s.equals("HIKING")) {
      ActivityType.HIKING
    } else {
      ActivityType.CLIMBING
    }
  }

  @TypeConverter
  fun toListR(s: String): List<Rating> {
    val gson = Gson()

    return gson.fromJson(s, TypeToken.get(listOf(Rating("", "")).javaClass))
  }

  @TypeConverter
  fun fromListR(l: List<Rating>): String {
    val gson = Gson()
    return gson.toJson(l)
  }

  @TypeConverter
  fun toListS(s: String): List<String> {
    val a = s.replace("[", "")

    val b = a.replace("]", "")
    return b.split(",").map { it.trim() }
  }

  @TypeConverter
  fun fromListS(l: List<String>): String {
    return l.joinToString()
  }

  @TypeConverter
  fun toUserModel(s: String): UserModel {
    val gson = Gson()

    return gson.fromJson(s, TypeToken.get(UserModel::class.java))
  }

  @TypeConverter
  fun fromModel(u: UserModel): String {
    val gson = Gson()
    return gson.toJson(u)
  }

  @TypeConverter
  fun fromDate(d: Date): String {
    return Gson().toJson(d)
  }

  @TypeConverter
  fun toDate(s: String): Date {
    return Gson().fromJson(s, TypeToken.get(Date::class.java))
  }
}
