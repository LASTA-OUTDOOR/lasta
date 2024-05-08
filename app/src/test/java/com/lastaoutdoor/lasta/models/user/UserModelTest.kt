package com.lastaoutdoor.lasta.models.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.models.activity.ActivityType
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class UserModelTest {

  @Test
  fun userModel_copyUserWithFirebaseInfo() {
    val firebaseUser = mockk<FirebaseUser>()
    every { firebaseUser.uid } returns "uid"
    every { firebaseUser.displayName } returns "displayName"
    every { firebaseUser.email } returns "email"

    val mockedPhotoUrl = mockk<Uri>()
    every { mockedPhotoUrl.toString() } returns "photoUrl"

    every { firebaseUser.photoUrl } returns mockedPhotoUrl

    val userModel = UserModel(firebaseUser)
    val copyUser = userModel.copyUserWithFirebaseInfo(firebaseUser)
    assert(copyUser.userId == "uid")
    assert(copyUser.userName == "displayName")
    assert(copyUser.email == "email")
    assert(copyUser.profilePictureUrl == "photoUrl")
    assert(copyUser.description == "")
    assert(copyUser.language == Language.ENGLISH)
    assert(copyUser.prefActivity == ActivityType.CLIMBING)
    assert(
        copyUser.levels ==
            UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER))
    assert(copyUser.friends.isEmpty())
    assert(copyUser.friendRequests.isEmpty())
    assert(copyUser.favorites.isEmpty())
  }
}
