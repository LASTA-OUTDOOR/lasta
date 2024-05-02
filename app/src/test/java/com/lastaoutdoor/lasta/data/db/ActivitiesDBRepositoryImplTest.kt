package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.R
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.lang.Exception
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before

class ActivitiesDBRepositoryImplTest {

  /*class MockTask<T : Any> : Task<T>() {

    override fun addOnFailureListener(p0: OnFailureListener): Task<T> {
      return this
    }

    override fun addOnFailureListener(p0: android.app.Activity, p1: OnFailureListener): Task<T> {
      return this
    }

    override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<T> {
      return this
    }

    override fun getException(): Exception? {
      return exception
    }

    override fun getResult(): T? {
      return result
    }

    override fun <X : Throwable?> getResult(p0: Class<X>): T? {
      return result
    }

    override fun isCanceled(): Boolean {
      return false
    }

    override fun isComplete(): Boolean {
      return true
    }

    override fun isSuccessful(): Boolean {
      return exception == null
    }

    override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in T>): Task<T> {
      return this
    }

    override fun addOnSuccessListener(
        p0: android.app.Activity,
        p1: OnSuccessListener<in T>
    ): Task<T> {
      return this
    }

    override fun addOnSuccessListener(p0: OnSuccessListener<in T>): Task<T> {
      return this
    }

    fun await(): T? {
      return result
    }
  }*/

  private lateinit var activitiesDB: ActivitiesDBRepositoryImpl
  private val database: FirebaseFirestore = mockk(relaxed = true)
  private val context: Context = mockk()
  private val collection: CollectionReference = mockk(relaxed = true)

  @Before
  fun setUp() {
    every { context.getString(R.string.activities_db_name) } returns "activities"
    every { database.collection("activities") } returns collection
    activitiesDB = ActivitiesDBRepositoryImpl(context, database)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  private inline fun <reified T> mockTask(result: T?, exception: Exception? = null): Task<T> {
    val task: Task<T> = mockk(relaxed = true)
    every { task.isComplete } returns true
    every { task.exception } returns exception
    every { task.isCanceled } returns false
    val relaxedT: T = mockk(relaxed = true)
    every { task.result } returns result

    coEvery { task.await() } returns result!!

    return task
  }

  /*@Test
  fun `Adding activities returns false if already existing`() = runTest {
    val mockActivity = Activity("", 1)
    val query: Query = mockk()
    val querySnapshot: QuerySnapshot = mockk()
    val osmId = "osmId"
    val task = mockTask(querySnapshot)
    every { collection.whereEqualTo(osmId, 1) } returns query
    coEvery { task.await() } returns querySnapshot
    every { querySnapshot.isEmpty } returns false
    println(task.await().isEmpty)
    val result = activitiesDB.addActivityIfNonExisting(mockActivity)
    assert(!result)
  }*/
}
