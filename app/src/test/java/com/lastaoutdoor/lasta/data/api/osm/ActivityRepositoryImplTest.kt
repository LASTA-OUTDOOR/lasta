package com.lastaoutdoor.lasta.data.api.osm

import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.models.api.Tags
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

  @Test
  fun `test getClimbingPointById success`() {
    val mockCall = mockk<Call<APIResponse<NodeWay>>>()

    every { osmApiService.getClimbingPointById(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<NodeWay>>>(0)
      callback.onResponse(mockCall, Response.success(APIResponse(listOf(NodeWay("node", 0L, null, null, null, Tags())))))
    }

    runTest {
      val response = repository.getClimbingPointById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Success)
    }
  }

  @Test
  fun `test getClimbingPointById failure after reaching endpoint`() {
    val mockCall = mockk<Call<APIResponse<NodeWay>>>()
    val res = mockk<Response<APIResponse<NodeWay>>>()

    every { osmApiService.getClimbingPointById(any()) } returns mockCall
    every { res.isSuccessful } returns false
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<NodeWay>>>(0)
      callback.onResponse(mockCall, res)
    }

    runTest {
      val response = repository.getClimbingPointById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test getClimbingPointById failure`() {
    val mockCall = mockk<Call<APIResponse<NodeWay>>>()

    every { osmApiService.getClimbingPointById(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<NodeWay>>>(0)
      callback.onFailure(mockCall, Throwable("Error"))
    }

    runTest {
      val response = repository.getClimbingPointById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test get HikingRoutesInfo success`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()

    every { osmApiService.getHikingRoutesInfo(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onResponse(mockCall, Response.success(APIResponse(listOf())))
    }

    runTest {
      val response = repository.getHikingRoutesInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Success)
    }
  }

  @Test
  fun `test get HikingRoutesInfo failure after reaching endpoint`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()
    val res = mockk<Response<APIResponse<Relation>>>()

    every { osmApiService.getHikingRoutesInfo(any()) } returns mockCall
    every { res.isSuccessful } returns false
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onResponse(mockCall, res)
    }

    runTest {
      val response = repository.getHikingRoutesInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test get HikingRoutesInfo failure`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()

    every { osmApiService.getHikingRoutesInfo(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onFailure(mockCall, Throwable("Error"))
    }

    runTest {
      val response = repository.getHikingRoutesInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test getBikingRoutesInfo success`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()

    every { osmApiService.getBikingRoutesInfo(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onResponse(mockCall, Response.success(APIResponse(listOf())))
    }

    runTest {
      val response = repository.getBikingRoutesInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Success)
    }
  }

  @Test
  fun `test getBikingRoutesInfo failure after reaching endpoint`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()
    val res = mockk<Response<APIResponse<Relation>>>()

    every { osmApiService.getBikingRoutesInfo(any()) } returns mockCall
    every { res.isSuccessful } returns false
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onResponse(mockCall, res)
    }

    runTest {
      val response = repository.getBikingRoutesInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test getBikingRoutesInfo failure`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()

    every { osmApiService.getBikingRoutesInfo(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onFailure(mockCall, Throwable("Error"))
    }

    runTest {
      val response = repository.getBikingRoutesInfo(10, 0.0, 0.0)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test getHikingRouteById success`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()

    every { osmApiService.getHikingRouteById(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onResponse(mockCall, Response.success(APIResponse(listOf(Relation("relation", 0L, Tags(), null)))))
    }

    runTest {
      val response = repository.getHikingRouteById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Success)
    }
  }

  @Test
  fun `test getHikingRouteById failure after reaching endpoint`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()
    val res = mockk<Response<APIResponse<Relation>>>()

    every { osmApiService.getHikingRouteById(any()) } returns mockCall
    every { res.isSuccessful } returns false
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onResponse(mockCall, res)
    }

    runTest {
      val response = repository.getHikingRouteById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test getHikingRouteById failure`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()

    every { osmApiService.getHikingRouteById(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onFailure(mockCall, Throwable("Error"))
    }

    runTest {
      val response = repository.getHikingRouteById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test getBikingRouteById success`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()

    every { osmApiService.getBikingRouteById(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onResponse(mockCall, Response.success(APIResponse(listOf(Relation("relation", 0L, Tags(), null)))))
    }

    runTest {
      val response = repository.getBikingRouteById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Success)
    }
  }

  @Test
  fun `test getBikingRouteById failure after reaching endpoint`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()
    val res = mockk<Response<APIResponse<Relation>>>()

    every { osmApiService.getBikingRouteById(any()) } returns mockCall
    every { res.isSuccessful } returns false
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onResponse(mockCall, res)
    }

    runTest {
      val response = repository.getBikingRouteById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }

  @Test
  fun `test getBikingRouteById failure`() {
    val mockCall = mockk<Call<APIResponse<Relation>>>()

    every { osmApiService.getBikingRouteById(any()) } returns mockCall
    every { mockCall.enqueue(any()) } answers {
      val callback = arg<Callback<APIResponse<Relation>>>(0)
      callback.onFailure(mockCall, Throwable("Error"))
    }

    runTest {
      val response = repository.getBikingRouteById(10)
      assert(response is com.lastaoutdoor.lasta.utils.Response.Failure)
    }
  }
}