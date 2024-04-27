package com.lastaoutdoor.lasta.data.model.profile

import com.lastaoutdoor.lasta.models.profile.ActivitiesDatabaseType
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ActivitiesDatabaseTypeTest {

  @Test
  fun `toString from Sport enums properly converts`() {
    assertEquals("Climbing", ActivitiesDatabaseType.Sports.CLIMBING.toString())
    assertEquals("Hiking", ActivitiesDatabaseType.Sports.HIKING.toString())
  }
}
