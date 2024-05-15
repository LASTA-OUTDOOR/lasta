package com.lastaoutdoor.lasta.repository.offline

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.Converters
import com.lastaoutdoor.lasta.models.user.UserPreferences

@Database(entities = [Activity::class, UserPreferences::class], version = 1)
@TypeConverters(Converters::class)
abstract class ActivityDatabase : RoomDatabase() {
  abstract val activityDao: ActivityDao
}
