package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ConnectivityViewModelTest {
  private val repo = FakeConnectivityviewRepo()
  private val vm = ConnectivityViewModel(repo)

  @Test
  fun connectTest() {
    assertEquals(vm.connectionState.value, ConnectionState.OFFLINE)
  }
}
