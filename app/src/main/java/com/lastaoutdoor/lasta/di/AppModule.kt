package com.lastaoutdoor.lasta.di

import android.content.Context
import com.lastaoutdoor.lasta.data.auth.GoogleAuth
import com.lastaoutdoor.lasta.data.preferences.PreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt Module for providing dependencies */
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

  /** Provides the [GoogleAuth] class */
  @Singleton
  @Provides
  fun provideGoogleAuth(@ApplicationContext context: Context) = GoogleAuth(context)

  /** Provides the [PreferencesDataStore] class */
  @Singleton
  @Provides
  fun providePreferencesDataStore(@ApplicationContext context: Context) =
      PreferencesDataStore(context)
}
