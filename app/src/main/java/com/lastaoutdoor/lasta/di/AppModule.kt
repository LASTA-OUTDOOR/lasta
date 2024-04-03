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

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

  @Singleton
  @Provides
  fun provideGoogleAuth(@ApplicationContext context: Context) = GoogleAuth(context)

  @Singleton
  @Provides
  fun providePreferencesDataStore(@ApplicationContext context: Context) =
      PreferencesDataStore(context)
}
