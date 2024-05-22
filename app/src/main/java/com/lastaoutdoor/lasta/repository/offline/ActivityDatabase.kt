package com.lastaoutdoor.lasta.repository.offline

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.Converters
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserPreferences

@Database(entities = [Activity::class, UserPreferences::class, ClimbingUserActivity::class,HikingUserActivity::class, BikingUserActivity::class ], version = 1)
@TypeConverters(Converters::class)
abstract class ActivityDatabase : RoomDatabase() {
  abstract val activityDao: ActivityDao
}
