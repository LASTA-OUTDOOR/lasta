package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.SocialRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FakeSocialRepository : SocialRepository {

  var friends: ArrayList<UserModel> = ArrayList()
  var activities: ArrayList<ActivitiesDatabaseType> = ArrayList()
  var messages: ArrayList<String> = ArrayList()

  override fun getFriends(): List<UserModel>? {
    return friends
  }

  override fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>? {
    return activities
  }

  override fun getMessages(): List<String>? {
    return messages
  }

  fun clean() {
    friends.clear()
    activities.clear()
    messages.clear()
  }
}

class SocialViewModelTest {

  private val repo = FakeSocialRepository()
  private lateinit var viewModel: SocialViewModel

  @Before
  fun setUp() {
    viewModel = SocialViewModel(repo)
  }

  @Test
  fun `Default values`() {
    assertFalse(viewModel.isConnected)
    assert(viewModel.getNumberOfDays() == 7)
    assertFalse(viewModel.friendButton)
    assertTrue(viewModel.messages.isNullOrEmpty())
    assertTrue(viewModel.friends.isNullOrEmpty())
    assertTrue(viewModel.latestFriendActivities.isNullOrEmpty())
    assert(viewModel.topButtonText == "Default button")
  }

  @Test
  fun `Show top button`() {
    viewModel.showTopButton("Add friend", {})
    assertTrue(viewModel.friendButton)
    assert(viewModel.topButtonText == "Add friend")

    viewModel.hideTopButton()
    assertFalse(viewModel.friendButton)
  }
}
