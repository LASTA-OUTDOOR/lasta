package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class ActivitiesDBRepositoryImpl
@Inject
constructor(context: Context, database: FirebaseFirestore) : ActivitiesDBRepository {
  private val activitiesCollection =
      database.collection(context.getString(R.string.activities_db_name))

  override suspend fun addActivity(activity: Activity): Activity {
    val activityData =
        hashMapOf(
            "osmId" to activity.osmId,
            "activityType" to activity.activityType.name,
            "name" to activity.name,
            "startPosition" to
                hashMapOf("lat" to activity.startPosition.lat, "lon" to activity.startPosition.lon),
            "rating" to activity.rating,
            "numRatings" to activity.numRatings,
            "ratings" to
                activity.ratings.map {
                  hashMapOf("userId" to it.userId, "comment" to it.comment, "rating" to it.rating)
                },
            "difficulty" to activity.difficulty.name,
            "activityImageUrl" to activity.activityImageUrl,
            "climbingStyle" to activity.climbingStyle.name,
            "elevationTotal" to activity.elevationTotal,
            "from" to activity.from,
            "to" to activity.to,
            "distance" to activity.distance)

    val documentRef = activitiesCollection.add(activityData).await()
    val activityId = documentRef.id
    return activity.copy(activityId)
  }

  override suspend fun getActivityById(activityId: String): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun getActivityByOSMId(osmId: Long): Activity {
    TODO("Not yet implemented")
  }

  override suspend fun getActivitiesByOSMIds(osmIds: List<Long>): List<Activity> {
    val activities = ArrayList<Activity>()
    val query = activitiesCollection.whereIn("osmId", osmIds).get().await()
    if (!query.isEmpty) {
      val documents = query.documents
      for (document in documents) {
        val id = document.id
        val osmId = document.getLong("osmId")!!
        val activityType = document.getString("activityType")!!
        val name = document.getString("name")!!
        val startPositionMap = document.get("startPosition") as Map<String, Double>
        val startPosition =
            Position(
                startPositionMap.getOrDefault("lat", 0.0),
                startPositionMap.getOrDefault("lon", 0.0))

        val rating = document.getDouble("rating")!!.toFloat()
        val numRatings = document.getLong("numRatings")!!.toInt()
        val ratingsMap = document.get("ratings") as List<HashMap<String, Any>>
        val ratings =
            ratingsMap.map {
              Rating(it["userId"] as String, it["comment"] as String, it["rating"] as Int)
            }
        val difficulty = Difficulty.valueOf(document.getString("difficulty")!!)
        val activityImageUrl = document.getString("activityImageUrl")!!
        val climbingStyle = ClimbingStyle.valueOf(document.getString("climbingStyle")!!)
        val elevationTotal = document.getDouble("elevationTotal")!!.toFloat()
        val from = document.getString("from")!!
        val to = document.getString("to")!!
        val distance = document.getDouble("distance")!!.toFloat()
        activities.add(
            Activity(
                id,
                osmId,
                ActivityType.valueOf(activityType),
                name,
                startPosition,
                rating,
                numRatings,
                ratings,
                difficulty,
                activityImageUrl,
                climbingStyle,
                elevationTotal,
                from,
                to,
                distance))
      }
    }
    return activities
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
