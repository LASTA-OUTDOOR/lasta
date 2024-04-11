package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.repository.PreferencesRepository
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.After
import org.junit.Before

class PreferencesViewModelTest {

  private lateinit var preferencesViewModel: PreferencesViewModel
  private lateinit var mockPreferencesDataStore: PreferencesRepository

  @Before
  fun setUp() {
    mockPreferencesDataStore = mockk()
    preferencesViewModel = PreferencesViewModel(mockPreferencesDataStore)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }
}
