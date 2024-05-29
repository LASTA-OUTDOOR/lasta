package com.lastaoutdoor.lasta.ui.screen.tracking

import android.app.LocaleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lastaoutdoor.lasta.data.db.UserDBRepositoryImpl
import com.lastaoutdoor.lasta.data.preferences.PreferencesRepositoryImpl
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.di.NetworkModule
import com.lastaoutdoor.lasta.services.StopwatchService
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import com.lastaoutdoor.lasta.viewmodel.TrackingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@AndroidEntryPoint
class TrackingActivity : ComponentActivity() {

  private var isBound by mutableStateOf(false)
  private lateinit var stopwatchService: StopwatchService
  private val connection =
      object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
          val binder = service as StopwatchService.StopwatchBinder
          stopwatchService = binder.getService()
          isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
          isBound = false
        }
      }

  override fun onStart() {
    super.onStart()
    Intent(this, StopwatchService::class.java).also { intent ->
      bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        // Creates an instance of preferencesViewModel to get the user's language
        val vm =
            PreferencesViewModel(
                PreferencesRepositoryImpl(applicationContext),
                UserDBRepositoryImpl(applicationContext, NetworkModule.provideFirebaseFirestore()),
                AppModule.provideErrorToast(applicationContext))
        // Get the user's language from PreferencesViewModel
        val language =
            vm.language.stateIn(
                lifecycleScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue =
                    // Set the initial value of the language to the user's system's default
                    when (LocaleListCompat.getDefault().get(0)) {
                      Locale.ENGLISH -> com.lastaoutdoor.lasta.models.user.Language.ENGLISH
                      Locale.FRENCH -> com.lastaoutdoor.lasta.models.user.Language.FRENCH
                      Locale.GERMAN -> com.lastaoutdoor.lasta.models.user.Language.GERMAN
                      else -> com.lastaoutdoor.lasta.models.user.Language.ENGLISH
                    })
        // update the language of the app when language is changed in the DB
        language.collect {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
              !getSystemService(LocaleManager::class.java).applicationLocales.isEmpty) {

            val locale = getSystemService(LocaleManager::class.java).applicationLocales
            val newLocale = LocaleList(Locale.forLanguageTag(it.toLocale()))

            changeLocale(locale.get(0), newLocale.get(0))
          } else {
            // different version for older android versions
            val locale = Locale.getDefault()
            val newLocale = Locale(it.toLocale())
            changeLocale(locale, newLocale)
          }
        }
      }
    }

    setContent {
      val trackingViewModel: TrackingViewModel = hiltViewModel()
      val state = trackingViewModel.state.collectAsState().value
      LastaTheme {
        if (isBound) {
          TrackingScreen(
              stopwatchService = stopwatchService,
              trackingState = state,
              getLocationCallback = trackingViewModel::getLocationCallBack,
              registerSensorListener = trackingViewModel::registerSensorListener,
              trackingViewModel::updateStepCount)
        }
      }
    }
  }

  override fun onStop() {
    super.onStop()
  }

  private fun changeLocale(old: Locale, new: Locale) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (old != new) {
        // Set the new Locale.
        getSystemService(LocaleManager::class.java).applicationLocales = LocaleList(new)

        // Recreate the Activity.
        recreate()
      }
    } else {
      if (old != new) {
        val configuration =
            resources.configuration.apply {
              Locale.setDefault(new)
              setLocale(new)
            }

        resources.updateConfiguration(configuration, resources.displayMetrics)
        recreate()
      }
    }
  }
}
