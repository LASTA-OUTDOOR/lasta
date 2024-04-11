package com.lastaoutdoor.lasta.data.db

import org.junit.Before

class DatabaseManagerTest {
  private lateinit var databaseManager: DatabaseManager

  @Before
  fun setUp() {
    databaseManager = DatabaseManager()
  }
}
