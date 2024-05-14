package com.lastaoutdoor.lasta.data

import com.lastaoutdoor.lasta.data.db.UserDBRepositoryImpl
import com.lastaoutdoor.lasta.data.preferences.PreferencesRepositoryImpl
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import io.mockk.mockk
import io.mockk.spyk

class MainActivityTest {

  private val m = spyk(MainActivity())
  private val pr: PreferencesRepositoryImpl = mockk()
  private val db: UserDBRepositoryImpl = mockk()
  private val vm: PreferencesViewModel = PreferencesViewModel(pr, db)

  // @Test
  // fun testMainActivity(){
  //  mockkConstructor(PreferencesViewModel::class)

  // }
}
