package com.lastaoutdoor.lasta

import com.lastaoutdoor.lasta.models.activity.HikingActivity
import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.models.api.SimpleWay
import com.lastaoutdoor.lasta.models.api.Tags
import junit.framework.TestCase.assertEquals
import org.junit.Test

class OSMDataTest {
  @Test
  fun tagAttributes() {
    val tag = Tags("a", "b", "c", "d", "e", "f")
    assertEquals("a", tag.name)
    assertEquals("b", tag.sport)
    assertEquals("c", tag.route)
    assertEquals("d", tag.from)
    assertEquals("e", tag.to)
    assertEquals("f", tag.distance)
    assertEquals("name: a, sport: b", tag.toString())
  }

  @Test
  fun positionAtrributes() {
    val pos = Position(1.0, 2.0)
    assertEquals(1.0, pos.lat)
    assertEquals(2.0, pos.lon)
  }

  @Test
  fun nodeWayFunctions() {
    val tag = Tags("a", "b", "c", "d", "e", "f")
    val nodeWay = NodeWay("node", 1L, 2.0, 3.0, Position(1.0, 2.0), tag)
    assertEquals(Position(2.0, 3.0), nodeWay.getPosition())
    val nodeWay2 = NodeWay("a", 1L, 2.0, 3.0, Position(1.0, 2.0), tag)
    assertEquals(Position(1.0, 2.0), nodeWay2.getPosition())
    assertEquals("", nodeWay.getActivityFromData().activityId)
    assertEquals(1L, nodeWay.getActivityFromData().osmId)
    assertEquals("a", nodeWay.getActivityFromData().name)
    assertEquals(Position(2.0, 3.0), nodeWay.getActivityFromData().startPosition)
  }

  @Test
  fun simpleWayToString() {
    val pos = Position(1.0, 2.0)
    val listPos: List<Position>? = listOf(pos)
    val simpleWay = SimpleWay(listPos)
    assertEquals("Simple Way : $listPos\n", simpleWay.toString())
  }

  @Test
  fun relationFunctions() {
    val tag = Tags("a", "b", "hiking", "d", "e", "f")
    val pos = Position(1.0, 2.0)
    val listPos = listOf(pos)
    val simpleWay = SimpleWay(listPos)
    val relation = Relation("a", 1L, tag, listOf(simpleWay))
    val tag2 = Tags("a", "b", "hiking", "d", "e", "f")
    assertEquals(Position(1.0, 2.0), relation.getPosition())
    val relation2 = Relation("a", 1L, tag2, null)
    assertEquals(Position(0.0, 0.0), relation2.getPosition())

    val hikingActivity =
        HikingActivity(
            activityId = "",
            osmId = 1L,
            name = "a",
            startPosition = Position(1.0, 2.0),
            from = "d",
            to = "e")
    assertEquals(hikingActivity.activityId, relation.getActivityFromData().activityId)
    assertEquals(hikingActivity.osmId, relation.getActivityFromData().osmId)
    assertEquals(hikingActivity.name, relation.getActivityFromData().name)
    assertEquals(hikingActivity.startPosition, relation.getActivityFromData().startPosition)
    val pos2 = relation2.getActivityFromData()
  }
}
