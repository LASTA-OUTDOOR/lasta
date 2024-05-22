package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ConnectivityViewModelTest {
  private val errorToast = mockk<ErrorToast>()
  private val repo = FakeConnectivityviewRepo()
  private val vm = ConnectivityViewModel(repo, errorToast)

  @Test
  fun connectTest() {
    assertEquals(vm.connectionState.value, ConnectionState.OFFLINE)
  }
}
