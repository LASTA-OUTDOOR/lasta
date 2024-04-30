package com.lastaoutdoor.lasta.data.model.profile

import com.lastaoutdoor.lasta.models.activity.ActivityType
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ActivitiesDatabaseTypeTest {

  @Test
  fun `toString from Sport enums properly converts`() {
    assertEquals("Climbing", ActivityType.CLIMBING.toString())
    assertEquals("Hiking", ActivityType.HIKING.toString())
  }
}
