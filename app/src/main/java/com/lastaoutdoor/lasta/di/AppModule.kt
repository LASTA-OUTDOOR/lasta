package com.lastaoutdoor.lasta.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.lastaoutdoor.lasta.data.connectivity.ConnectivityRepositoryImpl
import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.data.preferences.PreferencesRepositoryImpl
import com.lastaoutdoor.lasta.data.time.RealTimeProvider
import com.lastaoutdoor.lasta.data.time.TimeProvider
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.app.PreferencesRepository
import com.lastaoutdoor.lasta.repository.offline.ActivityDao
import com.lastaoutdoor.lasta.repository.offline.ActivityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt Module for providing dependencies */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
  @Provides
  fun provideOneTapClient(@ApplicationContext context: Context): SignInClient =
      Identity.getSignInClient(context)

  @Singleton
  @Provides
  fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository =
      PreferencesRepositoryImpl(context)

  @Singleton
  @Provides
  fun provideConnectivityRepository(@ApplicationContext context: Context): ConnectivityRepository =
      ConnectivityRepositoryImpl(context)

  /** Provides the [TimeProvider] class */
  @Provides @Singleton fun provideTimeProvider(): TimeProvider = RealTimeProvider()

  @Provides
  @Singleton
  fun provideAppDatabase(@ApplicationContext appContext: Context): ActivityDatabase {
    return Room.databaseBuilder(appContext, ActivityDatabase::class.java, "room_database").build()
  }

  @Provides
  @Singleton
  fun provideDao(appDatabase: ActivityDatabase): ActivityDao {
    return appDatabase.activityDao
  }

  @Provides
  @Singleton
  fun provideTaskRepository(taskDao: ActivityDao): ActivityDatabaseImpl {
    return ActivityDatabaseImpl(taskDao)
  }
}
