package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.utils.Response

interface ActivityRepository {
  suspend fun getClimbingPointsInfo(range: Int, lat: Double, lon: Double): Response<List<NodeWay>>
  suspend fun getClimbingPointById(id: Long): Response<NodeWay>
}
