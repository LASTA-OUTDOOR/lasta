package com.lastaoutdoor.lasta.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.OActivity
import com.lastaoutdoor.lasta.repository.offline.ActivityDatabase
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ActivityDatabaseTest {
    val con : Context = ApplicationProvider.getApplicationContext()
    val db = Room.databaseBuilder(con, ActivityDatabase::class.java, "test").build()
    val dao = db.activityDao
    val act : Activity = Activity("id" , osmId = 10L, name = "name")
    @Test
    fun p(){
        runBlocking {
            dao.insertActivity(act)
            val l = dao.getActivity("id")
            db.close()

            assertEquals(l, act)

        }
    }

}