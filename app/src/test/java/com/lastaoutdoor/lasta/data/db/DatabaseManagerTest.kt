/*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.data.db.DatabaseManager
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@Config(manifest = Config.NONE)
class DatabaseManagerTest {

    private lateinit var databaseManager: DatabaseManager
    private lateinit var firestore: FirebaseFirestore

    @Before
    fun setup() {
        firestore = mockk()
        databaseManager = DatabaseManager(firestore)
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
        val documentSnapshot = mockk<DocumentSnapshot>()
        val documentReference = mockk<DocumentReference>()

        // Mock Firestore behavior
        coEvery { firestore.collection(any()).document(any()).get().await() } returns documentSnapshot
        coEvery { documentSnapshot.getString(field) } returns expectedValue

        // Run the test
        runBlocking {
            val result = databaseManager.getFieldFromUser(uid, field)
            assert(result == expectedValue)
        }
    }
}
*/
