package com.lastaoutdoor.lasta.repository.db

import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.useractivities.ActivitiesDatabaseType

/** This interface provides methods for interacting with user activities data. */
interface UserActivitiesDBRepository {

  /**
   * Adds an activity to a user's activities.
   *
   * @param user The FirebaseUser to whom the activity is to be added.
   * @param activity The activity to add to the user's activities.
   */
  fun addActivityToUserActivities(user: FirebaseUser, activity: ActivitiesDatabaseType)

  /**
   * Fetches the activities of a user.
   *
   * @param user The UserModel whose activities are to be fetched.
   * @param activityType The type of activities to fetch.
   * @return A list of activities of the specified type.
   */
  suspend fun getUserActivities(
      user: UserModel,
      activityType: ActivitiesDatabaseType.Sports
  ): List<ActivitiesDatabaseType>
}
