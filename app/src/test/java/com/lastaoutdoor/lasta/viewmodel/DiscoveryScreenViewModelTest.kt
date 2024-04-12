package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.data.model.api.Tags
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DiscoveryScreenViewModelTest {

  private lateinit var viewModel: DiscoveryScreenViewModel
  private val repository = MockRepository()

  @Before
  fun setUp() {
    viewModel = DiscoveryScreenViewModel(repository)
  }

  private fun dummyNode(type: ActivityType, name: String, position: LatLng): Node {
    return Node(
        type.toString(),
        0,
        position.latitude,
        position.longitude,
        Tags(name, type.toString()),
        type,
        0,
        10.2f,
        "1",
        name)
  }

  @Test
  fun `fetchClimbingActivities should populate climbingActivities`() = runBlocking {
    // Given
    val centerLocation = LatLng(46.519962, 6.633597)
    val rad = 10000.0
    val climbingNodes =
        listOf(
            dummyNode(ActivityType.CLIMBING, "Climbing Node 1", centerLocation),
            dummyNode(ActivityType.CLIMBING, "Climbing Node 2", centerLocation))
    repository.addClimbingNode(climbingNodes[0])
    repository.addClimbingNode(climbingNodes[1])

    // When
    viewModel.fetchClimbingActivities(rad, centerLocation)

    // Then
    assertEquals(climbingNodes.size, viewModel.climbingActivities.size)
  }

  @Test
  fun `fetchClimbingActivities with no arguments should populate climbingActivities`() =
      runBlocking {
        // Given
        val centerLocation = LatLng(46.519962, 6.633597)
        val rad = 10000.0
        val climbingNodes =
            listOf(
                dummyNode(ActivityType.CLIMBING, "Climbing Node 1", centerLocation),
                dummyNode(ActivityType.CLIMBING, "Climbing Node 2", centerLocation))
        repository.addClimbingNode(climbingNodes[0])
        repository.addClimbingNode(climbingNodes[1])

        // When
        viewModel.fetchClimbingActivities()

        // Then
        assertEquals(climbingNodes.size, viewModel.climbingActivities.size)
      }

  @Test
  fun `showDialog should set displayDialog to true`() {
    // Given
    val outdoorActivity = OutdoorActivity(ActivityType.HIKING, 3, 5.0f, "2 hours", "Zurich")

    // When
    viewModel.showDialog(outdoorActivity)

    // Then
    assertEquals(true, viewModel.displayDialog.value)
    assertEquals(outdoorActivity, viewModel.activityToDisplay.value)
  }
}
