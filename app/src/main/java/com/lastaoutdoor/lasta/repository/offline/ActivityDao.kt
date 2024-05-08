package com.lastaoutdoor.lasta.repository.offline

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.OActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface ActivityDao {
    @Upsert
    suspend fun insertActivity( a: Activity)
    @Upsert
    suspend fun insertUserPref( u : UserPreferences)
    @Query("SELECT * FROM activity WHERE activityId = :id")
    suspend fun getActivity(id : String) : Activity
    @Query("SELECT * FROM userpreferences WHERE user = :u")
    suspend fun getUserPreferences(u : UserModel) : UserPreferences
    @Query("SELECT downloadedActivities FROM userpreferences WHERE user = :u")
    suspend fun getUserActivities(u : UserModel) : List<String>
}