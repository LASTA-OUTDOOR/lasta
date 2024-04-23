package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import junit.framework.TestCase
import org.junit.Test

class MoreInfoScreenViewModelTest {

  @Test
  fun testFetchDiff() {
    val moreInfoScreenViewModel: MoreInfoScreenViewModel = MoreInfoScreenViewModel()
    val fakeActivity = OutdoorActivity(ActivityType.CLIMBING, 0, 1.5f, "3 hours", "Test Title")
    val fakeActivity2 = OutdoorActivity(ActivityType.CLIMBING, 1, 1.5f, "3 hours", "Test Title")
    val fakeActivity3 = OutdoorActivity(ActivityType.CLIMBING, 2, 1.5f, "3 hours", "Test Title")
    val fakeActivity4 = OutdoorActivity(ActivityType.CLIMBING, 3, 1.5f, "3 hours", "Test Title")
    TestCase.assertEquals("Easy", moreInfoScreenViewModel.processDiffText(fakeActivity))
    TestCase.assertEquals("Medium", moreInfoScreenViewModel.processDiffText(fakeActivity2))
    TestCase.assertEquals("Difficult", moreInfoScreenViewModel.processDiffText(fakeActivity3))
    TestCase.assertEquals(
        "No available difficulty", moreInfoScreenViewModel.processDiffText(fakeActivity4))
  }
}
