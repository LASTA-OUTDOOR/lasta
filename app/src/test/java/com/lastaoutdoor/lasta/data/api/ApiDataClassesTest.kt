package com.lastaoutdoor.lasta.data.api

import androidx.test.core.app.ApplicationProvider
import com.google.api.Context
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.data.model.api.Tags
import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
@RunWith(RobolectricTestRunner::class)
  class ApiDataClassesTest {
    private val con = ApplicationProvider.getApplicationContext<android.content.Context>()
    private val rep = OutdoorActivityRepositoryImpl(AppModule.provideAPIService(con))


    @Test
    fun nullLoading() {
        val ld = ApiResponse.Loading<String>()
        assert(ld.data == null)
    }

    @Test
    fun success() {
        val sc = ApiResponse.Success("Testing")
        assert(sc.data == "Testing")
    }

    @Test
    fun error() {
        val er = ApiResponse.Error<String>("Error")
        assert(er.errorMessage == "Error")
    }

    @Test
    fun outdoorActivityResponse() {
        val od = OutdoorActivityResponse<Node>(0.5f, emptyList())
        assert(od.version == 0.5f && od.elements == emptyList<Node>())
    }

    @Test
    fun outdoorActivityRepositoryStringClimbing() {

        val str = rep.getDataStringClimbing(1000, 1.0, 1.0, "node")
        assert(str == "[out:json];node(around:1000,1.0,1.0)[sport=climbing];out geom;")

    }
    @Test
    fun outdoorActivityRepositoryStringHiking() {

        val str = rep.getDataStringHiking(1000, 1.0, 1.0)
        assert(str == "[out:json];relation(around:1000,1.0,1.0)[route][route=\"hiking\"];out geom;")

    }
    @Test
    fun outdoorActivityRepositorygetNodesEmpty(){
        val nodes = rep.getClimbingActivitiesNode(1,1.0,1.0)
        assert(nodes == OutdoorActivityResponse<Node>(0.6f, emptyList()))
    }
    @Test
    fun outdoorActivityRepositorygetNodesNotEmpty(){
        val nodes = rep.getClimbingActivitiesNode(10000,46.5,6.6)
        println(nodes.elements[0])
        assert(nodes.elements[0].getActivityType() == ActivityType.CLIMBING)
    }
    @Test
    fun outdoorActivityRepositorygetNodesFailed(){
        val nodes = rep.getClimbingActivitiesNode(-1,-1.0,6.6)

        assert(nodes.version== 0.0f)
    }
    @Test
    fun outdoorActivityRepositorygetWayEmpty(){
        val nodes = rep.getClimbingActivitiesWay(1,1.0,1.0)
        assert(nodes == OutdoorActivityResponse<Node>(0.6f, emptyList()))
    }
    @Test
    fun outdoorActivityRepositorygetWayNotEmpty(){
        val nodes = rep.getClimbingActivitiesWay(100000,46.5,6.6)
        println(nodes.elements[0])
        assert(nodes.elements[0].getActivityType() == ActivityType.CLIMBING)
    }
    @Test
    fun outdoorActivityRepositorygetWayFailed(){
        val nodes = rep.getClimbingActivitiesWay(-1,-1.0,6.6)

        assert(nodes.version== 0.0f)
    }
    @Test
    fun outdoorActivityRepositorygetEmpty(){
        val nodes = rep.getClimbingActivitiesNode(1,1.0,1.0)
        assert(nodes == OutdoorActivityResponse<Node>(0.6f, emptyList()))
    }
    @Test
    fun outdoorActivityRepositorygetHikingNotEmpty(){
        val nodes = rep.getHikingActivities(10000,46.5,6.6)
        println(nodes.elements[0])
        assert(nodes.elements[0].getActivityType() == ActivityType.HIKING)
    }
    @Test
    fun outdoorActivityRepositorygetHikingFailed(){
        val nodes = rep.getHikingActivities(-1,-1.0,6.6)

        assert(nodes.version== 0.0f)
    }
}


