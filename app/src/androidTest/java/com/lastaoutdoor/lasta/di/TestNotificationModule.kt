package com.lastaoutdoor.lasta.di

import android.app.NotificationManager
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.services.StopwatchServiceHelper
import com.lastaoutdoor.lasta.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@ExperimentalAnimationApi
@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

  @ServiceScoped
  @Provides
  fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
    return NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
        .setContentTitle("Stopwatch")
        .setContentText("00:00:00")
        .setSmallIcon(R.drawable.app_logo)
        .setOngoing(true)
        .addAction(0, "Stop", StopwatchServiceHelper.stopPendingIntent(context))
        .addAction(0, "Cancel", StopwatchServiceHelper.cancelPendingIntent(context))
        .setContentIntent(StopwatchServiceHelper.clickPendingIntent(context))
  }

  @ServiceScoped
  @Provides
  fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }
}
