package com.lastaoutdoor.lasta.di

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.ApiService
import com.lastaoutdoor.lasta.data.api.OutdoorActivityRepositoryImpl
import com.lastaoutdoor.lasta.data.auth.AuthRepositoryImpl
import com.lastaoutdoor.lasta.data.preferences.PreferencesDataStore
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Hilt Module for providing dependencies */
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

  @Singleton @Provides fun provideFirebaseAuth() = Firebase.auth

  @Singleton
  @Provides
  fun provideOneTapClient(@ApplicationContext context: Context): SignInClient =
      Identity.getSignInClient(context)

  @Singleton
  @Provides
  fun provideSignInRequest(@ApplicationContext context: Context): BeginSignInRequest =
      BeginSignInRequest.builder()
          .setGoogleIdTokenRequestOptions(
              BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                  .setSupported(true)
                  .setFilterByAuthorizedAccounts(false)
                  .setServerClientId(context.getString(R.string.web_client_id))
                  .build())
          .setAutoSelectEnabled(true)
          .build()

  @Singleton
  @Provides
  fun provideAPIService(@ApplicationContext context: Context): ApiService =
      Retrofit.Builder()
          .baseUrl(context.getString(R.string.osm_base_url))
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(ApiService::class.java)

  @Singleton
  @Provides
  fun provideAuthRepository(
      auth: FirebaseAuth,
      oneTapClient: SignInClient,
      signInRequest: BeginSignInRequest
  ): AuthRepository = AuthRepositoryImpl(auth, oneTapClient, signInRequest)

  @Singleton
  @Provides
  fun provideOutdoorActivitiesRepository(apiService: ApiService): OutdoorActivityRepository =
      OutdoorActivityRepositoryImpl(apiService)

  /** Provides the [PreferencesDataStore] class */
  @Singleton
  @Provides
  fun providePreferencesDataStore(@ApplicationContext context: Context): PreferencesDataStore =
      PreferencesDataStore(context)

  @Singleton
  @Provides
  fun provideActivitiesRepository(): ActivitiesRepository {
    return ActivitiesRepository()
  }
}
