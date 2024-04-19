package com.lastaoutdoor.lasta.di

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.ApiService
import com.lastaoutdoor.lasta.data.api.OutdoorActivityRepositoryImpl
import com.lastaoutdoor.lasta.data.auth.AuthRepositoryImpl
import com.lastaoutdoor.lasta.data.connectivity.ConnectivityRepositoryImpl
import com.lastaoutdoor.lasta.data.db.ActivitiesRepositoryImpl
import com.lastaoutdoor.lasta.data.preferences.PreferencesRepositoryImpl
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.repository.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import com.lastaoutdoor.lasta.repository.PreferencesRepository
import com.lastaoutdoor.lasta.repository.SocialRepository
import com.lastaoutdoor.lasta.ui.screen.social.FakeSocialRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Fake Hilt Module for providing dependencies */
@TestInstallIn(components = [SingletonComponent::class], replaces = [AppModule::class])
@Module
object TestAppModule {

  @Singleton @Provides fun provideFirebaseAuth() = Firebase.auth

  @Singleton
  @Provides
  fun provideOneTapClient(@ApplicationContext context: Context): SignInClient =
      Identity.getSignInClient(context)

  /** Provides the [Firebase.firestore] class */
  @Provides @Singleton fun provideFirebaseUser() = Firebase.firestore

  /** Provides the [ApiService] class */
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

  /** Provides the [OutdoorActivityRepository] class */
  @Singleton
  @Provides
  fun provideAuthRepository(
      auth: FirebaseAuth,
      oneTapClient: SignInClient,
      signInRequest: BeginSignInRequest
  ): AuthRepository = AuthRepositoryImpl(auth, oneTapClient, signInRequest)

  @Singleton
  @Provides
  fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository =
      PreferencesRepositoryImpl(context)

  /** Provides the [GoogleAuth] class */
  @Singleton
  @Provides
  fun provideOutdoorActivitiesRepository(apiService: ApiService): OutdoorActivityRepository =
      OutdoorActivityRepositoryImpl(apiService)

  @Singleton
  @Provides
  fun provideActivitiesRepository(
      @ApplicationContext context: Context,
      database: FirebaseFirestore
  ): ActivitiesRepository = ActivitiesRepositoryImpl(database, context)

  @Singleton
  @Provides
  fun provideConnectivityRepository(@ApplicationContext context: Context): ConnectivityRepository =
      ConnectivityRepositoryImpl(context)

  /** Provides the [TimeProvider] class */
  @Provides @Singleton fun provideTimeProvider(): TimeProvider = RealTimeProvider()

  /** Provides the [SocialRepository] class */
  @Singleton
  @Provides
  fun provideSocialRepository(): SocialRepository {
    return FakeSocialRepository()
  }
}
