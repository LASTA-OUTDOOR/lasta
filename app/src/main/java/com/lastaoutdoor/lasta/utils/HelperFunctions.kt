package com.lastaoutdoor.lasta.utils

import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.db.Trail
import com.lastaoutdoor.lasta.data.model.user_profile.TimeFrame
import com.lastaoutdoor.lasta.di.TimeProvider
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

fun createDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date {
  return Date.from(
      LocalDateTime.of(year, month, day, hour, minute, second)
          .atZone(ZoneId.systemDefault())
          .toInstant())
}

fun chartDisplayValues(trails: List<Trail>, timeFrame: TimeFrame): List<Float> {
  val values =
      when (timeFrame) {
        TimeFrame.W,
        TimeFrame.M,
        TimeFrame.Y -> MutableList(timeFrame.toInt()) { 0f }
        TimeFrame.ALL -> {
          val start =
              Calendar.getInstance().apply { time = trails[0].timeStarted }.get(Calendar.YEAR)
          val end =
              Calendar.getInstance()
                  .apply { time = trails[trails.size - 1].timeStarted }
                  .get(Calendar.YEAR)
          ((start..end).map { 0f }).toMutableList()
        }
      }

  for (trail in trails) {
    val index =
        when (timeFrame) {
          TimeFrame.W -> indexDayOfWeekFromDate(trail.timeStarted)
          TimeFrame.M -> indexWeekOfMonthFromDate(trail.timeStarted)
          TimeFrame.Y -> indexMonthOfYearFromDate(trail.timeStarted)
          TimeFrame.ALL -> indexYearFromDate(trail.timeStarted, trails[0].timeStarted)
        }
    values[index] += metersToKilometers(trail.distanceInMeters)
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

fun caloriesFromHiking(trails: List<Trail>): Int {
  return trails.sumOf { it.caloriesBurned.toInt() }
}

fun timeFromHikingInMillis(vararg trails: Trail): Long {
  return trails.sumOf { it.timeFinished.time - it.timeStarted.time }
}

fun timeFromMillis(time: Long): String {
  val hours = time / 3600000
  val minutes = (time % 3600000) / 60000
  val seconds = (time % 60000) / 1000
  return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
