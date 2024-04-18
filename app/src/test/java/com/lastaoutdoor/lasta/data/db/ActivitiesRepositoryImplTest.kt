import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.data.db.ActivitiesRepositoryImpl
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class ActivitiesRepositoryImplTest {

  private lateinit var mockDatabase: FirebaseFirestore
  private lateinit var mockCollectionReference: CollectionReference
  private lateinit var mockDocumentReference: DocumentReference
  private lateinit var activitiesRepositoryImpl: ActivitiesRepositoryImpl

  @Before
  fun setUp() {
    mockkStatic(FirebaseFirestore::class)
    mockkObject(FirebaseFirestore::class)

    val context: Context = ApplicationProvider.getApplicationContext()
    FirebaseApp.initializeApp(context)

    mockDatabase = mockk()
    mockCollectionReference = mockk()
    mockDocumentReference = mockk()

    every { FirebaseFirestore.getInstance() } returns mockDatabase
    every { mockDatabase.collection(any()) } returns mockCollectionReference
    every { mockCollectionReference.document(any()) } returns mockDocumentReference

    activitiesRepositoryImpl = ActivitiesRepositoryImpl(mockDatabase, context)
  }

  @Test
  fun `test getUserActivities with user`() = runTest {
    // Given
    val user = mockk<FirebaseUser>()
    val activityType = ActivitiesDatabaseType.Sports.HIKING

    // When
    val result = activitiesRepositoryImpl.getUserActivities(user, activityType)

    // Then
    assert(result.isEmpty())
  }
}
