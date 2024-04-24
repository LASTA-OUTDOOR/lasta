package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.data.model.api.Position
import com.lastaoutdoor.lasta.data.model.api.Tags
import java.lang.reflect.Method
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
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

  private fun dummyNode(type: ActivityType, name: String, position: LatLng): NodeWay {
    return NodeWay(type.toString(), 0, position.latitude,
      position.longitude, Position(position.latitude, position.longitude), Tags(""))
  }

  @Test
  fun `fetchClimbingActivities should populate climbingActivities`() = runTest {
    // Given
    val centerLocation = LatLng(46.519962, 6.633597)
    val rad = 10000.0
    val climbingNodes =
        listOf(
            dummyNode(ActivityType.CLIMBING, "Climbing Node 1", centerLocation),
            dummyNode(ActivityType.CLIMBING, "Climbing Node 2", centerLocation))
    repository.addClimbingNode(climbingNodes[0])
    repository.addClimbingNode(climbingNodes[1])

    val method: Method =
        DiscoveryScreenViewModel::class
            .java
            .getDeclaredMethod("fetchClimbingActivities", Double::class.java, LatLng::class.java)
    method.isAccessible = true
    method.invoke(viewModel, rad, centerLocation)

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

        val method: Method =
            DiscoveryScreenViewModel::class
                .java
                .getDeclaredMethod(
                    "fetchClimbingActivities", Double::class.java, LatLng::class.java)
        method.isAccessible = true
        method.invoke(viewModel, rad, centerLocation)

        // Then
        assertEquals(climbingNodes.size, viewModel.climbingActivities.size)
      }
}
