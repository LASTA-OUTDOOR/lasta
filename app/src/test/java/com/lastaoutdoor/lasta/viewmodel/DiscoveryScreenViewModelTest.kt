package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class DiscoveryScreenViewModelTest {

  private lateinit var viewModel: DiscoverScreenViewModel
  private val repository = mockk<ActivityRepository>()

  @Before
  fun setUp() {
    viewModel = DiscoverScreenViewModel(repository)
  }

  @Test
  fun testInitialValues() {
    assertEquals(viewModel.activities.value.isEmpty(), false) // Check initial activities
    assertEquals(viewModel.screen.value, DiscoverDisplayType.LIST)
    assertEquals(viewModel.range.value, 10000.0)
    assertEquals(
        viewModel.localities,
        listOf(
            "Ecublens" to LatLng(46.519962, 6.633597),
            "Geneva" to LatLng(46.2043907, 6.1431577),
            "Payerne" to LatLng(46.834190, 6.928969),
            "Matterhorn" to LatLng(45.980537, 7.641618)))
  }
}
