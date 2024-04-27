package com.lastaoutdoor.lasta.data.api.osm

import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.models.api.Relation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This is an interface defining the different endpoints of the OSM API. We use Retrofit annotations
 * to define the HTTP requests.
 */
interface OSMApiService {

  /**
   * This method fetches the climbing points info from the OSM API. It uses the [GET] annotation to
   * define the HTTP request type. It uses the [Query] annotation to define the query parameters.
   *
   * @param query The query to search for climbing points.
   * @return A [Call] object with the list of climbing points as [NodeWay] objects.
   */
  @GET("interpreter")
  fun getClimbingPointsInfo(@Query("data") query: String): Call<APIResponse<NodeWay>>

  /**
   * This method fetches a climbing point by its id from the OSM API. It uses the [GET] annotation
   * to define the HTTP request type. It uses the [Query] annotation to define the query parameters.
   *
   * @param query The query to search for the climbing point by its id.
   * @return A [Call] object with the climbing point as a [NodeWay] object.
   */
  @GET("interpreter")
  fun getClimbingPointById(@Query("data") query: String): Call<APIResponse<NodeWay>>

  /**
   * This method fetches the hiking routes info from the OSM API. It uses the [GET] annotation to
   * define the HTTP request type. It uses the [Query] annotation to define the query parameters.
   *
   * @param query The query to search for hiking routes.
   * @return A [Call] object with the list of hiking routes as [Relation] objects.
   */
  @GET("interpreter")
  fun getHikingRoutesInfo(@Query("data") query: String): Call<APIResponse<Relation>>

  /**
   * This method fetches a hiking route by its id from the OSM API. It uses the [GET] annotation to
   * define the HTTP request type. It uses the [Query] annotation to define the query parameters.
   *
   * @param query The query to search for the hiking route by its id.
   * @return A [Call] object with the hiking route as a [Relation] object.
   */
  @GET("interpreter")
  fun getHikingRouteById(@Query("data") query: String): Call<APIResponse<Relation>>

  /**
   * This method fetches the biking routes info from the OSM API. It uses the [GET] annotation to
   * define the HTTP request type. It uses the [Query] annotation to define the query parameters.
   *
   * @param query The query to search for biking routes.
   * @return A [Call] object with the list of biking routes as [Relation] objects.
   */
  @GET("interpreter")
  fun getBikingRoutesInfo(@Query("data") query: String): Call<APIResponse<Relation>>

  /**
   * This method fetches a biking route by its id from the OSM API. It uses the [GET] annotation to
   * define the HTTP request type. It uses the [Query] annotation to define the query parameters.
   *
   * @param query The query to search for the biking route by its id.
   * @return A [Call] object with the biking route as a [Relation] object.
   */
  @GET("interpreter")
  fun getBikingRouteById(@Query("data") query: String): Call<APIResponse<Relation>>
}
