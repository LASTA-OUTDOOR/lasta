package com.lastaoutdoor.lasta.ui

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lastaoutdoor.lasta.data.db.UserDBRepositoryImpl
import com.lastaoutdoor.lasta.data.preferences.PreferencesRepositoryImpl
import com.lastaoutdoor.lasta.di.NetworkModule.provideFirebaseFirestore
import com.lastaoutdoor.lasta.ui.navigation.AppNavGraph
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        // Creates an instance of preferencesViewModel to get the user's language
        val vm =
            PreferencesViewModel(
                PreferencesRepositoryImpl(applicationContext),
                UserDBRepositoryImpl(applicationContext, provideFirebaseFirestore()))
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
          val locale = getSystemService(LocaleManager::class.java).applicationLocales
          val newLocale = LocaleList(Locale.forLanguageTag(it.toLocale()))

          if (locale != newLocale) {

            // Set the new Locale.
            getSystemService(LocaleManager::class.java).applicationLocales = newLocale

            // Recreate the Activity.
            recreate()
          }
        }
      }
    }
    setContent { LastaTheme { AppNavGraph() } }
  }
}
