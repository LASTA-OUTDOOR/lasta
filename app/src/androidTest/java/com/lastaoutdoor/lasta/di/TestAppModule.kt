package com.lastaoutdoor.lasta.di

import com.lastaoutdoor.lasta.data.api.FakeOutdoorActivityRepository
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Fake Hilt Module for providing dependencies */
@InstallIn(SingletonComponent::class)
@Module
object TestAppModule {

    @Singleton
    @Provides
    fun provideOutdoorActivitiesRepository(): OutdoorActivityRepository =
        FakeOutdoorActivityRepository()


}