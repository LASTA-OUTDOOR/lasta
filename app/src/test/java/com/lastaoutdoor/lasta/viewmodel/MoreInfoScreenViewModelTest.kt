package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.map.ClimbingMarker
import com.lastaoutdoor.lasta.models.map.HikingMarker
import junit.framework.TestCase
import org.junit.Test

class MoreInfoScreenViewModelTest {

  @Test
  fun testFetchDiff() {
    val moreInfoScreenViewModel: MoreInfoScreenViewModel = MoreInfoScreenViewModel()
    val fakeActivity =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY)
    val fakeActivity2 =
        Activity("b", 10, activityType = ActivityType.HIKING, difficulty = Difficulty.NORMAL)
    val fakeActivity3 =
        Activity("c", 10, activityType = ActivityType.BIKING, difficulty = Difficulty.HARD)

    TestCase.assertEquals("Easy", moreInfoScreenViewModel.processDiffText(fakeActivity))
    TestCase.assertEquals("Normal", moreInfoScreenViewModel.processDiffText(fakeActivity2))
    TestCase.assertEquals("Hard", moreInfoScreenViewModel.processDiffText(fakeActivity3))
  }

  @Test
  fun testMoreInfoMarker() {
    val moreInfoScreenViewModel: MoreInfoScreenViewModel = MoreInfoScreenViewModel()
    val fakeActivity =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY)
    val fakeActivity2 =
        Activity("b", 10, activityType = ActivityType.HIKING, difficulty = Difficulty.NORMAL)
    val fakeActivity3 =
        Activity("c", 10, activityType = ActivityType.BIKING, difficulty = Difficulty.HARD)
    assert(
        moreInfoScreenViewModel.goToMarker(fakeActivity) ==
            ClimbingMarker("", LatLng(0.0, 0.0), "", R.drawable.climbing_icon))
    assert(
        moreInfoScreenViewModel.goToMarker(fakeActivity2) ==
            HikingMarker("", LatLng(0.0, 0.0), "", R.drawable.hiking_icon, 10))
    assert(
        moreInfoScreenViewModel.goToMarker(fakeActivity3) ==
            HikingMarker("", LatLng(0.0, 0.0), "", R.drawable.hiking_icon, 10, ActivityType.BIKING))
  }
}
