package com.lastaoutdoor.lasta.data.db

import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config


class DatabasemanagerTest {
  private lateinit var databaseManager: DatabaseManager

  @Before
  fun setUp() {
    databaseManager = DatabaseManager()
  }
}
