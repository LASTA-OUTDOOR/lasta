package com.lastaoutdoor.lasta.data.api.osm

import com.lastaoutdoor.lasta.models.api.NodeWay
import org.junit.Test

class APIResponseTest {

  @Test
  fun `API response is a model that works`() {
    val od = APIResponse<NodeWay>(emptyList())
    assert(od.elements == emptyList<NodeWay>())
  }
}
