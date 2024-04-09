package com.lastaoutdoor.lasta.utils

import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.model.profile.TimeFrame
import com.lastaoutdoor.lasta.data.model.profile.Trail
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

fun calculateTimeRange(frame: TimeFrame): Pair<Timestamp, Timestamp> {
  val today = LocalDate.now()
  val start =
      when (frame) {
        TimeFrame.W -> today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        TimeFrame.M -> today.with(TemporalAdjusters.firstDayOfMonth())
        TimeFrame.Y -> today.with(TemporalAdjusters.firstDayOfYear())
        TimeFrame.ALL -> LocalDate.of(1970, 1, 1)
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

fun weekDisplay(trails: List<Trail>): List<Float> {
  // Display for each value of TimeFrame the corresponding trails
  val weekValues = mutableListOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)

  for (trail in trails) {
    val dayOfTheWeek = dayOfWeekFromDate(trail.timeStarted)
    weekValues[dayOfTheWeek] += (trail.distanceInMeters.toInt() / 1000f)
  }
  return weekValues
}

fun dayOfWeekFromDate(date: Date): Int {
  val calendar = Calendar.getInstance().apply { time = date }

  // Get the day of the week as an integer
  return (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
}
