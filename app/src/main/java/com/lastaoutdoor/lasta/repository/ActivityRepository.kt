package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.data.model.api.Relation
import com.lastaoutdoor.lasta.utils.Response

/**
 * This is an interface for the [ActivityRepository] implementation. We define here the main methods
 * to fetch the data from the OSM API.
 */
interface ActivityRepository {

  /**
   * This method fetches the climbing points info from the OSM API.
   *
   * @param range The range in meters to search for climbing points.
   * @param lat The latitude of the location to search for climbing points.
   * @param lon The longitude of the location to search for climbing points.
   * @return A [Response] object with the list of climbing points as [NodeWay] objects.
   */
  suspend fun getClimbingPointsInfo(range: Int, lat: Double, lon: Double): Response<List<NodeWay>>

  /**
   * This method fetches a climbing point by its id from the OSM API.
   *
   * @param id The OSM id of the climbing point to fetch.
   * @return A [Response] object with the climbing point as a [NodeWay] object.
   */
  suspend fun getClimbingPointById(id: Long): Response<NodeWay>

  /**
   * This method fetches the hiking routes info from the OSM API.
   *
   * @param range The range in meters to search for hiking routes.
   * @param lat The latitude of the location to search for hiking routes.
   * @param lon The longitude of the location to search for hiking routes.
   * @return A [Response] object with the list of hiking routes as [Relation] objects.
   */
  suspend fun getHikingRoutesInfo(range: Int, lat: Double, lon: Double): Response<List<Relation>>

  /**
   * This method fetches a hiking route by its id from the OSM API.
   *
   * @param id The OSM id of the hiking route to fetch.
   * @return A [Response] object with the hiking route as a [Relation] object.
   */
  suspend fun getHikingRouteById(id: Long): Response<Relation>

  /**
   * This method fetches the biking routes info from the OSM API.
   *
   * @param range The range in meters to search for biking routes.
   * @param lat The latitude of the location to search for biking routes.
   * @param lon The longitude of the location to search for biking routes.
   * @return A [Response] object with the list of biking routes as [Relation] objects.
   */
  suspend fun getBikingRoutesInfo(range: Int, lat: Double, lon: Double): Response<List<Relation>>

  /**
   * This method fetches a biking route by its id from the OSM API.
   *
   * @param id The OSM id of the biking route to fetch.
   * @return A [Response] object with the biking route as a [Relation] object.
   */
  suspend fun getBikingRouteById(id: Long): Response<Relation>
}
