package com.lastaoutdoor.lasta.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.repository.offline.ActivityDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ActivityDatabaseTest {
  val con: Context = ApplicationProvider.getApplicationContext()
  val db = Room.databaseBuilder(con, ActivityDatabase::class.java, "test").build()
  val dao = db.activityDao
  val act: Activity = Activity("id", osmId = 10L, name = "name")
  val act2: Activity = Activity("id2", osmId = 10L, name = "name")

  val user = UserModel("moi")
  val pref = UserPreferences(true, user, listOf("id", "id2"))

  @Test
  fun p() {
    runBlocking {
      dao.insertActivity(act)
      dao.insertActivity(act2)

      val l = dao.getAllActivities()

      db.close()

      assertEquals(l, listOf(act, act2))
    }
  }
}
