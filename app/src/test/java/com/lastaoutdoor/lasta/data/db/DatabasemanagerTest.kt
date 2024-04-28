import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.db.UserDBRepositoryImpl
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(manifest = Config.NONE)
class DatabaseManagerTest {
  // private val userModel : UserModel = UserModel()
  private val firestore: FirebaseFirestore = mockk(relaxed = true)
  private val context: Context = mockk()
  private val user: FirebaseUser = mockk()
  private val documentReference: DocumentReference = mockk()
  private val documentSnapshot: DocumentSnapshot = mockk()
  private lateinit var databaseManager: UserDBRepositoryImpl
  private val task: UserActivitiesRepositoryImplTest.mockTask<DocumentSnapshot> = mockk()
  private val task2: UserActivitiesRepositoryImplTest.mockTask<Void> = mockk()

  @Before
  fun setup() {

    databaseManager = UserDBRepositoryImpl(firestore)
    every { context.getString(R.string.activities_database_name) } returns "activities"
    every { firestore.collection("users") } returns mockk()
    every { firestore.collection("users").document(any()) } returns documentReference
    every { user.uid } returns "userId"

    every { documentSnapshot.exists() } returns true

    coEvery { documentReference.get() } returns task
    every { task.isComplete } returns true
    every { task.isCanceled } returns false
    every { task.isSuccessful } returns true
    every { task.result } returns documentSnapshot
    every { task.exception } returns null
    every { task.await() } returns documentSnapshot

    every { task2.isComplete } returns true
    every { task2.isCanceled } returns false
    every { task2.isSuccessful } returns true
    every { task2.result }
    every { task2.exception } returns null
    every { task2.await() }
  }

  @After
  fun teardown() {
    clearAllMocks()
  }

  @Test
  fun `test getFieldFromUser`() {
    // Mock data
    val uid = "testUserId"
    val field = "displayName"
    val expectedValue = "Test User"

    // Mock Firestore behavior

    coEvery { documentSnapshot.getString(field) } returns expectedValue

    // Run the test
    runBlocking {
      val result = databaseManager.getFieldFromUser(uid, field)
      assert(result == expectedValue)
    }
  }
}
