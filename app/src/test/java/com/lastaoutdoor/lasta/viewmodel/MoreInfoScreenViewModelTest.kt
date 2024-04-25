package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.data.model.activity.Activity
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.Difficulty
import junit.framework.TestCase
import org.junit.Test

class MoreInfoScreenViewModelTest {

  @Test
  fun testFetchDiff() {
    val moreInfoScreenViewModel: MoreInfoScreenViewModel = MoreInfoScreenViewModel()
    val fakeActivity =
        Activity(ActivityType.CLIMBING, Difficulty.EASY, 1.5f, "3 hours", "Test Title")
    val fakeActivity2 =
        Activity(ActivityType.CLIMBING, Difficulty.NORMAL, 1.5f, "3 hours", "Test Title")
    val fakeActivity3 =
        Activity(ActivityType.CLIMBING, Difficulty.NORMAL, 1.5f, "3 hours", "Test Title")
    val fakeActivity4 =
        Activity(ActivityType.CLIMBING, Difficulty.HARD, 1.5f, "3 hours", "Test Title")
    TestCase.assertEquals("Easy", moreInfoScreenViewModel.processDiffText(fakeActivity))
    TestCase.assertEquals("Medium", moreInfoScreenViewModel.processDiffText(fakeActivity2))
    TestCase.assertEquals("Difficult", moreInfoScreenViewModel.processDiffText(fakeActivity3))
    TestCase.assertEquals(
        "No available difficulty", moreInfoScreenViewModel.processDiffText(fakeActivity4))
  }
}
