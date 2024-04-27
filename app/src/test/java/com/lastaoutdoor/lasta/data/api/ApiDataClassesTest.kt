package com.lastaoutdoor.lasta.data.api

import androidx.test.core.app.ApplicationProvider
import com.lastaoutdoor.lasta.data.api.osm.APIResponse
import com.lastaoutdoor.lasta.data.api.osm.ActivityRepositoryImpl
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.models.api.NodeWay
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ApiDataClassesTest {
  private val con = ApplicationProvider.getApplicationContext<android.content.Context>()
  private val rep = ActivityRepositoryImpl(AppModule.provideOSMAPIService(con))

  @Test
  fun outdoorActivityResponse() {
    val od = APIResponse<NodeWay>(emptyList())
    assert(od.elements == emptyList<NodeWay>())
  }

  /*
  @Test
  fun outdoorActivityRepositoryStringClimbing() {

    val str = rep.getDataStringClimbing(1000, 1.0, 1.0, "node")
    assert(str == "[out:json];node(around:1000,1.0,1.0)[sport=climbing];out geom;")
  }

  @Test
  fun outdoorActivityRepositoryStringHiking() {

    val str = rep.getDataStringHiking(1000, 1.0, 1.0)
    assert(str == "[out:json];relation(around:1000,1.0,1.0)[route][route=\"hiking\"];out geom;")
  }*/
  /*
   @Test
   fun outdoorActivityRepositorygetNodesEmpty() {
     val nodes = rep.getClimbingActivitiesNode(1, 1.0, 1.0)
     assert(nodes == OutdoorActivityResponse<Node>(0.6f, emptyList()))
   }

   @Test
   fun outdoorActivityRepositorygetNodesNotEmpty() {
     val nodes = rep.getClimbingActivitiesNode(10000, 46.5, 6.6)

     assert(nodes.elements[0].getActivityType() == ActivityType.CLIMBING)
   }

   @Test
   fun outdoorActivityRepositorygetNodesFailed() {
     val nodes = rep.getClimbingActivitiesNode(-1, -1.0, 6.6)

     assert(nodes.version == 0.0f)
   }

   @Test
   fun outdoorActivityRepositorygetWayEmpty() {
     val nodes = rep.getClimbingActivitiesWay(1, 1.0, 1.0)
     assert(nodes == OutdoorActivityResponse<Node>(0.6f, emptyList()))
   }

   @Test
   fun outdoorActivityRepositorygetWayNotEmpty() {
     val nodes = rep.getClimbingActivitiesWay(100000, 46.5, 6.6)

     assert(nodes.elements[0].getActivityType() == ActivityType.CLIMBING)
   }

   @Test
   fun outdoorActivityRepositorygetWayFailed() {
     val nodes = rep.getClimbingActivitiesWay(-1, -1.0, 6.6)

     assert(nodes.version == 0.0f)
   }

   @Test
   fun outdoorActivityRepositorygetEmpty() {
     val nodes = rep.getClimbingActivitiesNode(1, 1.0, 1.0)
     assert(nodes == OutdoorActivityResponse<Node>(0.6f, emptyList()))
   }

   @Test
   fun outdoorActivityRepositorygetHikingNotEmpty() {
     val nodes = rep.getHikingActivities(10000, 46.5, 6.6)
     println(nodes.elements[0])
     assert(nodes.elements[0].getActivityType() == ActivityType.HIKING)
   }

   @Test
   fun outdoorActivityRepositorygetHikingFailed() {
     val nodes = rep.getHikingActivities(-1, -1.0, 6.6)

     assert(nodes.version == 0.0f)
   }

  */

  /*
  @Test
  fun simpleWay() {
    val sw = SimpleWay(listOf(Position(1.0, 1.0)))

    assert(
        (sw.nodes?.get(0) ?: emptyList<Position>()) == Position(1.0, 1.0) &&
            sw.toString() == "Simple Way : [Position(lat=1.0, lon=1.0)]\n")
  }

  @Test
  fun tags() {
    val tg = Tags("name", "sport")
    assert(tg.name == "name" && tg.sport == "sport" && tg.toString() == "name: name, sport: sport")
  }

  @Test
  fun bounds() {
    val bd = Bounds(-1.0, -1.0, -1.0, -1.0)
    assert(bd.maxlat == -1.0 && bd.minlat == -1.0 && bd.maxlon == -1.0 && bd.minlon == -1.0)
  }

  @Test
  fun relation() {
    val rel =
        Relation(
            "type",
            0L,
            Tags("name", "sport"),
            listOf(SimpleWay(listOf(Position(1.0, 1.0)))),
            Bounds(-1.0, -1.0, -1.0, -1.0),
            ActivityType.HIKING,
            0,
            0.0f,
            null,
            null)
    assert(
        rel.type == "type" &&
            rel.id == 0L &&
            rel.tags == Tags("name", "sport") &&
            rel.bounds == Bounds(-1.0, -1.0, -1.0, -1.0) &&
            (rel.ways?.get(0)?.nodes?.get(0)?.lat ?: 0.0) == 1.0 &&
            rel.getActivityType() == ActivityType.HIKING &&
            rel.difficulty == 0 &&
            rel.length == 0.0f &&
            rel.duration == "not given" &&
            rel.locationName == "unnamed")
  }

  @Test
  fun way() {
    val w =
        Way(
            "type",
            0L,
            Tags("name", "sport"),
            listOf(Position(1.0, 1.0)),
            ActivityType.HIKING,
            0,
            0.0f,
            "dur",
            "loc")
    assert(
        w.type == "type" &&
            w.id == 0L &&
            w.tags == Tags("name", "sport") &&
            w.nodes[0].lat == 1.0 &&
            w.getActivityType() == ActivityType.HIKING &&
            w.difficulty == 0 &&
            w.length == 0.0f &&
            w.duration == "dur" &&
            w.locationName == "loc" &&
            w.toString() ==
                " type: ${w.type} id: ${w.id}  activityType: ${w.getActivityType()} name: ${w.tags.name} nodes: ${w.nodes}\n")
  }

  @Test
  fun node() {
    val n =
        Node(
            "type", 0L, 0.0, 0.0, Tags("name", "sport"), ActivityType.HIKING, 0, 0.0f, "dur", "loc")
    assert(
        n.type == "type" &&
            n.id == 0L &&
            n.tags == Tags("name", "sport") &&
            n.getActivityType() == ActivityType.HIKING &&
            n.difficulty == 0 &&
            n.length == 0.0f &&
            n.duration == "dur" &&
            n.locationName == "" &&
            n.lon == 0.0 &&
            n.lat == 0.0 &&
            n.toString() ==
                " type: ${n.type} id: ${n.id} lat: ${n.lat} lon: ${n.lon} activityType: ${n.getActivityType()} name: ${n.tags.name}\n")
  }*/
}
