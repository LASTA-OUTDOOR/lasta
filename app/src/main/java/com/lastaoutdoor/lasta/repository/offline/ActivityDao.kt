package com.lastaoutdoor.lasta.repository.offline

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.lastaoutdoor.lasta.models.activity.Activity

@Dao
interface ActivityDao {
  @Upsert suspend fun insertActivity(a: Activity)

  @Query("SELECT * FROM activity WHERE activityId = :id")
  suspend fun getActivity(id: String): Activity

  @Query("SELECT * FROM activity") suspend fun getAllActivities(): List<Activity>

  @Delete suspend fun deleteActivity(a: Activity)
}
