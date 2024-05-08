package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
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

  override suspend fun addActivityIfNonExisting(activity: Activity): Boolean {
    val query = activitiesCollection.whereEqualTo("osmId", activity.osmId).get().await()
    if (!query.isEmpty) return false
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

    activitiesCollection.add(activityData).await()
    return true
  }

  override suspend fun getActivityById(activityId: String): Activity? {
    val document = activitiesCollection.document(activityId).get().await()
    if (document.exists()) {
      return convertDocumentToActivity(document)
    }
    return null
  }

  override suspend fun getActivitiesByIds(activityIds: List<String>): List<Activity> {
    val activities = ArrayList<Activity>()
    if (activityIds.last().isEmpty()) return activities
    val query = activitiesCollection.whereIn(FieldPath.documentId(), activityIds).get().await()
    if (!query.isEmpty) {
      val documents = query.documents
      for (document in documents) {
        activities.add(convertDocumentToActivity(document))
      }
    }
    return activities
  }

  override suspend fun getActivitiesByOSMIds(
      osmIds: List<Long>,
      onlyKnown: Boolean
  ): List<Activity> {
    val activities = ArrayList<Activity>()
    if (osmIds.isEmpty()) return activities
    val query = activitiesCollection.whereIn("osmId", osmIds).get().await()
    if (!query.isEmpty) {
      val documents = query.documents
      for (document in documents) {
        val startPositionMap = document.get("startPosition") as Map<String, Double>
        if (onlyKnown && startPositionMap["lat"] == 0.0 && startPositionMap["lon"] == 0.0) {
          continue
        }
        activities.add(convertDocumentToActivity(document))
      }
    }
    return activities
  }

  override suspend fun updateStartPosition(activityId: String, position: Position) {
    val document = activitiesCollection.document(activityId)
    document
        .update("startPosition", hashMapOf("lat" to position.lat, "lon" to position.lon))
        .await()
  }

  private fun convertDocumentToActivity(document: DocumentSnapshot): Activity {
    val startPositionMap: Map<String, Double> =
        (document.get("startPosition") ?: HashMap<String, Double>()) as Map<String, Double>
    val startPosition =
        Position(
            startPositionMap.getOrDefault("lat", 0.0), startPositionMap.getOrDefault("lon", 0.0))
    val id = document.id
    val osmId = document.getLong("osmId") ?: 0
    val activityType = ActivityType.valueOf(document.getString("activityType") ?: "CLIMBING")
    val name = document.getString("name") ?: ""

    val rating = (document.getDouble("rating") ?: 5).toFloat()
    val numRatings = (document.getLong("numRatings") ?: 1).toInt()
    val ratingsMap: List<Map<String, Any>> =
        (document.get("ratings") ?: emptyList<Map<String, Any>>()) as List<Map<String, Any>>
    val ratings =
        ratingsMap.map {
          Rating(it["userId"] as String, it["comment"] as String, it["rating"] as Int)
        }
    val difficulty = Difficulty.valueOf(document.getString("difficulty") ?: "EASY")
    val activityImageUrl = document.getString("activityImageUrl") ?: ""
    val climbingStyle = ClimbingStyle.valueOf(document.getString("climbingStyle") ?: "OUTDOOR")
    val elevationTotal = (document.getDouble("elevationTotal") ?: 0).toFloat()
    val from = document.getString("from") ?: ""
    val to = document.getString("to") ?: ""
    val distance = (document.getDouble("distance") ?: 0).toFloat()
    return Activity(
        id,
        osmId,
        activityType,
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
        distance)
  }

  fun addRatingToActivity(activityId: String, rating: Rating) {
    val document = activitiesCollection.document(activityId)
    document.update(
        "ratings",
        FieldValue.arrayUnion(
            hashMapOf("userId" to rating.userId, "comment" to rating.comment, "rating" to rating.rating)))
  }
}
