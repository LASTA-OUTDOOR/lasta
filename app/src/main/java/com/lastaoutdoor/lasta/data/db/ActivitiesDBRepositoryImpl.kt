package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivitiesDBRepositoryImpl @Inject constructor(context: Context, database: FirebaseFirestore): ActivitiesDBRepository {
  private val activitiesCollection = database.collection(context.getString(R.string.activities_db_name))
  override suspend fun addActivity(activity: Activity): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun getActivityById(activityId: String): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun getActivityByOSMId(osmId: Long): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun getActivitiesByOSMIds(osmIds: List<Long>): List<Activity> {
    TODO("Not yet implemented")
  }

  override suspend fun updateActivity(activity: Activity): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun addRating(activity: Activity, rating: Rating): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun deleteRating(activity: Activity, userId: String): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun updateDifficulty(activity: Activity, difficulty: Difficulty): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun updateImageUrl(activity: Activity, imageUrl: String): Activity {
    TODO("Not yet implemented")
  }
}