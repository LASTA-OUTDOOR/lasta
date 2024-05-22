package com.lastaoutdoor.lasta.repository.offline

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserPreferences

@Dao
interface ActivityDao {
  @Upsert suspend fun insertActivity(a: Activity)

  @Query("SELECT * FROM activity WHERE activityId = :id")
  suspend fun getActivity(id: String): Activity
  @Upsert suspend fun insertClimbingActivity(a: ClimbingUserActivity)
  @Upsert suspend fun insertHikingActivity(a: HikingUserActivity)
  @Upsert suspend fun insertBikingActivity(a: BikingUserActivity)
  @Upsert suspend fun insertUserPreferences(u : UserPreferences)
  @Query("SELECT * from USERPREFERENCES") suspend fun
  @Query("SELECT * FROM activity") suspend fun getAllActivities(): List<Activity>
  @Query("SELECT * FROM ClimbingUserActivity") suspend fun getClimbingActivities():List<ClimbingUserActivity>
  @Query("SELECT * FROM HikingUserActivity") suspend fun getHikingActivities():List<HikingUserActivity>
  @Query("SELECT * FROM BikingUserActivity") suspend fun getBikingActivities():List<BikingUserActivity>

  @Delete suspend fun deleteActivity(a: Activity)

}
