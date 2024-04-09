package com.lastaoutdoor.lasta.di

import android.content.Context
import com.lastaoutdoor.lasta.data.api.ApiService
import com.lastaoutdoor.lasta.data.auth.GoogleAuth
import com.lastaoutdoor.lasta.data.preferences.PreferencesDataStore
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/** Hilt Module for providing dependencies */
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

  /** Provides the [ApiService] class */
  @Singleton
  @Provides
  fun provideApiService(): ApiService {
    return Retrofit.Builder()
      .baseUrl("https://overpass-api.de/api/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(ApiService::class.java)
  }

  /** Provides the [OutdoorActivityRepository] class */
  @Singleton
  @Provides
  fun provideOutdoorActivityRepository(apiService: ApiService): OutdoorActivityRepository {
      return OutdoorActivityRepository(apiService)
  }

  /** Provides the [GoogleAuth] class */
  @Singleton
  @Provides
  fun provideGoogleAuth(@ApplicationContext context: Context) = GoogleAuth(context)

  /** Provides the [PreferencesDataStore] class */
  @Singleton
  @Provides
  fun providePreferencesDataStore(@ApplicationContext context: Context) =
      PreferencesDataStore(context)

  @Singleton
  @Provides
  fun provideActivitiesRepository(): ActivitiesRepository {
    return ActivitiesRepository()
  }
}
