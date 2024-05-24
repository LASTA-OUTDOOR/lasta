package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.MockTask
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserDBRepositoryImplTest {

  private lateinit var userDB: UserDBRepositoryImpl
  private val database: FirebaseFirestore = mockk(relaxed = true)
  private val context: Context = mockk()
  private val userCollection: CollectionReference = mockk(relaxed = true)
  private val documentReference: DocumentReference = mockk()
  private val documentSnapshot: DocumentSnapshot = mockk()
  private val query: Query = mockk()
  private val querySnapshot: QuerySnapshot = mockk()
  private val getTask: MockTask<DocumentSnapshot> = mockk()
  private val queryTask: MockTask<QuerySnapshot> = mockk()
  private val updateTask: MockTask<Void> = mockk()

  @Before
  fun setUp() {
    every { context.getString(R.string.user_db_name) } returns "users"
    every { database.collection("users") } returns userCollection

    every { userCollection.document(any()) } returns documentReference
    every { documentReference.get() } returns getTask

    every { userCollection.whereEqualTo(any() as String, any()) } returns query
    every { query.get() } returns queryTask

    every { getTask.isComplete } returns true
    every { getTask.isCanceled } returns false
    every { getTask.isSuccessful } returns true
    every { getTask.result } returns documentSnapshot
    every { getTask.exception } returns null
    coEvery { getTask.await() } returns documentSnapshot

    every { queryTask.isComplete } returns true
    every { queryTask.isCanceled } returns false
    every { queryTask.isSuccessful } returns true
    every { queryTask.result } returns querySnapshot
    every { queryTask.exception } returns null
    coEvery { queryTask.await() } returns querySnapshot

    every { updateTask.isComplete } returns true
    every { updateTask.isCanceled } returns false
    every { updateTask.isSuccessful } returns true
    every { updateTask.result } returns mockk()
    every { updateTask.exception } returns null
    coEvery { updateTask.await() } returns mockk()

    userDB = UserDBRepositoryImpl(context, database)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `Update User works fine`() = runTest {
    val user = UserModel(userId = "userId")
    coEvery { documentReference.set(any(), any()) } returns mockk()
    userDB.updateUser(user)
    coVerify(exactly = 1) { documentReference.set(any(), any()) }
  }

  @Test
  fun `Get User By Id returns null if user is not found`() = runTest {
    every { documentSnapshot.exists() } returns false
    val result = userDB.getUserById("userId")
    assert(result == null)
  }

  @Test
  fun `Get User By Id returns user if found`() = runTest {
    every { documentSnapshot.exists() } returns true
    every { documentSnapshot.getString("userName") } returns "userName"
    every { documentSnapshot.getString("email") } returns "email"
    every { documentSnapshot.getString("profilePictureUrl") } returns "profilePictureUrl"
    every { documentSnapshot.getString("description") } returns "description"
    every { documentSnapshot.getString("language") } returns "ENGLISH"
    every { documentSnapshot.getString("prefActivity") } returns "CLIMBING"
    every { documentSnapshot.get("levels") } returns
        hashMapOf(
            "climbingLevel" to "BEGINNER", "hikingLevel" to "BEGINNER", "bikingLevel" to "BEGINNER")
    every { documentSnapshot.get("friends") } returns emptyList<String>()
    every { documentSnapshot.get("friendRequests") } returns emptyList<String>()
    every { documentSnapshot.get("favorites") } returns emptyList<String>()
    val result = userDB.getUserById("userId") as UserModel
    assert(result.userId == "userId")
    assert(result.userName == "userName")
    assert(result.email == "email")
    assert(result.profilePictureUrl == "profilePictureUrl")
    assert(result.description == "description")
    assert(result.language == Language.ENGLISH)
    assert(result.prefActivity == ActivityType.CLIMBING)
    assert(result.levels.climbingLevel == UserLevel.BEGINNER)
    assert(result.levels.hikingLevel == UserLevel.BEGINNER)
    assert(result.levels.bikingLevel == UserLevel.BEGINNER)
    assert(result.friends.isEmpty())
    assert(result.friendRequests.isEmpty())
    assert(result.favorites.isEmpty())
  }

  @Test
  fun `Get User By Id returns user but every field is null`() = runTest {
    every { documentSnapshot.exists() } returns true
    every { documentSnapshot.getString(any()) } returns null
    every { documentSnapshot.get(any() as String) } returns null
    val result = userDB.getUserById("userId") as UserModel
    assert(result.userId == "userId")
    assert(result.userName == "")
    assert(result.email == "")
    assert(result.profilePictureUrl == "")
    assert(result.description == "")
    assert(result.language == Language.ENGLISH)
    assert(result.prefActivity == ActivityType.CLIMBING)
    assert(result.levels.climbingLevel == UserLevel.BEGINNER)
    assert(result.levels.hikingLevel == UserLevel.BEGINNER)
    assert(result.levels.bikingLevel == UserLevel.BEGINNER)
    assert(result.friends.isEmpty())
    assert(result.friendRequests.isEmpty())
    assert(result.favorites.isEmpty())
  }

  @Test
  fun `Get User By Email returns null if user is not found`() = runTest {
    every { querySnapshot.isEmpty } returns true
    val result = userDB.getUserByEmail("email")
    assert(result == null)
  }

  @Test
  fun `Get User By Email returns user if found`() = runTest {
    every { querySnapshot.isEmpty } returns false
    every { querySnapshot.documents } returns listOf(documentSnapshot)
    every { documentSnapshot.id } returns "userId"
    every { documentSnapshot.getString("userName") } returns "userName"
    every { documentSnapshot.getString("profilePictureUrl") } returns "profilePictureUrl"
    every { documentSnapshot.getString("description") } returns "description"
    every { documentSnapshot.getString("language") } returns "ENGLISH"
    every { documentSnapshot.getString("prefActivity") } returns "CLIMBING"
    every { documentSnapshot.get("levels") } returns
        hashMapOf(
            "climbingLevel" to "BEGINNER", "hikingLevel" to "BEGINNER", "bikingLevel" to "BEGINNER")
    every { documentSnapshot.get("friends") } returns emptyList<String>()
    every { documentSnapshot.get("friendRequests") } returns emptyList<String>()
    every { documentSnapshot.get("favorites") } returns emptyList<String>()
    val result = userDB.getUserByEmail("email") as UserModel
    assert(result.userId == "userId")
    assert(result.userName == "userName")
    assert(result.email == "email")
    assert(result.profilePictureUrl == "profilePictureUrl")
    assert(result.description == "description")
    assert(result.language == Language.ENGLISH)
    assert(result.prefActivity == ActivityType.CLIMBING)
    assert(result.levels.climbingLevel == UserLevel.BEGINNER)
    assert(result.levels.hikingLevel == UserLevel.BEGINNER)
    assert(result.levels.bikingLevel == UserLevel.BEGINNER)
    assert(result.friends.isEmpty())
    assert(result.friendRequests.isEmpty())
    assert(result.favorites.isEmpty())
  }

  @Test
  fun `Get User By Email returns user but every field is null`() = runTest {
    every { querySnapshot.isEmpty } returns false
    every { querySnapshot.documents } returns listOf(documentSnapshot)
    every { documentSnapshot.id } returns "userId"
    every { documentSnapshot.getString(any()) } returns null
    every { documentSnapshot.get(any() as String) } returns null
    val result = userDB.getUserByEmail("email") as UserModel
    assert(result.userId == "userId")
    assert(result.userName == "")
    assert(result.email == "email")
    assert(result.profilePictureUrl == "")
    assert(result.description == "")
    assert(result.language == Language.ENGLISH)
    assert(result.prefActivity == ActivityType.CLIMBING)
    assert(result.levels.climbingLevel == UserLevel.BEGINNER)
    assert(result.levels.hikingLevel == UserLevel.BEGINNER)
    assert(result.levels.bikingLevel == UserLevel.BEGINNER)
    assert(result.friends.isEmpty())
    assert(result.friendRequests.isEmpty())
    assert(result.favorites.isEmpty())
  }

  @Test
  fun `Update field works fine`() = runTest {
    coEvery { documentReference.update(any()) } returns updateTask
    userDB.updateField("userId", "field", "value")
    coVerify(exactly = 1) { documentReference.update(any()) }
  }

  @Test
  fun `Add Favorite works fine`() = runTest {
    coEvery { documentReference.update(any() as String, any()) } returns updateTask
    userDB.addFavorite("userId", "favorite")
    coVerify(exactly = 1) { documentReference.update(any() as String, any()) }
  }

  @Test
  fun `Remove Favorite works fine`() = runTest {
    coEvery { documentReference.update(any() as String, any()) } returns updateTask
    userDB.removeFavorite("userId", "favorite")
    coVerify(exactly = 1) { documentReference.update(any() as String, any()) }
  }

  @Test
  fun `deleteUser works fine`() = runTest {
    coEvery { documentReference.delete() } returns updateTask
    userDB.deleteUser("userId")
    coVerify(exactly = 1) { documentReference.delete() }
  }
}
