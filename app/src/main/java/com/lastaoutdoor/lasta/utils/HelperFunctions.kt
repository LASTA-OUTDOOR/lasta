package com.lastaoutdoor.lasta.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.time.TimeProvider
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.log2

/* Initial data for the application */
val firstValidDate: LocalDate = LocalDate.of(1970, 1, 1)

fun calculateTimeRangeUntilNow(
    frame: TimeFrame,
    timeProvider: TimeProvider
): Pair<Timestamp, Timestamp> {
  val today = timeProvider.currentDate()
  val start =
      when (frame) {
        TimeFrame.W -> today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        TimeFrame.M -> today.with(TemporalAdjusters.firstDayOfMonth())
        TimeFrame.Y -> today.with(TemporalAdjusters.firstDayOfYear())
        TimeFrame.ALL -> firstValidDate
      }

  // Set start time at the beginning of the day
  val startTime = LocalDateTime.of(start, LocalTime.MIN)
  // For end time, you could adjust this logic to be the end of the current day/week/month/year if
  // needed
  val endTime = LocalDateTime.of(today, LocalTime.MAX)

  // Convert to Timestamp
  val startInstant = startTime.atZone(ZoneId.systemDefault()).toInstant()
  val endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant()

  val startTimestamp = Timestamp(startInstant.epochSecond, startInstant.nano)
  val endTimestamp = Timestamp(endInstant.epochSecond, endInstant.nano)

  return Pair(startTimestamp, endTimestamp)
}

/**
 * Filters the activities by the selected time frame.
 *
 * @param activities The list of activities to filter.
 * @param timeFrame The selected time frame.
 * @return The filtered list of activities.
 */
fun filterTrailsByTimeFrame(
    activities: List<UserActivity>,
    timeFrame: TimeFrame,
    timeProvider: TimeProvider
): List<UserActivity> {
  return when (timeFrame) {
    TimeFrame.W,
    TimeFrame.M,
    TimeFrame.Y -> {
      val frame = calculateTimeRangeUntilNow(timeFrame, timeProvider)
      activities.filter { activity ->
        val trailStart = Timestamp(activity.timeStarted)
        val trailEnd = Timestamp(activity.timeFinished)
        trailStart > frame.first && trailEnd < frame.second
      }
    }
    TimeFrame.ALL -> activities
  }
}

fun createDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date {
  return Date.from(
      LocalDateTime.of(year, month, day, hour, minute, second)
          .atZone(ZoneId.systemDefault())
          .toInstant())
}

fun chartDisplayValues(activities: List<UserActivity>, timeFrame: TimeFrame): List<Float> {
  val values =
      when (timeFrame) {
        TimeFrame.W,
        TimeFrame.M,
        TimeFrame.Y -> MutableList(timeFrame.toInt()) { 0f }
        TimeFrame.ALL -> {
          val start =
              Calendar.getInstance().apply { time = activities[0].timeStarted }.get(Calendar.YEAR)
          val end =
              Calendar.getInstance()
                  .apply { time = activities[activities.size - 1].timeStarted }
                  .get(Calendar.YEAR)
          ((start..end).map { 0f }).toMutableList()
        }
      }

  for (a in activities) {
    val index =
        when (timeFrame) {
          TimeFrame.W -> indexDayOfWeekFromDate(a.timeStarted)
          TimeFrame.M -> indexWeekOfMonthFromDate(a.timeStarted)
          TimeFrame.Y -> indexMonthOfYearFromDate(a.timeStarted)
          TimeFrame.ALL -> indexYearFromDate(a.timeStarted, a.timeStarted)
        }
    values[index] +=
        when (a.activityType) {
          ActivityType.CLIMBING ->
              metersToKilometers((a as ClimbingUserActivity).totalElevation.toLong())
          ActivityType.HIKING -> metersToKilometers((a as HikingUserActivity).distanceDone.toLong())
          ActivityType.BIKING -> metersToKilometers((a as BikingUserActivity).distanceDone.toLong())
        }
  }
  return values
}

fun indexDayOfWeekFromDate(date: Date): Int {
  val calendar = Calendar.getInstance().apply { time = date }

  // Get the day of the week as an integer
  return (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
}

fun indexWeekOfMonthFromDate(date: Date): Int {
  val calendar =
      Calendar.getInstance().apply {
        time = date
        firstDayOfWeek = Calendar.MONDAY // Set Monday as the first day of the week
      }

  return calendar.get(Calendar.WEEK_OF_MONTH) - 1
}

fun indexMonthOfYearFromDate(date: Date): Int {
  val calendar = Calendar.getInstance().apply { time = date }

  // Get the month as an integer
  return calendar.get(Calendar.MONTH)
}

fun indexYearFromDate(date: Date, startDate: Date): Int {
  val currentYear = Calendar.getInstance().apply { time = date }.get(Calendar.YEAR)
  val start = Calendar.getInstance().apply { time = startDate }.get(Calendar.YEAR)
  return (currentYear - start)
}

fun formatDate(date: Date): String {
  val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
  return formatter.format(date)
}

fun metersToKilometers(meters: Long): Float {
  return meters / 1000f
}

fun timeFromActivityInMillis(activities: List<UserActivity>): Long {
  return activities.sumOf { it.timeFinished.time - it.timeStarted.time }
}

@SuppressLint("DefaultLocale")
fun timeFromMillis(time: Long): String {
  val hours = time / 3600000
  val minutes = (time % 3600000) / 60000
  val seconds = (time % 60000) / 1000
  return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/**
 * Returns a BitmapDescriptor that is a scaled version of the provided vector resource.
 *
 * This function is used to create a BitmapDescriptor from a vector drawable resource. The resulting
 * BitmapDescriptor can be used to customize the appearance of markers on a Google Map, for example.
 *
 * @param context The context used to access resources.
 * @param vectorResId The resource ID of the vector drawable to scale.
 * @param width The desired width of the resulting bitmap.
 * @param height The desired height of the resulting bitmap.
 * @return A BitmapDescriptor that is a scaled version of the provided vector resource, or null if
 *   the resource could not be found.
 */
fun getScaledBitmapDescriptor(
    context: Context,
    vectorResId: Int,
    width: Int,
    height: Int
): BitmapDescriptor? {
  val vectorDrawable: Drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null

  // Scale the drawable
  vectorDrawable.setBounds(0, 0, width, height)

  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) ?: return null
  val canvas = Canvas(bitmap)
  vectorDrawable.draw(canvas)

  return BitmapDescriptorFactory.fromBitmap(bitmap)
}

/**
 * Converts a radius in meters to a zoom level for a Google Map.
 *
 * This function is used to convert a radius in meters to a zoom level for a Google Map. The zoom
 * level is a value between 0 and 21 that determines the level of detail shown on the map. A larger
 * zoom level corresponds to a higher level of detail.
 *
 * @param radiusInMeters The radius in meters.
 * @param screenWidthInPixels The width of the screen in pixels.
 * @param latitude The latitude of the center of the map.
 * @return The zoom level for the given radius.
 */
fun calculateZoomLevel(
    radiusInMeters: Double,
    screenWidthInPixels: Float,
    latitude: Double
): Float {
  val diameter = 2 * radiusInMeters
  val groundResolution = Math.E * diameter / screenWidthInPixels
  val googleMapConstant = 156543.03 // converts from degrees to meters
  return (log2(googleMapConstant * cos(Math.toRadians(latitude)) / groundResolution)).toFloat()
}
