package com.lastaoutdoor.lasta.di

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.notifications.FCMApi
import com.lastaoutdoor.lasta.data.api.osm.ActivityRepositoryImpl
import com.lastaoutdoor.lasta.data.api.osm.OSMApiService
import com.lastaoutdoor.lasta.data.api.weather.WeatherApiService
import com.lastaoutdoor.lasta.data.api.weather.WeatherRepositoryImpl
import com.lastaoutdoor.lasta.data.auth.AuthRepositoryImpl
import com.lastaoutdoor.lasta.data.db.ActivitiesDBRepositoryImpl
import com.lastaoutdoor.lasta.data.db.SocialDBRepositoryImpl
import com.lastaoutdoor.lasta.data.db.TokenDBRepositoryImpl
import com.lastaoutdoor.lasta.data.db.UserActivitiesDBRepositoryImpl
import com.lastaoutdoor.lasta.data.db.UserDBRepositoryImpl
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.repository.api.WeatherRepository
import com.lastaoutdoor.lasta.repository.auth.AuthRepository
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import com.lastaoutdoor.lasta.repository.db.TokenDBRepository
import com.lastaoutdoor.lasta.repository.db.UserActivitiesDBRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @Singleton @Provides fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

  @Provides @Singleton fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

  @Provides
  @Named("signInRequest")
  fun provideSignInRequest(@ApplicationContext context: Context): BeginSignInRequest =
      BeginSignInRequest.builder()
          .setGoogleIdTokenRequestOptions(
              BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                  .setSupported(true)
                  .setFilterByAuthorizedAccounts(true)
                  .setServerClientId(context.getString(R.string.web_client_id))
                  .build())
          .setAutoSelectEnabled(false)
          .build()

  @Provides
  @Named("signUpRequest")
  fun provideSignUpRequest(@ApplicationContext context: Context): BeginSignInRequest =
      BeginSignInRequest.builder()
          .setGoogleIdTokenRequestOptions(
              BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                  .setSupported(true)
                  .setFilterByAuthorizedAccounts(false)
                  .setServerClientId(context.getString(R.string.web_client_id))
                  .build())
          .build()

  @Singleton
  @Provides
  fun provideOSMAPIService(@ApplicationContext context: Context): OSMApiService =
      Retrofit.Builder()
          .baseUrl(context.getString(R.string.osm_base_url))
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(OSMApiService::class.java)

  @Singleton
  @Provides
  fun provideWeatherAPIService(@ApplicationContext context: Context): WeatherApiService =
      Retrofit.Builder()
          .baseUrl(context.getString(R.string.weather_api_url))
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(WeatherApiService::class.java)

  @Singleton
  @Provides
  fun provideFcmAPIService(@ApplicationContext context: Context): FCMApi =
      Retrofit.Builder()
          .baseUrl(context.getString(R.string.notification_server_url))
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(FCMApi::class.java)

  @Singleton
  @Provides
  fun provideActivitiesRepository(osmApiService: OSMApiService): ActivityRepository =
      ActivityRepositoryImpl(osmApiService)

  @Singleton
  @Provides
  fun provideWeatherRepository(weatherApiService: WeatherApiService): WeatherRepository =
      WeatherRepositoryImpl(weatherApiService)

  @Singleton
  @Provides
  fun provideAuthRepository(
      auth: FirebaseAuth,
      oneTapClient: SignInClient,
      @Named("signInRequest") signInRequest: BeginSignInRequest,
      @Named("signUpRequest") signUpRequest: BeginSignInRequest,
      userDBRepository: UserDBRepository
  ): AuthRepository =
      AuthRepositoryImpl(auth, oneTapClient, signInRequest, signUpRequest, userDBRepository)

  @Singleton
  @Provides
  fun provideUserDBRepository(
      @ApplicationContext context: Context,
      firestore: FirebaseFirestore
  ): UserDBRepository = UserDBRepositoryImpl(context, firestore)

  @Singleton
  @Provides
  fun provideActivitiesDBRepository(
      @ApplicationContext context: Context,
      firestore: FirebaseFirestore
  ): ActivitiesDBRepository = ActivitiesDBRepositoryImpl(context, firestore)

  @Singleton
  @Provides
  fun provideUserActivitiesDBRepository(
      @ApplicationContext context: Context,
      firestore: FirebaseFirestore
  ): UserActivitiesDBRepository = UserActivitiesDBRepositoryImpl(context, firestore)

  @Singleton
  @Provides
  fun provideSocialDBRepository(
      @ApplicationContext context: Context,
      firestore: FirebaseFirestore
  ): SocialDBRepository = SocialDBRepositoryImpl(context, firestore)

  @Singleton
  @Provides
  fun provideTokenDBRepository(
      @ApplicationContext context: Context,
      firestore: FirebaseFirestore
  ): TokenDBRepository = TokenDBRepositoryImpl(context, firestore)
}
