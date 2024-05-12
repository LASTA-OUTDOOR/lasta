package com.lastaoutdoor.lasta.data.api.osm

import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityRepositoryImplTest {

  private lateinit var repository: ActivityRepository
  private lateinit var osmApiService: OSMApiService

  @Before
  fun setUp() {
    osmApiService = mockk(relaxed = true)
    repository = ActivityRepositoryImpl(osmApiService)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `test getClimbingPointsInfo success`() {
    val mockCall = mockk<Call<APIResponse<NodeWay>>>()

    every { osmApiService.getClimbingPointsInfo(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<NodeWay>>>(0)
      callback.onResponse(mockCall, Response.success(APIResponse(listOf())))
    }

    runTest {
      val response = repository.getClimbingPointsInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Success)
    }
  }

  @Test
  fun `test getClimbingPointsInfo failure after reaching endpoint`() {
    val mockCall = mockk<Call<APIResponse<NodeWay>>>()
    val res = mockk<Response<APIResponse<NodeWay>>>()

    every { osmApiService.getClimbingPointsInfo(any()) } returns mockCall
    every { res.isSuccessful } returns false
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<NodeWay>>>(0)
      callback.onResponse(mockCall, res)
    }

    runTest {
      val response = repository.getClimbingPointsInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test getClimbingPointsInfo failure`() {
    val mockCall = mockk<Call<APIResponse<NodeWay>>>()

    every { osmApiService.getClimbingPointsInfo(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<NodeWay>>>(0)
      callback.onFailure(mockCall, Throwable("Error"))
    }

    runTest {
      val response = repository.getClimbingPointsInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }
}