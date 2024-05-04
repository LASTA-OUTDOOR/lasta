package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.models.api.SimpleWay
import com.lastaoutdoor.lasta.models.api.Tags
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response

class FakeActivityRepository() : ActivityRepository {
  val t = Throwable("thrown")
  var currResponse: Response<Void> = Response.Success(null)

  val fakeNode = NodeWay("node", 10, 1.0, 1.0, Position(0.0, 0.0), Tags("name"))
  val fakeRel = Relation("node", 10, Tags("name"), listOf(SimpleWay(listOf(Position(0.0, 0.0)))))

  override suspend fun getClimbingPointsInfo(
      range: Int,
      lat: Double,
      lon: Double
  ): Response<List<NodeWay>> {
    return Response.Success(listOf(fakeNode))
  }

  override suspend fun getClimbingPointById(id: Long): Response<NodeWay> {
    return when (currResponse) {
      is Response.Failure -> Response.Failure(t)
      Response.Loading -> Response.Loading
      is Response.Success -> Response.Success(fakeNode)
    }
  }

  override suspend fun getHikingRoutesInfo(
      range: Int,
      lat: Double,
      lon: Double
  ): Response<List<Relation>> {
    return Response.Success(listOf(fakeRel))
  }

  override suspend fun getHikingRouteById(id: Long): Response<Relation> {
    return when (currResponse) {
      is Response.Failure -> Response.Failure(t)
      Response.Loading -> Response.Loading
      is Response.Success -> Response.Success(fakeRel)
    }
  }

  override suspend fun getBikingRoutesInfo(
      range: Int,
      lat: Double,
      lon: Double
  ): Response<List<Relation>> {
    return Response.Success(listOf(fakeRel))
  }

  override suspend fun getBikingRouteById(id: Long): Response<Relation> {
    return when (currResponse) {
      is Response.Failure -> Response.Failure(t)
      Response.Loading -> Response.Loading
      is Response.Success -> Response.Success(fakeRel)
    }
  }
}
