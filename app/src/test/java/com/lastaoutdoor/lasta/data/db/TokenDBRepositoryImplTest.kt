package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.data.MockTask
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class TokenDBRepositoryImplTest {

  private lateinit var tokenDB: TokenDBRepositoryImpl
  private val database: FirebaseFirestore = mockk(relaxed = true)
  private val context: Context = mockk()
  private val tokenCollection: CollectionReference = mockk(relaxed = true)
  private val documentReference: DocumentReference = mockk()
  private val documentSnapshot: DocumentSnapshot = mockk()
  private val getTask: MockTask<DocumentSnapshot> = mockk()
  private val updateTask: MockTask<Void> = mockk()

  @Before
  fun setUp() {
    every { context.getString(any()) } returns "resource"
    every { database.collection(any() as String) } returns tokenCollection
    every { tokenCollection.document(any()) } returns documentReference
    every { documentReference.get() } returns getTask

    every { getTask.isComplete } returns true
    every { getTask.isCanceled } returns false
    every { getTask.isSuccessful } returns true
    every { getTask.result } returns documentSnapshot
    every { getTask.exception } returns null
    coEvery { getTask.await() } returns documentSnapshot

    every { updateTask.isComplete } returns true
    every { updateTask.isCanceled } returns false
    every { updateTask.isSuccessful } returns true
    every { updateTask.result } returns mockk()
    every { updateTask.exception } returns null
    coEvery { updateTask.await() } returns mockk()

    tokenDB = TokenDBRepositoryImpl(context, database)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `uploadUserToken works fine`() {
    every { documentReference.set(any(), any()) } returns updateTask
    tokenDB.uploadUserToken("userId", "token")
    tokenDB.uploadUserToken("", "token")
    tokenDB.uploadUserToken("userId", "")
  }

  @Test
  fun `getUserTokenById works fine`() = runTest {
    every { documentSnapshot.exists() } returns true
    every { documentSnapshot.getString("token") } returns "token"
    assert(tokenDB.getUserTokenById("userId") == "token")
    every { documentSnapshot.exists() } returns false
    assert(tokenDB.getUserTokenById("userId") == null)
    assert(tokenDB.getUserTokenById("") == null)
  }
}
