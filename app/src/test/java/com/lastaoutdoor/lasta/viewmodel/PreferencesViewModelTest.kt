import com.google.common.base.Verify.verify
import com.lastaoutdoor.lasta.data.preferences.PreferencesDataStore
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test

class PreferencesViewModelTest {

    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var mockPreferencesDataStore: PreferencesDataStore

    @Before
    fun setUp() {
        mockPreferencesDataStore = mockk()
        preferencesViewModel = PreferencesViewModel(mockPreferencesDataStore)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `updateIsLoggedIn should call updateIsLoggedIn from PreferencesDataStore`() {
        val isLoggedIn = true
        preferencesViewModel.updateIsLoggedIn(isLoggedIn)
    }


}