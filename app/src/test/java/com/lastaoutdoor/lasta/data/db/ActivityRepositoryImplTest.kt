import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserActivitiesRepositoryImplTest {
  /*public class mockTask<T : Any> : Task<T>() {
      override fun addOnFailureListener(p0: OnFailureListener): Task<T> {
        return this
      }

      override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<T> {
        return this
      }

      override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<T> {
        return this
      }

      override fun getException(): Exception? {
        return null
      }

      override fun getResult(): T {
        return this.getResult()
      }

      override fun <X : Throwable?> getResult(p0: Class<X>): T {
        return this.getResult()
      }

      override fun isCanceled(): Boolean {
        return false
      }

      override fun isComplete(): Boolean {
        return true
      }

      override fun isSuccessful(): Boolean {
        return true
      }

      override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in T>): Task<T> {
        return this
      }

      override fun addOnSuccessListener(p0: Activity, p1: OnSuccessListener<in T>): Task<T> {
        return this
      }

      override fun addOnSuccessListener(p0: OnSuccessListener<in T>): Task<T> {
        return this
      }

      fun await(): T {
        return getResult()
      }
    }

    var expectedActivitiesHK = hashMapOf("Hiking" to arrayListOf<ActivitiesDatabaseType.Trail>())
    private val firestore: FirebaseFirestore = mockk(relaxed = true)
    private val context: Context = mockk()
    private val user: FirebaseUser = mockk()
    private val documentReference: DocumentReference = mockk()
    private val documentSnapshot: DocumentSnapshot = mockk()
    private val activitiesRepository = UserActivitiesDBRepositoryImpl(firestore, context)
    private val task: mockTask<DocumentSnapshot> = mockk()
    private val task2: mockTask<Void> = mockk()

    @Before
    fun setup() {

      every { context.getString(R.string.activities_database_name) } returns "activities"
      every { firestore.collection(any()) } returns mockk()
      every { firestore.collection("activities").document(any()) } returns documentReference
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

    /*
    @Test
    fun `test getUserActivitiesEmpty`() = runBlocking {
      every { documentSnapshot.get("Hiking") } returns expectedActivitiesHK
      val activities =
          activitiesRepository.getUserActivities(
              UserModel(user, "", HikingLevel.BEGINNER), ActivitiesDatabaseType.Sports.HIKING)

      assertEquals(emptyList<ActivitiesDatabaseType>(), activities)
    }

     */

    @Test
    fun `test addActivityToUserActivities`() = runBlocking {
      val activity = ActivitiesDatabaseType.Trail()

      activitiesRepository.addActivityToUserActivities(user, activity)

      coVerify(exactly = 1) { firestore.collection("activities").document(any()) }
      // coVerify(exactly = 1) { documentReference.set(any(),any())}
    }

    @Test
    fun `test addUser`() = runBlocking {
      val userData =
          hashMapOf(
              "Hiking" to arrayListOf<ActivitiesDatabaseType.Trail>(),
              "Climbing" to arrayListOf<ActivitiesDatabaseType.Climb>())
      every { documentReference.set(userData, SetOptions.merge()) } returns task2
      val method: Method =
          UserActivitiesDBRepositoryImpl::class
              .java
              .getDeclaredMethod("addUserToActivitiesDatabase", FirebaseUser::class.java)

      method.isAccessible = true
      method.invoke(activitiesRepository, user)

      coVerify(exactly = 1) { firestore.collection("activities").document(any()) }
      // coVerify(exactly = 1) { documentReference.set(any(),any())}
    }
    /*
    @Test
    fun `test getUserActivitiesNotEmpty`() = runBlocking {


        every { documentSnapshot.get("Hiking") } returns hashMapOf("Hiking" to hashMapOf(
            "activityID" to 4L)
        )
        val activities =
            activitiesRepository.getUserActivities(user, ActivitiesDatabaseType.Sports.HIKING)

        assertEquals(arrayListOf( ActivitiesDatabaseType.Trail(activityId = 4L)), activities)
    }
    */
  */
}
