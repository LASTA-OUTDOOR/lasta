package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.Executor

class ActivitiesDBRepositoryImplTest {

  class MockTask<T : Any> : Task<T>() {
    private var result: T? = null
    private var exception: Exception? = null

    fun setResult(result: T) {
      this.result = result
    }

    fun setException(exception: Exception) {
      this.exception = exception
    }

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
  }

  private lateinit var activitiesDB: ActivitiesDBRepositoryImpl
  private val database: FirebaseFirestore = mockk(relaxed = true)
  private val context: Context = mockk()
  private val collection: CollectionReference = mockk()
  private val task: MockTask<QuerySnapshot> = mockk()
  private val querySnapshot: QuerySnapshot = mockk()

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

  @Test
  fun `Adding activities returns false if already existing`() = run {
    val mockActivity = mockk<Activity>()
    every { mockActivity.osmId } returns 1
    coEvery { collection.whereEqualTo("osmId", mockActivity.osmId).get() } returns task
    every { task.await() } returns querySnapshot
    every { querySnapshot.isEmpty } returns false
    runBlocking {
      val result = activitiesDB.addActivityIfNonExisting(mockActivity)
      assert(!result)
    }
  }
}